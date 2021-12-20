package com.company.my.blog.dao;

import java.util.Date;
import java.util.List;

import com.company.my.blog.model.Comment;
import com.company.my.blog.repository.CommentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

@Service
public class CommentDao {

    @Autowired
    private CommentRepository commentRepository;
    
    @GetMapping("/comment")
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

    @GetMapping("/comment/list")
    public List<Comment> getCommentsByPostId(int postId) {
        return commentRepository.findCommentByPostId(postId);
    }
}
