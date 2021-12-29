package com.company.my.blog.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.company.my.blog.model.Post;
import com.company.my.blog.model.User;
import com.company.my.blog.service.PostService;
import com.company.my.blog.service.TagService;
import com.company.my.blog.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {

    private static final int START_PAGE = 0;
    private static final int PAGE_LIMIT = 10;
    private static final int NO_DATA = -1;

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

    @GetMapping(value = "/signup")
    public String signup() {
        return "signup.html";
    }

    @PostMapping(value = "/signup")
    public String signup(@ModelAttribute User user) {

        userService.registerUser(user);
        return "redirect:/login";
    }

    @RequestMapping(value = "/logout-success")
    public String logout() {
        return "redirect:/";
    }

    @RequestMapping("")
    public String getIndexPage() {
        return "redirect:/?start="+START_PAGE+"&limit=" + PAGE_LIMIT;
    }

    @GetMapping(value = "/", params = { "start", "limit" })
    public String getPageByAuthor(
            @RequestParam("start") int startPage,
            @RequestParam("limit") int endPage,
            @RequestParam(value = "author", required = false, defaultValue = ""+NO_DATA+"")
                 List<Integer> authorId,
            @RequestParam(value = "tagId", required = false, defaultValue = ""+NO_DATA+"")
                 List<Integer> tagIds,
            @RequestParam(value = "fromDate", required = false) String fromDate,
            @RequestParam(value = "toDate", required = false) String toDate,
            @RequestParam(value = "order", required = false, defaultValue = ""+NO_DATA+"") String order,
            Model model) {
        Set<Integer> authorIdsSet = new HashSet<>(authorId);
        Set<Integer> tagIdsSet = new HashSet<>(tagIds);
        List<Post> posts = null;

        if (fromDate == null || toDate == null) {
            if (authorIdsSet.contains(NO_DATA) && tagIdsSet.contains(NO_DATA)) {
                posts = postService.getAllPosts(startPage, endPage);
            } else if (!(authorIdsSet.contains(NO_DATA)) && tagIdsSet.contains(NO_DATA)) {
                posts = postService.getPostByAuthor(authorId, startPage, endPage);
            } else if (authorIdsSet.contains(NO_DATA) && !(tagIdsSet.contains(NO_DATA))) {
                posts = postService.getAllPostsByTagId(tagIds, startPage, endPage);
            } else if (!(authorIdsSet.contains(NO_DATA)) && !(tagIdsSet.contains(NO_DATA))) {
                posts = postService.getAllPostsByAuthorAndTag(authorId, tagIds, startPage, endPage);
            }
        } else if (fromDate != null && toDate != null) {
            if (authorIdsSet.contains(NO_DATA) && tagIdsSet.contains(NO_DATA)) {
                posts = postService.getAllPostsBasedOnDates(startPage, endPage, fromDate, toDate);
            } else if (!(authorIdsSet.contains(NO_DATA)) && tagIdsSet.contains(NO_DATA)) {
                posts = postService.getPostsByAuthorBasedOnDates(
                    authorId, startPage, endPage, fromDate, toDate);
            } else if (authorIdsSet.contains(NO_DATA) && !(tagIdsSet.contains(NO_DATA))) {
                posts = postService.getAllPostsByTagIdBasedOnDates(
                    tagIds, startPage, endPage, fromDate, toDate);
            } else if (!(authorIdsSet.contains(NO_DATA)) && !(tagIdsSet.contains(NO_DATA))) {
                posts = postService.getAllPostsByAuthorAndTagBasedOnDates(
                    authorId, tagIds, startPage, endPage, fromDate, toDate);
            }
        }

        Map<Post, List<String>> postTagMap = postService.getPostsWithTagsAsHashMap(posts);

        model.addAttribute("authorIdsSet", authorIdsSet);
        model.addAttribute("tagIdsSet", tagIdsSet);
        model.addAttribute("postTagMap", postTagMap);
        model.addAttribute("totalResultCount", postTagMap.size());
        model.addAttribute("authors", userService.getAllUsers());
        if (authorIdsSet.contains(NO_DATA)) {
            model.addAttribute("tags", tagService.getAllTags());
        } else {
            model.addAttribute("tags", tagService.getAllTagsOfSelectedAuthor(authorId));
        }

        if (posts.size() >= PAGE_LIMIT) {
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
            @RequestParam(value = "author", required = false, defaultValue = ""+NO_DATA+"")
                 List<Integer> authorIds,
            @RequestParam(value = "tagId", required = false, defaultValue = ""+NO_DATA+"")
                 List<Integer> tagIds,
            @RequestParam(value = "fromDate", required = false) String fromDate,
            @RequestParam(value = "toDate", required = false) String toDate,
            Model model) {
        Set<Integer> authorIdsSet = new HashSet<>(authorIds);
        Set<Integer> tagIdsSet = new HashSet<>(tagIds);
        List<Post> posts = null;

        if (fromDate == null || toDate == null) {
            if (authorIdsSet.contains(NO_DATA) && tagIdsSet.contains(NO_DATA)) {
                posts = postService.getAllPostsInSortingOrder(start, limit, order);
            } else if (authorIdsSet.contains(NO_DATA) && !(tagIdsSet.contains(NO_DATA))) {
                posts = postService.getAllPostsByTagIdsInSortingOrder(tagIds, start, limit, order);
            } else if (!(authorIdsSet.contains(NO_DATA)) && tagIdsSet.contains(NO_DATA)) {
                posts = postService.getAllPostsByAuthorIdsInSortingOrder(
                    authorIds, start, limit, order);
            } else if (!(authorIdsSet.contains(NO_DATA)) && !(tagIdsSet.contains(NO_DATA))) {
                posts = postService.getAllPostsByAuthorIdsAndTagIdsInSortingOrder(
                    authorIds, tagIds, start, limit, order);
            }
        } else if (fromDate != null && toDate != null) {
            if (authorIdsSet.contains(NO_DATA) && tagIdsSet.contains(NO_DATA)) {
                posts = postService.getAllPostsInSortingOrderByDates(
                    start, limit, fromDate, toDate, order);
            } else if (authorIdsSet.contains(NO_DATA) && !(tagIdsSet.contains(NO_DATA))) {
                posts = postService.getAllPostsByTagIdsInSortingOrderByDates(
                    tagIds, start, limit, fromDate, toDate, order);
            } else if (!(authorIdsSet.contains(NO_DATA)) && tagIdsSet.contains(NO_DATA)) {
                posts = postService.getAllPostsByAuthorIdsInSortingOrderByDates(
                    authorIds, start, limit, fromDate,
                        toDate, order);
            } else if (!(authorIdsSet.contains(NO_DATA)) && !(tagIdsSet.contains(NO_DATA))) {
                posts = postService.getAllPostsByAuthorIdsAndTagIdsInSortingOrderByDates(
                    authorIds, tagIds, start, limit, fromDate, toDate, order);
            }
        }

        Map<Post, List<String>> postTagMap = postService.getPostsWithTagsAsHashMap(posts);

        model.addAttribute("authorIdsSet", authorIdsSet);
        model.addAttribute("tagIdsSet", tagIdsSet);
        model.addAttribute("postTagMap", postTagMap);
        model.addAttribute("totalResultCount", postTagMap.size());
        model.addAttribute("authors", userService.getAllUsers());
        if (authorIdsSet.contains(NO_DATA)) {
            model.addAttribute("tags", tagService.getAllTags());
        } else {
            model.addAttribute("tags", tagService.getAllTagsOfSelectedAuthor(authorIds));
        }
        if (posts.size() >= PAGE_LIMIT) {
            model.addAttribute("currentPage", (start / limit) + 1);
        } else {
            model.addAttribute("currentPage", "last");
        }
        return "index";
    }
}
