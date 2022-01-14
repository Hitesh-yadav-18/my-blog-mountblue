package com.company.my.blog.repository;

import java.util.List;

import com.company.my.blog.dto.CommentDto;
import com.company.my.blog.model.Comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Query("SELECT new com.company.my.blog.dto.CommentDto(c.id, c.name, c.email, c.comment, c.createdAt, c.updatedAt, c.postId.id) "
            + "FROM Comment c WHERE c.postId.id = :postId")
    List<CommentDto> findCommentsByPostId(@Param("postId") Integer postId);

    @Query("SELECT new com.company.my.blog.dto.CommentDto(c.id, c.name, c.email, c.comment, c.createdAt, c.updatedAt, c.postId.id) from Comment c WHERE c.id = :commentId")
    CommentDto findCommentById(int commentId);

    
}
