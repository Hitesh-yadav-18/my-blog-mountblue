package com.company.my.blog.Service;

import java.util.Date;
import java.util.List;

import com.company.my.blog.model.Comment;
import com.company.my.blog.repository.CommentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    
    public void addNewCommentToPost(String name, String email, String commentContent, int postId) {
        Comment comment = new Comment();
        comment.setName(name);
        comment.setEmail(email);
        comment.setComment(commentContent);
        comment.setPostId(postId);
        comment.setCreatedAt(new Date());
        comment.setUpdatedAt(new Date());

        commentRepository.save(comment);
    }

    public List<Comment> getCommentsByPostId(int postId) {
        return commentRepository.findCommentByPostId(postId);
    }

    public Comment getCommentById(int commentId) {
        return commentRepository.findCommentById(commentId);
    }

    public void deleteExistingCommentById(int commentId) {
        commentRepository.deleteCommentById(commentId);
    }

    public void updateExistingCommentById(int commentId, String comment) {
        commentRepository.updateCommentById(commentId, comment);
    }
}
