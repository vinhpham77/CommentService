package org.caykhe.commentservice.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.caykhe.commentservice.dtos.ApiException;
import org.caykhe.commentservice.dtos.SubCommentDto;
import org.caykhe.commentservice.models.Comment;
import org.caykhe.commentservice.models.CommentDetails;
import org.caykhe.commentservice.services.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestParam(name = "targetId") Integer targetId,
                                    @RequestParam(required = false, name = "type", defaultValue = "false") boolean type) {
        Comment createdComment = commentService.create(targetId, type);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }

    @PostMapping("/delete")
    public ResponseEntity<?> delete(HttpServletRequest request, @RequestParam(name = "targetId") Integer targetId,
                                    @RequestParam(required = false, name = "type", defaultValue = "false") boolean type) {
        commentService.deleteComment(request.getHeader("Authorization"), targetId, type);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/delete/{targetId}/{isSeries}")
    public ResponseEntity<?> create(HttpServletRequest request, @PathVariable Integer targetId, @PathVariable Boolean isSeries) {
        commentService.deleteComment(request.getHeader("Authorization"), targetId, isSeries);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{targetId}/{type}/add")
    public ResponseEntity<?> addSubcomment(HttpServletRequest request, @PathVariable Integer targetId, @PathVariable boolean type,
                                           @Valid @RequestBody SubCommentDto subCommentDto) {
        if (subCommentDto == null) {
            throw new ApiException("Dữ liệu không hợp lệ", HttpStatus.BAD_REQUEST);
        }

        CommentDetails subCommentAggregate = commentService.addSubComment(request.getHeader("Authorization"), targetId, type, subCommentDto);
        return new ResponseEntity<>(subCommentAggregate, HttpStatus.CREATED);
    }

    @DeleteMapping("/{targetId}/{type}/{subId}/delete")
    public ResponseEntity<?> removeSubcomment(HttpServletRequest request, @PathVariable Integer targetId,@PathVariable Integer subId, @PathVariable boolean type) {

        boolean result = commentService.removeSubComment(request.getHeader("Authorization"), targetId, subId, type);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/{targetId}/{type}/{subId}/update")
    public ResponseEntity<?> updateSubComment(HttpServletRequest request, @PathVariable Integer targetId, @PathVariable Integer subId,
                                              @PathVariable boolean type, @RequestBody SubCommentDto subCommentDto){
        if (subCommentDto == null) {
            throw new ApiException("Dữ liệu không hợp lệ", HttpStatus.BAD_REQUEST);
        }

        CommentDetails subCommentAggregate = commentService.updateSubComment(request.getHeader("Authorization"), targetId, type, subId, subCommentDto);
        return new ResponseEntity<>(subCommentAggregate, HttpStatus.OK);
    }

    @GetMapping("/{targetId}/{type}/get")
    public ResponseEntity<?> getComment(@PathVariable Integer targetId, @PathVariable boolean type,
                                        @RequestParam(required = false, name = "subId") Integer subId){

        List<CommentDetails> subCommentAggregates = commentService.getComments(targetId, type, subId);
        return new ResponseEntity<>(subCommentAggregates, HttpStatus.OK);
    }
}
