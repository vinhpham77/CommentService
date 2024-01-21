package org.caykhe.commentservice.repositories;

import org.caykhe.commentservice.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    Optional<Comment> findByTargetIdAndType(Integer targetId, boolean type);
}