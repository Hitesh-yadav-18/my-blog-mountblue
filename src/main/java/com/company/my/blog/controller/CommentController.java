package com.company.my.blog.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.company.my.blog.dto.CommentDto;
import com.company.my.blog.model.Comment;
import com.company.my.blog.model.Post;
import com.company.my.blog.model.RequestMessage;
import com.company.my.blog.service.CommentService;
import com.company.my.blog.service.PostService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private PostService postService;

    @PostMapping(value = "/create")
    public ResponseEntity<?> createPostComment(HttpServletRequest request) {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String comment = request.getParameter("comment");
        String postId = request.getParameter("postId");
        Post post = new Post();
        post.setId(Integer.parseInt(postId));
        try {
            if (name != null && email != null && comment != null && post != null) {
                commentService.addNewCommentToPost(name, email, comment, post);
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(new RequestMessage("Comment added successfully"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                .body(new RequestMessage("Comment not added"));
    }

    @GetMapping("/edit/{id}")
    public ResponseEntity<?> showCommentEditForm(@PathVariable("id") int id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getPrincipal() == "anonymousUser") {
            return ResponseEntity.badRequest().body(new RequestMessage("You are not logged in"));
        }

        CommentDto commentDto = commentService.getCommentById(id);
        if (commentDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new RequestMessage("Comment not found"));
        }
        String currentUserEmailId = postService.getParticularPost(commentDto.getPostId())
                .getAuthor().getEmail();
        if (currentUserEmailId.equals(auth.getName())) {
            return ResponseEntity.ok(commentDto);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new RequestMessage("You are not authorized to edit this comment"));
        }

    }

    @PutMapping(value = "/update/{commentId}")
    public ResponseEntity<?> processUpdateCommentById(
            @PathVariable(value = "commentId") int commentId,
            @ModelAttribute(value = "comments") Comment comment) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getPrincipal() == "anonymousUser") {
            return ResponseEntity.badRequest()
                    .body(new RequestMessage("You are not logged in"));
        }

        CommentDto commentDto = commentService.getCommentById(commentId);
        if (commentDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new RequestMessage("Comment not found"));
        }
        String currentUserEmailId = postService.getParticularPost(commentDto.getPostId())
                .getAuthor().getEmail();
        if (currentUserEmailId.equals(auth.getName())) {
            try {
                comment.setId(commentId);
                comment.setCreatedAt(commentService.getCommentById(commentId).getCreatedAt());
                comment.setUpdatedAt(new Date());
                comment.setPostId(postService.getParticularFullPost(commentDto.getPostId()));
                commentService.updateExistingCommentById(comment);

                return ResponseEntity.status(HttpStatus.OK)
                        .body(new RequestMessage("Comment updated successfully"));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                        .body(new RequestMessage("Unable to update the comment. Try again"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new RequestMessage("You are not authorized to edit this comment"));
        }

    }

    @DeleteMapping(value = "/delete/{commentId}")
    public ResponseEntity<?> processDeleteCommentById(@PathVariable(value = "commentId") int commentId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getPrincipal() == "anonymousUser") {
            return ResponseEntity.badRequest().body(new RequestMessage("You are not logged in"));
        }

        CommentDto commentDto = commentService.getCommentById(commentId);
        if (commentDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new RequestMessage("Comment not found"));
        }
        String currentUserEmailId = postService.getParticularPost(commentDto.getPostId())
                .getAuthor().getEmail();
        if (currentUserEmailId.equals(auth.getName())) {
            try {
                commentService.deleteExistingCommentById(commentId);
                return ResponseEntity.status(HttpStatus.FOUND)
                        .body(new RequestMessage("Comment deleted successfully"));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                        .body(new RequestMessage("Comment not deleted. Please try again"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new RequestMessage("You are not authorized to edit this comment"));
        }
    }

}
