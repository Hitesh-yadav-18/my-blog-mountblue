package com.company.my.blog.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.company.my.blog.model.Post;
import com.company.my.blog.service.PostService;
import com.company.my.blog.service.TagService;
import com.company.my.blog.service.UserService;

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

    @GetMapping(value = "/", params = { "start", "limit", "search" })
    public String getSearchPage(
            @RequestParam("start") int start,
            @RequestParam("limit") int limit,
            @RequestParam(value = "search") String searchedValue,
            @RequestParam(value = "author", required = false, defaultValue = "-1") List<Integer> authorIds,
            @RequestParam(value = "tagId", required = false, defaultValue = "-1") List<Integer> tagIds,
            Model model) {
        List<Post> posts = postService.getAllPostsBySearchedValue(searchedValue, start, limit);

        Map<Post, List<String>> postTagMap = postService.getPostsWithTagsAsHashMap(posts);
        Set<Integer> authorIdsSet = new HashSet<>(authorIds);
        Set<Integer> tagIdsSet = new HashSet<>(tagIds);

        model.addAttribute("authorIdsSet", authorIdsSet);
        model.addAttribute("tagIdsSet", tagIdsSet);
        model.addAttribute("postTagMap", postTagMap);
        model.addAttribute("totalResultCount", postTagMap.size());
        model.addAttribute("authors", userService.getAllUsers());
        model.addAttribute("tags", tagService.getAllTags());
        if (posts.size() >= 10) {
            model.addAttribute("currentPage", (start / limit) + 1);
        } else {
            model.addAttribute("currentPage", "last");
        }
        return "index";
    }

    @GetMapping(value = "/", params = { "start", "limit", "search", "author" })
    public String getSearchPageByAuthor(
            @RequestParam("start") int start,
            @RequestParam("limit") int limit,
            @RequestParam(value = "search") String searchedValue,
            @RequestParam(value = "author") List<Integer> authorIds,
            @RequestParam(value = "tagId", required = false, defaultValue = "-1") List<Integer> tagIds,
            Model model) {
        List<Post> posts = postService.getAllPostsBySearchedValueAndAuthor(searchedValue, authorIds,start, limit);

        Map<Post, List<String>> postTagMap = postService.getPostsWithTagsAsHashMap(posts);
        Set<Integer> authorIdsSet = new HashSet<>(authorIds);
        Set<Integer> tagIdsSet = new HashSet<>(tagIds);

        model.addAttribute("authorIdsSet", authorIdsSet);
        model.addAttribute("tagIdsSet", tagIdsSet);
        model.addAttribute("postTagMap", postTagMap);
        model.addAttribute("totalResultCount", postTagMap.size());
        model.addAttribute("authors", userService.getAllUsers());
        model.addAttribute("tags", tagService.getAllTags());
        if (posts.size() >= 10) {
            model.addAttribute("currentPage", (start / limit) + 1);
        } else {
            model.addAttribute("currentPage", "last");
        }
        return "index";
    }

    @GetMapping(value = "/", params = { "start", "limit", "search", "author", "tagId" })
    public String getSearchPageByAuthorAndTagWithoutSort(
            @RequestParam("start") int start,
            @RequestParam("limit") int limit,
            @RequestParam(value = "search") String searchedValue,
            @RequestParam(value = "author") List<Integer> authorIds,
            @RequestParam(value = "tagId", required = false, defaultValue = "-1") List<Integer> tagIds,
            Model model) {
        List<Post> posts = postService.getAllPostsBySearchedValueAuthorTagWithoutSort(searchedValue,
                             authorIds, tagIds, start, limit);

        Set<Integer> authorIdsSet = new HashSet<>(authorIds);
        Set<Integer> tagIdsSet = new HashSet<>(tagIds);
        Map<Post, List<String>> postTagMap = postService.getPostsWithTagsAsHashMap(posts);

        model.addAttribute("authorIdsSet", authorIdsSet);
        model.addAttribute("tagIdsSet", tagIdsSet);
        model.addAttribute("postTagMap", postTagMap);
        model.addAttribute("totalResultCount", postTagMap.size());
        model.addAttribute("authors", userService.getAllUsers());
        model.addAttribute("tags", tagService.getAllTags());
        if (posts.size() >= 10) {
            model.addAttribute("currentPage", (start / limit) + 1);
        } else {
            model.addAttribute("currentPage", "last");
        }
        return "index";
    }

    @GetMapping(value = "/", params = { "start", "limit", "search", "author", "sortField", "order" })
    public String getSearchPageByAuthorInSortedWay(
            @RequestParam("start") int start,
            @RequestParam("limit") int limit,
            @RequestParam(value = "search") String searchedValue,
            @RequestParam(value = "author") List<Integer> authorIds,
            @RequestParam(value = "tagId", required = false, defaultValue = "-1") List<Integer> tagIds,
            @RequestParam(value = "sortField") String sortField,
            @RequestParam(value = "order") String order,
            Model model) {
        List<Post> posts = null;
        if (order.equals("desc")) {
            posts = postService.getAllPostsBySearchedValueAndAuthorInDesc(
                    searchedValue, authorIds,start, limit);
        } else {
            posts = postService.getAllPostsBySearchedValueAndAuthorInAsc(
                    searchedValue, authorIds,start, limit);
        }

        Set<Integer> authorIdsSet = new HashSet<>(authorIds);
        Set<Integer> tagIdsSet = new HashSet<>(tagIds);
        Map<Post, List<String>> postTagMap = postService.getPostsWithTagsAsHashMap(posts);

        model.addAttribute("authorIdsSet", authorIdsSet);
        model.addAttribute("tagIdsSet", tagIdsSet);
        model.addAttribute("postTagMap", postTagMap);
        model.addAttribute("totalResultCount", postTagMap.size());
        model.addAttribute("authors", userService.getAllUsers());
        model.addAttribute("tags", tagService.getAllTags());
        if (posts.size() >= 10) {
            model.addAttribute("currentPage", (start / limit) + 1);
        } else {
            model.addAttribute("currentPage", "last");
        }
        return "index";
    }

    @GetMapping(value = "/", params = { "start", "limit", "search", "author", "sortField", "order", "tagId" })
    public String getSearchPageByAuthorAndTagInSortedWay(
            @RequestParam("start") int start,
            @RequestParam("limit") int limit,
            @RequestParam(value = "search") String searchedValue,
            @RequestParam(value = "author", defaultValue = "-1") List<Integer> authorIds,
            @RequestParam(value = "tagId", defaultValue = "-1") List<Integer> tagIds,
            @RequestParam(value = "sortField") String sortField,
            @RequestParam(value = "order") String order,
            Model model) {
        List<Post> posts = null;
        if (order.equals("desc")) {
            posts = postService.getAllPostsBySearchedValueAuthorTagInDesc(
                    searchedValue, authorIds, tagIds, start, limit);
        } else {
            posts = postService.getAllPostsBySearchedValueAndAuthorTagInAsc(
                    searchedValue, authorIds, tagIds, start, limit);
        }

        Set<Integer> authorIdsSet = new HashSet<>(authorIds);
        Set<Integer> tagIdsSet = new HashSet<>(tagIds);
        Map<Post, List<String>> postTagMap = postService.getPostsWithTagsAsHashMap(posts);
        
        model.addAttribute("authorIdsSet", authorIdsSet);
        model.addAttribute("tagIdsSet", tagIdsSet);
        model.addAttribute("postTagMap", postTagMap);
        model.addAttribute("totalResultCount", postTagMap.size());
        model.addAttribute("authors", userService.getAllUsers());
        model.addAttribute("tags", tagService.getAllTags());
        if (posts.size() >= 10) {
            model.addAttribute("currentPage", (start / limit) + 1);
        } else {
            model.addAttribute("currentPage", "last");
        }
        return "index";
    }
}
