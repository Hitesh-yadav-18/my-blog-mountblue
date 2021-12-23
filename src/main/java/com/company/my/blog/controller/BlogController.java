package com.company.my.blog.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.company.my.blog.Service.CommentService;
import com.company.my.blog.Service.PostService;
import com.company.my.blog.model.Comment;
import com.company.my.blog.model.Post;

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

@Controller
@RequestMapping(value = "/")
public class BlogController {

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;
    

    @GetMapping("/")
    public String getIndexPage() {
        return "redirect:/?start=0&limit=4";
    }

    @GetMapping(value = "post/{id}")
    public String getPostById(@PathVariable(value = "id") int id, Model model) {
        Post posts = postService.getParticularPost(id);
        List<Comment> comments = commentService.getCommentsByPostId(id);
        if (posts != null) {
            model.addAttribute("comments", comments);
            model.addAttribute("post", posts);
            return "post";
        }
        return "redirect:/post/create";
    }

    @GetMapping(value = "post/create")
    public String getCreatePostPage() {
        return "create";
    }

    @PostMapping(value = "post/create/save")
    public String newPostCreate(HttpServletRequest request, Model model) {
        String title = request.getParameter("title");
        String excerpt = request.getParameter("excerpt");
        String content = request.getParameter("content");
        if (title != null && excerpt != null && content != null) {
            postService.createNewPost(title, excerpt, content);
        } else {
            model.addAttribute("error", "Please insert all fields");
        }
        return "redirect:/post/create";
    }

    @GetMapping(value = "/search/{searchedValue}")
    public String getSearchPage(@PathVariable(value="searchedValue") String searchedValue, Model model) {
        List<Post> posts = postService.getAllPostsBySearchedValue(searchedValue);
        model.addAttribute("posts", posts);
        return "index";
    }

    // Filter methods
    @GetMapping(value = "/author")
    public String getAuthorFilterOnPosts(Model model) {
        List<Post> posts = postService.getPostByAuthor("Admin");
        model.addAttribute("posts", posts);
        return "index";
    }

    //sort methods
    @GetMapping(value = "/sortByDesc")
    public String getSortPostsByPublishedDateDesc(Model model) {
        List<Post> posts = postService.getAllPostsByPublishedDateDesc();
        model.addAttribute("posts", posts);
        return "index";
    }

    @GetMapping(value = "/sortByAsc")
    public String getSortPostsByPublishedDateAsc(Model model) {
        List<Post> posts = postService.getAllPostsByPublishedDateAsc();
        model.addAttribute("posts", posts);
        return "index";
    }

    @PostMapping(value = "/post/comment/save")
    public String createPostComment(HttpServletRequest request, Model model) {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String comment = request.getParameter("comment");
        String postId = request.getParameter("postId");
        if (name != null && email != null && comment != null && postId != null) {
            commentService.addNewCommentToPost(name, email, comment, Integer.parseInt(postId));
        } else {
            model.addAttribute("error", "Please insert all fields");
        }
        return "redirect:/post/" + postId;
    }

    @GetMapping(value = "/editPost/{id}")
    public String showPostEditForm(@PathVariable(value="id") int id, Model
    model) {
        Post post = postService.getParticularPost(id);
        model.addAttribute("post", post);
        return "edit-post";
    }

    @PostMapping(value = "/postUpdate/{id}")
    public String processUpdatePostById(
        @PathVariable(value="id") int postId,
        @ModelAttribute(value="post") Post post ){
        post.setUpdatedAt(new Date());
        postService.updatePostById(postId, post.getTitle(),
             post.getExcerpt(), post.getContent(), post.getUpdatedAt());
        return "redirect:/post/" + postId;     
    }

    @DeleteMapping(value = "/post/delete/{id}")
    public String deletePost(@PathVariable(value = "id") int id) {
        // postService.deletePost(id);
        return "redirect:/";
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
            @ModelAttribute(value = "comments") Comment commentObj) {
        int postId = commentObj.getPostId();
        commentService.updateExistingCommentById(commentId, commentObj.getComment());
        return "redirect:/post/" + postId;
    }

    @GetMapping(value = "/deleteComment/{postId}/{commentId}")
    public String processDeleteCommentById(@PathVariable(value = "postId") int postId,
            @PathVariable(value = "commentId") int commentId) {
        commentService.deleteExistingCommentById(commentId);
        return "redirect:/post/" + postId;
    }

    
    @GetMapping(value = "/", params = {"start", "limit"})
    public String getPage(@RequestParam("start") int startPage,
            @RequestParam("limit") int endPage, Model model) {
        List<Post> posts = postService.getAllPostsByPage(startPage, endPage);
        model.addAttribute("posts", posts);
        if(posts.size() >= 4){
            model.addAttribute("currentPage", (startPage/endPage) + 1);
        }else{
            model.addAttribute("currentPage", "last");
        }

        return "index";
    }

}
