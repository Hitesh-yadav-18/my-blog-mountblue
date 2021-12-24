package com.company.my.blog.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.Order;

import com.company.my.blog.Service.PostService;
import com.company.my.blog.Service.TagService;
import com.company.my.blog.model.Post;
import com.company.my.blog.model.Tag;
import com.company.my.blog.model.User;
import com.company.my.blog.repository.TagRepository;
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

    @Autowired
    private  TagService tagService;

    @GetMapping("")
    public String getIndexPage() {
        return "redirect:/?start=0&limit=4";
    }

    public HashMap<Post, List<String>> getPostsAsHashMap(List <Post> posts){
        List<String> tagList = null;
        HashMap <Post, List<String> > postTagMap = new HashMap<>(); 
        for(Post post : posts){
             tagList = tagService.getTagsName(post);
             postTagMap.put(post, tagList);
        }
       return postTagMap;
    }

    @GetMapping(value = "/", params = { "start", "limit" })
    public String getPage(@RequestParam("start") int startPage,
            @RequestParam("limit") int endPage, Model model) {
        List<Post> posts = postService.getAllPostsByPage(startPage, endPage);
        HashMap <Post, List<String>> postTagMap = getPostsAsHashMap(posts);
        model.addAttribute("postTagMap", postTagMap);
        if (posts.size() >= 4) {
            model.addAttribute("currentPage", (startPage / endPage) + 1);
        } else {
            model.addAttribute("currentPage", "last");
        }
        return "index";
    }
    //?authorId="1"&tagId="1"&tagId="2"

    @GetMapping(value = "/", params ={"search"})
    public String getSearchPage(@RequestParam(value = "search") String searchedValue, Model model) {
        List<Post> posts = postService.getAllPostsBySearchedValue(searchedValue);
        HashMap <Post, List<String>> postTagMap = getPostsAsHashMap(posts);
        model.addAttribute("postTagMap", postTagMap);
        return "index";
    }

    @GetMapping(value = "/author")
    public String getAuthorFilterOnPosts(Model model) {
        User user = new User();
        user = userRepository.findById(2).get();
        List<Post> posts = postService.getPostByAuthor(user);
        HashMap <Post, List<String>> postTagMap = getPostsAsHashMap(posts);
        model.addAttribute("postTagMap", postTagMap);
        return "index";
    }

    @GetMapping(value = "/", params = {"start","limit","sortField","order"})
    public String getSortPostsByPublishedDateDesc(
                @RequestParam("start") int start,
                @RequestParam("limit") int limit,
                @RequestParam(value = "sortField") String sortField,
                @RequestParam("order") String order, Model model) {
        System.out.println("reached" + order);
        List <Post> posts = null;
        if(order.equals("desc")){
            posts = postService.getAllPostsByPublishedDateDesc(start,limit);
        }else if(order.equals("asc")){
            posts=postService.getAllPostsByPublishedDateAsc(start, limit);
        }
        HashMap <Post, List<String>> postTagMap = getPostsAsHashMap(posts);
        model.addAttribute("postTagMap", postTagMap);
        return "index";
    }
}
