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
public class IndexController {

    @Autowired
    private PostService postService;

    @Autowired
    private TagService tagService;

    @Autowired
    private UserService userService;

    @GetMapping(value = "/login")
    public String login() {
        return "login";
    }

    @GetMapping("")
    public String getIndexPage() {
        return "redirect:/?start=0&limit=10";
    }

    @GetMapping(value = "/", params = { "start", "limit" })
    public String getPageByAuthor(
            @RequestParam("start") int startPage,
            @RequestParam("limit") int endPage,
            @RequestParam(value = "author", required = false, defaultValue = "-1") List<Integer> authorId,
            @RequestParam(value = "tagId", required = false, defaultValue = "-1") List<Integer> tagIds,
            @RequestParam(value = "sortField", required = false, defaultValue = "-1") String sortField,
            @RequestParam(value = "order", required = false, defaultValue = "-1") String order,
            Model model) {
        Set<Integer> authorIdsSet = new HashSet<>(authorId);
        Set<Integer> tagIdsSet = new HashSet<>(tagIds);
        List<Post> posts = null;

        if (authorIdsSet.contains(-1) && tagIdsSet.contains(-1)) {
            posts = postService.getAllPostsByPage(startPage, endPage);
        } else if (!(authorIdsSet.contains(-1)) && tagIdsSet.contains(-1)) {
            posts = postService.getPostByAuthor(authorId, startPage, endPage);
        } else if (authorIdsSet.contains(-1) && !(tagIdsSet.contains(-1))) {
            posts = postService.getAllPostsByTagId(tagIds, startPage, endPage);
        } else if (!(authorIdsSet.contains(-1)) && !(tagIdsSet.contains(-1))) {
            posts = postService.getAllPostsByAuthorAndTag(authorId, tagIds, startPage, endPage);
        }
        
        Map<Post, List<String>> postTagMap = postService.getPostsWithTagsAsHashMap(posts);

        model.addAttribute("authorIdsSet", authorIdsSet);
        model.addAttribute("tagIdsSet", tagIdsSet);
        model.addAttribute("postTagMap", postTagMap);
        model.addAttribute("totalResultCount", postTagMap.size());
        model.addAttribute("authors", userService.getAllUsers());
        if(authorIdsSet.contains(-1)) {
            model.addAttribute("tags", tagService.getAllTags());
        } else {
            model.addAttribute("tags", tagService.getAllTagsOfSelectedAuthor(authorId));
        }
       
        if (posts.size() >= 10) {
            model.addAttribute("currentPage", (startPage / endPage) + 1);
        } else {
            model.addAttribute("currentPage", "last");
        }
        return "index";
    }

    @GetMapping(value = "/", params = { "start", "limit", "sortField", "order" })
    public String getSortedPostsByPublishedDate(
            @RequestParam("start") int start,
            @RequestParam("limit") int limit,
            @RequestParam(value = "sortField") String sortField,
            @RequestParam("order") String order,
            @RequestParam(value = "author", required = false, defaultValue = "-1") List<Integer> authorId,
            @RequestParam(value = "tagId", required = false, defaultValue = "-1") List<Integer> tagIds,
            Model model) {
        Set<Integer> authorIdsSet = new HashSet<>(authorId);
        Set<Integer> tagIdsSet = new HashSet<>(tagIds);
        List<Post> posts = null;

        if (order.equals("desc") && authorIdsSet.contains(-1) && tagIdsSet.contains(-1)) {
            posts = postService.getAllPostsByPublishedDateDesc(start, limit);
        } else if (order.equals("asc") && authorIdsSet.contains(-1) && tagIdsSet.contains(-1)) {
            posts = postService.getAllPostsByPublishedDateAsc(start, limit);
        }else if (order.equals("desc") && authorIdsSet.contains(-1) && !(tagIdsSet.contains(-1))) {
            posts = postService.getAllPostsByTagIdsPublishedDateDesc(tagIds, start, limit);
        }else if(order.equals("asc") && authorIdsSet.contains(-1) && !(tagIdsSet.contains(-1))) {
            posts = postService.getAllPostsByTagIdsPublishedDateAsc(tagIds, start, limit);
        }else if(order.equals("desc") && !(authorIdsSet.contains(-1)) && tagIdsSet.contains(-1)) {
            posts = postService.getAllPostsByAuthorPublishedDateDesc(authorId, start, limit);
        }else if(order.equals("asc") && !(authorIdsSet.contains(-1)) && tagIdsSet.contains(-1)) {
            posts = postService.getAllPostsByAuthorPublishedDateAsc(authorId, start, limit);
        }else if(order.equals("desc") && !(authorIdsSet.contains(-1)) && !(tagIdsSet.contains(-1))) {
            posts = postService.getAllPostsByAuthorAndTagPublishedDateDesc(authorId, tagIds, start, limit);
        }else if(order.equals("asc") && !(authorIdsSet.contains(-1)) && !(tagIdsSet.contains(-1))) {
            posts = postService.getAllPostsByAuthorAndTagPublishedDateAsc(authorId, tagIds, start, limit);
        }           

        Map<Post, List<String>> postTagMap = postService.getPostsWithTagsAsHashMap(posts);
        
        model.addAttribute("authorIdsSet", authorIdsSet);
        model.addAttribute("tagIdsSet", tagIdsSet);
        model.addAttribute("postTagMap", postTagMap);
        model.addAttribute("totalResultCount", postTagMap.size());
        model.addAttribute("authors", userService.getAllUsers());
        if(authorIdsSet.contains(-1)) {
            model.addAttribute("tags", tagService.getAllTags());
        } else {
            model.addAttribute("tags", tagService.getAllTagsOfSelectedAuthor(authorId));
        }
        if (posts.size() >= 10) {
            model.addAttribute("currentPage", (start / limit) + 1);
        } else {
            model.addAttribute("currentPage", "last");
        }
        return "index";
    }
}
