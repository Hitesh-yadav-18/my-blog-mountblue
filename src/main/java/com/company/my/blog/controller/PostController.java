package com.company.my.blog.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.company.my.blog.model.Comment;
import com.company.my.blog.model.Post;
import com.company.my.blog.model.Tag;
import com.company.my.blog.model.User;
import com.company.my.blog.service.CommentService;
import com.company.my.blog.service.PostService;
import com.company.my.blog.service.PostTagService;
import com.company.my.blog.service.TagService;
import com.company.my.blog.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequestMapping(value = "/post")
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
    public String getPostById(@PathVariable(value = "id") int id,
                              @SessionAttribute(value="currentUser", required = false) User user,
                              Model model) {
        boolean isCurrentUserIsPostOwner =false;                          
        Post post = postService.getParticularPost(id);
        List<Comment> comments = commentService.getCommentsByPostId(post);
        List<Tag> tags = new ArrayList<>();

        for (int i = 0; i < post.getPostTags().size(); i++) {
            tags.add(post.getPostTags().get(i).getTag());
        }

        if(user != null){
            isCurrentUserIsPostOwner = postService.isCurrentUserIsPostOwner(post, user);
        }

        model.addAttribute("isCurrentUserIsPostOwner", isCurrentUserIsPostOwner);
        model.addAttribute("user", user);
        model.addAttribute("comments", comments);
        model.addAttribute("post", post);
        model.addAttribute("tags", tags);
        return "post";
    }

    @GetMapping(value = "/create")
    public String getCreatePostPage() {
        return "create";
    }

    @PostMapping(value = "/create/save")
    public String newPostCreate(
        HttpServletRequest request, 
        @SessionAttribute(value="currentUser", required = false) User user,
        Model model) {
        String title = request.getParameter("title");
        String excerpt = request.getParameter("excerpt");
        String content = request.getParameter("content");
        String tags = request.getParameter("tagsList");
        if (title != null && excerpt != null && content != null && tags != null) {
            Post post = postService.createNewPost(title, excerpt, content, user);

            if (post != null) {
                postTagService.splitTagsAndSavePostTags(tags, post);
            }
        }
        return "redirect:/post/create";
    }

    @GetMapping(value = "/editPost/{id}")
    public String showPostEditForm(
        @PathVariable(value = "id") int postId, 
        @SessionAttribute(value="currentUser", required = false) User user,
        Model model) {
        Post post = postService.getParticularPost(postId);
        List<User> authorsList = userService.getAllUsers();
        List<String> tags = tagService.getTagsName(post);
        String tagsAsText = String.join(",", tags);
        
        model.addAttribute("user", user);
        model.addAttribute("post", post);
        model.addAttribute("authorsList", authorsList);
        model.addAttribute("tags", tagsAsText);
        return "edit-post";
    }

    @PostMapping(value = "/postUpdate/{id}")
    public String processUpdatePostById(
            HttpServletRequest request,
            @PathVariable(value = "id") int postId,
            @ModelAttribute Post post,
            @RequestParam(value = "selectedAuthor", required = false) String selectedAuthor,
            @SessionAttribute(value="currentUser", required = false) User user) {
        String tagsAsText = request.getParameter("tagsList");
        post.setId(postId);
        post.setPublishedAt(postService.getParticularPost(postId).getPublishedAt());
        post.setCreatedAt(postService.getParticularPost(postId).getCreatedAt());
        post.setPublished(true);
        post.setUpdatedAt(new Date());
        postTagService.deleteAllPostTagsByPostId(post);
        
        if(user.getRole().equals("Admin")){
            
            User author = userService.getUserByEmail(selectedAuthor);
            post.setAuthor(author);            
            postService.updatePostWithAuthorById(post);
            
        }else{
           postService.updatePostById(
                        postId, 
                        post.getTitle(),
                        post.getExcerpt(), 
                        post.getContent(), 
                        post.getUpdatedAt());
        }
        postTagService.splitTagsAndSavePostTags(tagsAsText, post);
         
        return "redirect:/post/" + postId;
    }

    @DeleteMapping(value = "/postDelete/{id}")
    @ResponseBody
    public String deletePost(@PathVariable(value = "id") int postId) {  
        postService.deletePost(postId);
        return "success";
    }
}
