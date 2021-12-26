package com.company.my.blog.controller;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.company.my.blog.Service.PostService;
import com.company.my.blog.Service.TagService;
import com.company.my.blog.Service.UserService;
import com.company.my.blog.model.Post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {
    
    @Autowired
    private PostService postService;

    @Autowired
    private TagService tagService;

    @Autowired
    private UserService userService;

    
    
    @GetMapping(value = "/", params = { "start","limit","search" })
    public String getSearchPage(
            @RequestParam("start") int start,
            @RequestParam("limit") int limit,
            @RequestParam(value = "search") String searchedValue, 
            @RequestParam(value = "author", required = false, defaultValue = "-1") List<Integer> authorIds,
            @RequestParam(value="tagId", required = false, defaultValue = "-1") List<Integer> tagIds,
            Model model) {
        List<Post> posts = postService.getAllPostsBySearchedValue(searchedValue);
        Map<Post, List<String>> postTagMap = postService.getPostsWithTagsAsHashMap(posts);
        Set<Integer> authorIdsSet = new HashSet<>(authorIds);        
        Set<Integer> tagIdsSet = new HashSet<>(tagIds);  
        model.addAttribute("authorIdsSet", authorIdsSet);
        model.addAttribute("tagIdsSet", tagIdsSet);
        model.addAttribute("postTagMap", postTagMap);
        model.addAttribute("authors", userService.getAllUsers());
        model.addAttribute("tags", tagService.getAllTags());
        return "index";
    }

    @GetMapping(value = "/", params = { "start", "limit", "search", "author" })
    public String getSearchPageByAuthor(
            @RequestParam("start") int start,
            @RequestParam("limit") int limit,
            @RequestParam(value = "search") String searchedValue,
            @RequestParam(value = "author") List<Integer> authorIds,
            @RequestParam(value="tagId", required = false, defaultValue = "-1") List<Integer> tagIds,
            Model model) {
        List<Post> posts = postService.getAllPostsBySearchedValueAndAuthor(searchedValue, authorIds);
        Set<Integer> authorIdsSet = new HashSet<>(authorIds);        
        Set<Integer> tagIdsSet = new HashSet<>(tagIds);  
        Map<Post, List<String>> postTagMap = postService.getPostsWithTagsAsHashMap(posts);
        model.addAttribute("authorIdsSet", authorIdsSet);
        model.addAttribute("tagIdsSet", tagIdsSet);
        model.addAttribute("postTagMap", postTagMap);
        model.addAttribute("authors", userService.getAllUsers());
        model.addAttribute("tags", tagService.getAllTags());
        return "index";
    }

    @GetMapping(value = "/", params = { "start", "limit", "search", "author", "tagId" })
    public String getSearchPageByAuthorAndTagWithoutSort(
            @RequestParam("start") int start,
            @RequestParam("limit") int limit,
            @RequestParam(value = "search") String searchedValue,
            @RequestParam(value = "author") List<Integer> authorIds,
            @RequestParam(value="tagId", required = false, defaultValue = "-1") List<Integer> tagIds,
            Model model) {
        List<Post> posts = postService.getAllPostsBySearchedValueAuthorTagWithoutSort(searchedValue, authorIds, tagIds);
        Set<Integer> authorIdsSet = new HashSet<>(authorIds);        
        Set<Integer> tagIdsSet = new HashSet<>(tagIds);  
        Map<Post, List<String>> postTagMap = postService.getPostsWithTagsAsHashMap(posts);
        model.addAttribute("authorIdsSet", authorIdsSet);
        model.addAttribute("tagIdsSet", tagIdsSet);
        model.addAttribute("postTagMap", postTagMap);
        model.addAttribute("authors", userService.getAllUsers());
        model.addAttribute("tags", tagService.getAllTags());
        return "index";
    }

    @GetMapping(value = "/", params = { "start", "limit", "search", "author", "sortField", "order" })
    public String getSearchPageByAuthorInSortedWay(
            @RequestParam("start") int start,
            @RequestParam("limit") int limit,
            @RequestParam(value = "search") String searchedValue,
            @RequestParam(value = "author") List<Integer> authorIds,
            @RequestParam(value="tagId", required = false, defaultValue = "-1") List<Integer> tagIds,
            @RequestParam(value = "sortField") String sortField,
            @RequestParam(value = "order") String order,
            Model model) {
        List<Post> posts = null;        
        if(order.equals("desc")) {    
            posts = postService.getAllPostsBySearchedValueAndAuthorInDesc(
                                                searchedValue, authorIds);
        }else{
            posts = postService.getAllPostsBySearchedValueAndAuthorInAsc(
                                                    searchedValue, authorIds);
        }
        Set<Integer> authorIdsSet = new HashSet<>(authorIds);        
        Set<Integer> tagIdsSet = new HashSet<>(tagIds);  
        Map<Post, List<String>> postTagMap = postService.getPostsWithTagsAsHashMap(posts);
        model.addAttribute("authorIdsSet", authorIdsSet);
        model.addAttribute("tagIdsSet", tagIdsSet);
        model.addAttribute("postTagMap", postTagMap);
        model.addAttribute("authors", userService.getAllUsers());
        model.addAttribute("tags", tagService.getAllTags());
        return "index";
    }

    @GetMapping(value = "/", params = { "start", "limit", "search", "author", "sortField", "order", "tagId" })
    public String getSearchPageByAuthorAndTagInSortedWay(
            @RequestParam("start") int start,
            @RequestParam("limit") int limit,
            @RequestParam(value = "search") String searchedValue,
            @RequestParam(value= "author", defaultValue="-1") List<Integer> authorIds,
            @RequestParam(value="tagId",  defaultValue = "-1") List<Integer> tagIds, 
            @RequestParam(value = "sortField" ) String sortField,
            @RequestParam(value = "order") String order,
            Model model) {
        List<Post> posts = null;        
        if(order.equals("desc")) {    
            posts = postService.getAllPostsBySearchedValueAuthorTagInDesc(
                                                searchedValue, authorIds, tagIds);
        }else{
            posts = postService.getAllPostsBySearchedValueAndAuthorTagInAsc(
                                                    searchedValue, authorIds, tagIds);
        }
        Set<Integer> authorIdsSet = new HashSet<>(authorIds);        
        Set<Integer> tagIdsSet = new HashSet<>(tagIds);  
        Map<Post, List<String>> postTagMap = postService.getPostsWithTagsAsHashMap(posts);
        model.addAttribute("authorIdsSet", authorIdsSet);
        model.addAttribute("tagIdsSet", tagIdsSet);
        model.addAttribute("postTagMap", postTagMap);
        model.addAttribute("authors", userService.getAllUsers());
        model.addAttribute("tags", tagService.getAllTags());
        return "index";
    }
}
