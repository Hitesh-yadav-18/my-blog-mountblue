package com.company.my.blog.repository;

import java.util.List;

import com.company.my.blog.dto.CommentDto;
import com.company.my.blog.model.Comment;
import com.company.my.blog.model.Post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findCommentByPostId(Post post);

    @Query("SELECT new com.company.my.blog.dto.CommentDto(c.id, c.name, c.email, c.comment, c.createdAt, c.updatedAt) from Comment c WHERE c.id = :commentId")
    CommentDto findCommentById(int commentId);

    
}
