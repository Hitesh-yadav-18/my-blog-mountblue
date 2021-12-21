package com.company.my.blog.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.company.my.blog.Service.CommentService;
import com.company.my.blog.Service.PostService;
import com.company.my.blog.model.Comment;
import com.company.my.blog.model.Post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/")
public class BlogController {

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @GetMapping
    public String getIndexPage(Model model) {
        List<Post> posts = postService.getAllPosts();
        model.addAttribute("posts", posts);
        return "index";
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
    public String getCreatePage() {
        return "create";
    }

    @PostMapping(value = "post/create/save")
    public String postCreate(HttpServletRequest request, Model model) {
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

    @GetMapping(value = "/search")
    public String getSearchPage(Model model) {
        return "redirect:/index";
    }

    // Filter methods
    @GetMapping(value = "/author")
    public String getAuthorPosts(Model model) {
        List<Post> posts = postService.getPostByAuthor("Admin");
        model.addAttribute("posts", posts);
        return "index";
    }

    @PostMapping(value = "/post/comment/save")
    public String postComment(HttpServletRequest request, Model model) {
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

    // @GetMapping(value = "/post/edit/{id}")
    // public String getEditPage(@PathVariable(value="id") int id, ModelAndView
    // model) {
    // model.setViewName("edit", id);
    // return "redirect:/post/create";
    // }

    @GetMapping("/editComment/{id}")
    public String showEditForm(@PathVariable("id") int id, Model model) {
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


}
