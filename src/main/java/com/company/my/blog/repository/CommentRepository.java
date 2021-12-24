package com.company.my.blog.repository;

import java.util.List;

import com.company.my.blog.model.Comment;
import com.company.my.blog.model.Post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findCommentByPostId(Post post);

    Comment findCommentById(int commentId);

    @Transactional
    void deleteCommentById(int commentId);

    @Modifying
    @Transactional
    @Query(
        value = "UPDATE comment SET comment = ?2  WHERE id = ?1", 
        nativeQuery = true
        )
    void updateCommentById(int commentId, String comment);
    
}
