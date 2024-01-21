package org.caykhe.commentservice.services;

import lombok.RequiredArgsConstructor;
import org.caykhe.commentservice.dtos.ApiException;
import org.caykhe.commentservice.dtos.SubCommentDto;
import org.caykhe.commentservice.dtos.User;
import org.caykhe.commentservice.models.Comment;
import org.caykhe.commentservice.models.CommentDetails;
import org.caykhe.commentservice.repositories.CommentDetailsRepository;
import org.caykhe.commentservice.repositories.CommentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentDetailsRepository commentDetailsRepository;
    private final UserService userService;
    private final ContentService contentService;
    private final ImageService imageService;

    public Comment create(Integer targetId, boolean type) {

        var comment = Comment
                .builder()
                .targetId(targetId)
                .type(type)
                .build();

        return commentRepository.save(comment);
    }

    public void deleteComment(String token, Integer targetId, boolean type) {
        Comment comment = commentRepository.findByTargetIdAndType(targetId, type)
                .orElseThrow(() -> new ApiException("Comment không tồn tại", HttpStatus.NOT_FOUND));
        List<CommentDetails> listSubCom = commentDetailsRepository.findByComment(comment);
        for (CommentDetails subComment: listSubCom) {
            imageService.deleteByContent(token, subComment.getContent());
        }
        commentRepository.delete(comment);
    }

    public CommentDetails addSubComment(String token, Integer targetId, boolean type , SubCommentDto newSubComment) {
        Comment comment = commentRepository.findByTargetIdAndType(targetId, type)
                .orElseThrow(() -> new ApiException("Comment không tồn tại", HttpStatus.NOT_FOUND));
        List<CommentDetails> listSubCom = commentDetailsRepository.findByComment(comment);
        User user = userService.getByUsername(newSubComment.getUsername())
                .orElseThrow(() -> new ApiException("Người dùng @" + newSubComment.getUsername() + " không tồn tại", HttpStatus.NOT_FOUND));
        int right;
        CommentDetails subCommentFather = null;
        if(newSubComment.getSubCommentFatherId() == null)
            right = listSubCom.size()*2 + 1;
        else {
            subCommentFather = listSubCom.stream().filter(i -> i.getId().equals(newSubComment.getSubCommentFatherId())).findFirst()
                    .orElseThrow(() -> new ApiException("Comment không tồn tại", HttpStatus.NOT_FOUND));
            right = subCommentFather.getRight();
        }

        imageService.saveByContent(token, newSubComment.getContent());

        listSubCom.forEach(subComment -> {
            if (subComment.getLeft() > right) {
                subComment.setLeft(subComment.getLeft() + 2);
                subComment.setRight(subComment.getRight() + 2);
            }
            else if(subComment.getRight() > right) {
                subComment.setRight(subComment.getRight() + 2);
            }
        });

        CommentDetails subComment = CommentDetails
                .builder()
                .comment(comment)
                .createdBy(user.getUsername())
                .content(newSubComment.getContent())
                .updatedAt(new Date().toInstant())
                .left(right)
                .right(right + 1)
                .build();
        if(subCommentFather != null && subComment != null) {
            subCommentFather.setRight(right + 2);
        }
        listSubCom.add(subComment);
        commentDetailsRepository.saveAll(listSubCom);
        updateCommentCount(token, comment, listSubCom.size());
        return subComment;
    }

    public boolean removeSubComment(String token, Integer targetId, Integer subId, boolean type) {

        Comment comment = commentRepository.findByTargetIdAndType(targetId, type)
                .orElseThrow(() -> new ApiException("Comment không tồn tại", HttpStatus.NOT_FOUND));
        List<CommentDetails> listSubCom = commentDetailsRepository.findByComment(comment);
        CommentDetails subComment = listSubCom.stream().filter(i -> i.getId().equals(subId)).findFirst()
                .orElseThrow(() -> new ApiException("Comment không tồn tại", HttpStatus.NOT_FOUND));
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!subComment.getCreatedBy().equals(username)) {
            throw new ApiException("Bạn không có quyền xóa comment này", HttpStatus.FORBIDDEN);
        }

        int a = subComment.getRight() - subComment.getLeft() + 1;
        int left = subComment.getLeft();
        int right = subComment.getRight();
        boolean result = listSubCom.removeIf((subCom) -> {
            if(subCom.getLeft() >= left && subCom.getRight() <= right) {
                imageService.deleteByContent(token, subCom.getContent());
                commentDetailsRepository.deleteById(subCom.getId());
                return true;
            }
            return false;
        });

        listSubCom.forEach(subCom -> {
            if (subCom.getLeft() > left) {
                subCom.setLeft(subCom.getLeft() - a);
                subCom.setRight(subCom.getRight() - a);
            }
        });
        commentDetailsRepository.saveAll(listSubCom);
        updateCommentCount(token, comment, listSubCom.size());
        return result;
    }

    public CommentDetails updateSubComment(String token, Integer targetId, boolean type, Integer subId, SubCommentDto subCommentDto) {
        Comment comment = commentRepository.findByTargetIdAndType(targetId, type)
                .orElseThrow(() -> new ApiException("Comment không tồn tại", HttpStatus.NOT_FOUND));
        List<CommentDetails> listSubCom = commentDetailsRepository.findByComment(comment);
        CommentDetails subComment = listSubCom.stream().filter(i -> i.getId().equals(subId)).findFirst()
                .orElseThrow(() -> new ApiException("Comment không tồn tại", HttpStatus.NOT_FOUND));

        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!subComment.getCreatedBy().equals(username)) {
            throw new ApiException("Bạn không có quyền chỉnh sửa comment này", HttpStatus.FORBIDDEN);
        }

        imageService.deleteByContent(token, subComment.getContent());
        imageService.saveByContent(token, subCommentDto.getContent());
        subComment.setContent(subCommentDto.getContent());
        subComment.setUpdatedAt(new Date().toInstant());
        commentDetailsRepository.saveAll(listSubCom);
        updateCommentCount(token, comment, listSubCom.size());
        return subComment;
    }

    public List<CommentDetails> getComments(Integer targetId, boolean type, Integer subId) {
        Comment comment = commentRepository.findByTargetIdAndType(targetId, type)
                .orElseThrow(() -> new ApiException("Comment không tồn tại", HttpStatus.NOT_FOUND));
        List<CommentDetails> listSubComment = commentDetailsRepository.findByComment(comment);
        int number = 0;
        if(subId != null){
            CommentDetails subComment = listSubComment.stream().filter(i -> i.getId().equals(subId)).findFirst()
                    .orElseThrow(() -> new ApiException("Comment không tồn tại", HttpStatus.NOT_FOUND));
            number = subComment.getLeft();
        }

        List<CommentDetails> nextComments = new ArrayList<>();
        int currentRight = number;
        for (CommentDetails subComent : listSubComment) {
            if (subComent.getLeft() == currentRight + 1) {
                nextComments.add(subComent);
                currentRight = subComent.getRight();
            }
        }

        return nextComments;
    }

    private void updateCommentCount(String token, Comment comment, int commentCount) {
        if(comment.getType())
            updateSeriesCommentCount(token, comment.getTargetId(), commentCount);
        else
            updatePostCommentCount(token, comment.getTargetId(), commentCount);

    }

    public void updatePostCommentCount(String token, Integer id, int commentCount) {
        contentService.upCountCommentOfPost(token, id, commentCount);
    }

    public void updateSeriesCommentCount(String token, Integer id, int commentCount) {
        contentService.upCountCommentOfSeries(token, id, commentCount);
    }
}
