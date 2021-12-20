package com.company.my.blog.repository;

import java.util.List;

import com.company.my.blog.model.Comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findCommentByPostId(int postId);
    
}
