package com.company.my.blog.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.company.my.blog.dto.CommentDto;
import com.company.my.blog.dto.PostDto;
import com.company.my.blog.dto.TagDto;
import com.company.my.blog.dto.UserDto;
import com.company.my.blog.model.RequestMessage;
import com.company.my.blog.model.Post;
import com.company.my.blog.model.User;
import com.company.my.blog.service.CommentService;
import com.company.my.blog.service.PostService;
import com.company.my.blog.service.PostTagService;
import com.company.my.blog.service.TagService;
import com.company.my.blog.service.UserService;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequestMapping(value = "/api/post")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private PostTagService postTagService;

    @Autowired
    private TagService tagService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @GetMapping(value = "/{id}")
    public PostDto getPostById(@PathVariable(value = "id") int id,
            @SessionAttribute(value = "currentUser", required = false) User user) {
        PostDto postDto = postService.getParticularPost(id);
        List<CommentDto> comments = commentService.getCommentsByPostId(postDto);
        List<TagDto> tags = tagService.getTagsName(postDto);
        postDto.setComments(comments);
        postDto.setTags(tags);
        return postDto;
    }

    @PostMapping(value = "/create")
    public ResponseEntity<?> newPostCreate(
            HttpServletRequest request) {
        String title = request.getParameter("title");
        String excerpt = request.getParameter("excerpt");
        String content = request.getParameter("content");
        String tags = request.getParameter("tagsList");

        Authentication auth = (Authentication) request.getUserPrincipal(); 
        if(auth == null){
            return ResponseEntity.badRequest().body(new RequestMessage("You are not logged in"));
        }

        String email = auth.getName();
        User user = userService.getUserByEmail(email);

        if (title != null && excerpt != null && content != null && tags != null) {
            Post post = postService.createNewPost(title, excerpt, content, user);

            if (post != null) {
                postTagService.splitTagsAndSavePostTags(tags, post);
            }
                return ResponseEntity.status(HttpStatus.CREATED).body(new RequestMessage("Post created"));
        }else{
                return ResponseEntity.ok(new RequestMessage("Request processed but due to some error post not created. Try again!"));
        }
        
    }

    @GetMapping(value = "/edit/{id}")
    public ResponseEntity<?> showPostEditForm(
            @PathVariable(value = "id") int postId) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth.getPrincipal() == "anonymousUser"){
            return ResponseEntity.badRequest().body(new RequestMessage("You are not logged in"));
        }

        String email = auth.getName();
        User user = userService.getUserByEmail(email);

        UserDto userDto = new UserDto(user.getId(), user.getName(), user.getEmail());

        PostDto postDto = postService.getParticularPost(postId);
        List<TagDto> tags = tagService.getTagsName(postDto);
        postDto.setTags(tags);
        postDto.setAuthor(userDto);

        return ResponseEntity.ok(postDto);
    }

    @PutMapping(value = "/update/{id}")
    public ResponseEntity<?> processUpdatePostById(
            HttpServletRequest request,
            @PathVariable(value = "id") int postId,
            @ModelAttribute Post post,
            @RequestParam(value = "selectedAuthorEmail", required = false) String selectedAuthor) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    
        if(auth.getPrincipal() == "anonymousUser"){
            return ResponseEntity.badRequest().body(new RequestMessage("You are not logged in"));
        }
        String email = auth.getName();
        User user = userService.getUserByEmail(email);

        String tagsAsText = request.getParameter("tagsList");
        post.setId(postId);
        post.setPublishedAt(postService.getParticularPost(postId).getPublishedAt());
        post.setCreatedAt(postService.getParticularPost(postId).getCreatedAt());
        post.setPublished(true);
        post.setUpdatedAt(new Date());

        postTagService.deleteAllPostTagsByPostId(post);

        if (user.getRole().equals("Admin")) {

            User author = userService.getUserByEmail(selectedAuthor);
            post.setAuthor(author);
            postService.updatePostWithAuthorById(post);

        } else {
            post.setId(postId);
            post.setAuthor(user);
            postService.updatePostById(post);
        }
        postTagService.splitTagsAndSavePostTags(tagsAsText, post);

        return ResponseEntity.ok(new RequestMessage("Post updated"));
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deletePost(@PathVariable(value = "id") int postId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    
        if(auth.getPrincipal() == "anonymousUser"){
            return ResponseEntity.badRequest()
                    .body(new RequestMessage("You are not logged in"));
        }

        try {
            postService.deletePost(postId);
            return ResponseEntity.ok(new RequestMessage("Post deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new RequestMessage("Either post not exists or you are not authorized to delete this post"));
        }
    }
}
