package com.company.my.blog.controller;

import java.util.List;

import com.company.my.blog.Service.PostService;
import com.company.my.blog.model.Post;
import com.company.my.blog.model.User;
import com.company.my.blog.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/")
public class BlogController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("")
    public String getIndexPage() {
        return "redirect:/?start=0&limit=4";
    }

    @GetMapping(value = "/", params = { "start", "limit" })
    public String getPage(@RequestParam("start") int startPage,
            @RequestParam("limit") int endPage, Model model) {
        List<Post> posts = postService.getAllPostsByPage(startPage, endPage);
        model.addAttribute("posts", posts);
        if (posts.size() >= 4) {
            model.addAttribute("currentPage", (startPage / endPage) + 1);
        } else {
            model.addAttribute("currentPage", "last");
        }
        return "index";
    }

    @GetMapping(value = "/search/{searchedValue}")
    public String getSearchPage(@PathVariable(value = "searchedValue") String searchedValue, Model model) {
        List<Post> posts = postService.getAllPostsBySearchedValue(searchedValue);
        model.addAttribute("posts", posts);
        return "index";
    }

    // Filter methods
    @GetMapping(value = "/author")
    public String getAuthorFilterOnPosts(Model model) {
        User user = new User();
        user = userRepository.findById(1).get();
        List<Post> posts = postService.getPostByAuthor(user);
        model.addAttribute("posts", posts);
        return "index";
    }

    // sort methods
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
}
