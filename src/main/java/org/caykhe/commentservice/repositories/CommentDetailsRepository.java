package org.caykhe.commentservice.repositories;

import org.caykhe.commentservice.models.Comment;
import org.caykhe.commentservice.models.CommentDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentDetailsRepository extends JpaRepository<CommentDetails, Integer> {

    List<CommentDetails> findByComment(Comment comment);
}
