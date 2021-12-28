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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {

    @Autowired
    private PostService postService;

    @Autowired
    private TagService tagService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login")
    public String login() {
        return "login.html";
    }

    @RequestMapping(value = "/logout-success")
    public String logout() {
        return "redirect:/";
    }

    @RequestMapping("")
    public String getIndexPage() {
        return "redirect:/?start=0&limit=10";
    }

    @GetMapping(value = "/", params = { "start", "limit" })
    public String getPageByAuthor(
            @RequestParam("start") int startPage,
            @RequestParam("limit") int endPage,
            @RequestParam(value = "author", required = false, defaultValue = "-1") List<Integer> authorId,
            @RequestParam(value = "tagId", required = false, defaultValue = "-1") List<Integer> tagIds,
            @RequestParam(value = "fromDate", required = false) String fromDate,
            @RequestParam(value = "toDate", required = false) String toDate,
            @RequestParam(value = "order", required = false, defaultValue = "-1") String order,
            Model model) {
        Set<Integer> authorIdsSet = new HashSet<>(authorId);
        Set<Integer> tagIdsSet = new HashSet<>(tagIds);
        List<Post> posts = null;
        if(fromDate == null || toDate == null) {    
            if (authorIdsSet.contains(-1) && tagIdsSet.contains(-1)) {
                posts = postService.getAllPosts(startPage, endPage);
            } else if (!(authorIdsSet.contains(-1)) && tagIdsSet.contains(-1)) {
                posts = postService.getPostByAuthor(authorId, startPage, endPage);
            } else if (authorIdsSet.contains(-1) && !(tagIdsSet.contains(-1))) {
                posts = postService.getAllPostsByTagId(tagIds, startPage, endPage);
            } else if (!(authorIdsSet.contains(-1)) && !(tagIdsSet.contains(-1))) {
                posts = postService.getAllPostsByAuthorAndTag(authorId, tagIds, startPage, endPage);
            }
        }else if(fromDate != null && toDate != null){
            if (authorIdsSet.contains(-1) && tagIdsSet.contains(-1)) {
                posts = postService.getAllPostsBasedOnDates(startPage, endPage, fromDate, toDate);
            } else if (!(authorIdsSet.contains(-1)) && tagIdsSet.contains(-1)) {
                posts = postService.getPostsByAuthorBasedOnDates(authorId, startPage, endPage, fromDate, toDate);
            } else if (authorIdsSet.contains(-1) && !(tagIdsSet.contains(-1))) {
                posts = postService.getAllPostsByTagIdBasedOnDates(tagIds, startPage, endPage, fromDate, toDate);
            } else if (!(authorIdsSet.contains(-1)) && !(tagIdsSet.contains(-1))) {
                posts = postService.getAllPostsByAuthorAndTagBasedOnDates(authorId, tagIds, startPage, endPage, fromDate, toDate);
            }       
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
            @RequestParam(value = "author", required = false, defaultValue = "-1") List<Integer> authorIds,
            @RequestParam(value = "tagId", required = false, defaultValue = "-1") List<Integer> tagIds,
            @RequestParam(value = "fromDate", required = false) String fromDate,
            @RequestParam(value = "toDate", required = false) String toDate,
            Model model) {
        Set<Integer> authorIdsSet = new HashSet<>(authorIds);
        Set<Integer> tagIdsSet = new HashSet<>(tagIds);
        List<Post> posts = null;
        
        if(fromDate == null || toDate == null){
            if (authorIdsSet.contains(-1) && tagIdsSet.contains(-1)) {
                posts = postService.getAllPostsInSortingOrder(start, limit, order);
            } else if (authorIdsSet.contains(-1) && !(tagIdsSet.contains(-1))) {
                posts = postService.getAllPostsByTagIdsInSortingOrder(tagIds, start, limit, order);
            }else if(!(authorIdsSet.contains(-1)) && tagIdsSet.contains(-1)) {
                posts = postService.getAllPostsByAuthorIdsInSortingOrder(authorIds, start, limit, order);
            }else if(!(authorIdsSet.contains(-1)) && !(tagIdsSet.contains(-1))) {
                posts = postService.getAllPostsByAuthorIdsAndTagIdsInSortingOrder(authorIds, tagIds, start, limit, order);
            } 
        }else if(fromDate != null && toDate != null){
            if (authorIdsSet.contains(-1) && tagIdsSet.contains(-1)) {
                posts = postService.getAllPostsInSortingOrderByDates(start, limit, fromDate, toDate, order);
            } else if (authorIdsSet.contains(-1) && !(tagIdsSet.contains(-1))) {
                posts = postService.getAllPostsByTagIdsInSortingOrderByDates(tagIds, start, limit, fromDate, toDate, order);
            }else if(!(authorIdsSet.contains(-1)) && tagIdsSet.contains(-1)) {
                posts = postService.getAllPostsByAuthorIdsInSortingOrderByDates(authorIds, start, limit, fromDate, toDate, order);
            }else if(!(authorIdsSet.contains(-1)) && !(tagIdsSet.contains(-1))) {
                posts = postService.getAllPostsByAuthorIdsAndTagIdsInSortingOrderByDates(authorIds, tagIds, start, limit, fromDate, toDate, order);
            } 
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
            model.addAttribute("tags", tagService.getAllTagsOfSelectedAuthor(authorIds));
        }
        if (posts.size() >= 10) {
            model.addAttribute("currentPage", (start / limit) + 1);
        } else {
            model.addAttribute("currentPage", "last");
        }
        return "index";
    }
}
