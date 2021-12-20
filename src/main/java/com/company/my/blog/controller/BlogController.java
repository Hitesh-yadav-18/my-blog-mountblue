package com.company.my.blog.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.company.my.blog.dao.CommentDao;
import com.company.my.blog.dao.PostDao;
import com.company.my.blog.model.Comment;
import com.company.my.blog.model.Post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/")
public class BlogController {

    @Autowired
    private PostDao postDao;

    @Autowired
    private CommentDao commentDao;

    @GetMapping
    public String getIndexPage(Model model) {
        List<Post> posts = postDao.getAllPosts();
        model.addAttribute("posts", posts);
        return "index";
    }

    @GetMapping(value = "post/{id}")
    public String getPostById(@PathVariable(value="id") int id, Model model) {
        Post posts = postDao.getParticularPost(id);
        List<Comment> comments = commentDao.getCommentsByPostId(id);
        if(posts != null) {
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
        if(title != null && excerpt != null && content != null) {
            postDao.createNewPost(title, excerpt, content);
        }else{
            model.addAttribute("error", "Please insert all fields");
        }
        return "redirect:/post/create";
    }

    //Filter methods
    @GetMapping(value = "/author")
    public String getAuthorPosts(Model model) {
        List<Post> posts = postDao.getPostByAuthor("Admin");
        model.addAttribute("posts", posts);
        return "index";
    }
    
    @PostMapping(value = "/post/comment/save")
    public String postComment(HttpServletRequest request, Model model) {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String comment = request.getParameter("comment");
        String postId = request.getParameter("postId");
        if(name != null && email != null && comment != null && postId != null) {
            commentDao.addNewCommentToPost(name, email, comment, Integer.parseInt(postId));
        }else{
            model.addAttribute("error", "Please insert all fields");
        }
        return "redirect:/post/"+postId;
    }

    @GetMapping(value = "/post/edit/{id}")
    public String getEditPage(@PathVariable(value="id") int id, ModelAndView model) {
        model.setViewName("edit", id);
        return "redirect:/post/create";
    }

}
