package com.company.my.blog.controller;

import javax.servlet.http.HttpServletRequest;

import com.company.my.blog.model.Comment;
import com.company.my.blog.model.Post;
import com.company.my.blog.service.CommentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping(value = "/post/comment/save")
    public String createPostComment(HttpServletRequest request, Model model) {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String comment = request.getParameter("comment");
        String postId = request.getParameter("postId");
        Post post = new Post();
        post.setId(Integer.parseInt(postId));
        if (name != null && email != null && comment != null && post != null) {
            commentService.addNewCommentToPost(name, email, comment, post);
        }
        return "redirect:/post/" + postId;
    }

    @GetMapping("/editComment/{id}")
    public String showCommentEditForm(@PathVariable("id") int id, Model model) {
        Comment comment = commentService.getCommentById(id);
        model.addAttribute("comments", comment);
        return "edit-comment";
    }

    @PostMapping(value = "/updateComment/{commentId}")
    public String processUpdateCommentById(
            @PathVariable(value = "commentId") int commentId,
            @ModelAttribute(value = "comments") Comment comment,
            @RequestParam("postId") Integer postId) {
        comment.setId(commentId);
        comment.setCreatedAt(commentService.getCommentById(commentId).getCreatedAt());
        comment.setUpdatedAt(new Date());
        commentService.updateExistingCommentById(comment);
        return "redirect:/post/" + postId;
    }

    @GetMapping(value = "/deleteComment/{postId}/{commentId}")
    public String processDeleteCommentById(@PathVariable(value = "postId") int postId,
            @PathVariable(value = "commentId") int commentId) {
        commentService.deleteExistingCommentById(commentId);
        return "redirect:/post/" + postId;
    }

}
