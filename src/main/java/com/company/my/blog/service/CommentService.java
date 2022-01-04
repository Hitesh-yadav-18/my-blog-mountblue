package com.company.my.blog.service;

import java.util.Date;
import java.util.List;

import com.company.my.blog.model.Comment;
import com.company.my.blog.model.Post;
import com.company.my.blog.repository.CommentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    
    public void addNewCommentToPost(
        String name,
        String email, 
        String commentContent, 
        Post post) {
        Comment comment = new Comment();
        comment.setName(name);
        comment.setEmail(email);
        comment.setComment(commentContent);
        comment.setPostId(post);
        comment.setCreatedAt(new Date());
        comment.setUpdatedAt(new Date());

        commentRepository.save(comment);
    }

    public List<Comment> getCommentsByPostId(Post post) {
        return commentRepository.findCommentsByPostId(post);
    }

    public Comment getCommentById(int commentId) {
        return commentRepository.findCommentById(commentId);
    }

    public void deleteExistingCommentById(int commentId) {
        commentRepository.deleteById(commentId);
    }

    public void updateExistingCommentById(Comment comment) {
        commentRepository.save(comment);
    }
}
