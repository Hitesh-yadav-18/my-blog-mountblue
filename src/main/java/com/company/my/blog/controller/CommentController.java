package com.company.my.blog.controller;

import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.company.my.blog.dto.CommentDto;
import com.company.my.blog.model.Comment;
import com.company.my.blog.model.Post;
import com.company.my.blog.service.CommentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping(value = "/post/comment/save")
    public ResponseEntity<String> createPostComment(HttpServletRequest request) {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String comment = request.getParameter("comment");
        String postId = request.getParameter("postId");
        Post post = new Post();
        post.setId(Integer.parseInt(postId));
        try{
            if (name != null && email != null && comment != null && post != null) {
            commentService.addNewCommentToPost(name, email, comment, post);
            return ResponseEntity.status(HttpStatus.CREATED).body("Comment created successfully");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Comment not created. Please try again");
    }

    @GetMapping("/editComment/{id}")
    public ResponseEntity<CommentDto> showCommentEditForm(@PathVariable("id") int id) {
        try{
            CommentDto commentDto = commentService.getCommentById(id);
            return ResponseEntity.of(Optional.of(commentDto));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

   @PutMapping(value = "/updateComment/{commentId}")
   public ResponseEntity<String> processUpdateCommentById(
           @PathVariable(value = "commentId") int commentId,
           @ModelAttribute(value = "comments") Comment comment,
           @RequestParam("postId") Integer postId) {
         try{
            comment.setId(commentId);
            comment.setCreatedAt(commentService.getCommentById(commentId).getCreatedAt());
            comment.setUpdatedAt(new Date());
            commentService.updateExistingCommentById(comment);
        
            return ResponseEntity.status(HttpStatus.FOUND).body("Comment updated successfully");
         }catch(Exception e){
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comment not updated. Please try again");
         }
   }

   @DeleteMapping(value = "/deleteComment/{postId}/{commentId}")
   public ResponseEntity<String> processDeleteCommentById(@PathVariable(value = "postId") int postId,
           @PathVariable(value = "commentId") int commentId) {
       try{
              commentService.deleteExistingCommentById(commentId);
              return ResponseEntity.status(HttpStatus.FOUND).body("Comment deleted successfully");
       }
       catch(Exception e){
              return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Either comment not found or not deleted. Please try again");
       }
   }

}
