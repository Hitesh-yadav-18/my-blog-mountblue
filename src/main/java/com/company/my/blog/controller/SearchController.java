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

    private static final int PAGE_LIMIT = 10;
    private static final int NO_DATA = -1;

    @Autowired
    private PostService postService;

    @Autowired
    private TagService tagService;

    @Autowired
    private UserService userService;

    // @GetMapping(value = "/", params = { "start", "limit", "search" })
    // public String getSearchedPosts(
    //         @RequestParam("start") int start,
    //         @RequestParam("limit") int limit,
    //         @RequestParam(value = "search") String searchedValue,
    //         @RequestParam(value = "author", required = false, defaultValue = ""+NO_DATA+"")
    //              List<Integer> authorIds,
    //         @RequestParam(value = "tagId", required = false, defaultValue = ""+NO_DATA+"")
    //              List<Integer> tagIds,
    //         @RequestParam(value = "fromDate", required = false) String fromDate,
    //         @RequestParam(value = "toDate", required = false) String toDate,
    //         Model model) {
    //     List<Post> posts = null;
    //     if (fromDate == null || toDate == null) {
    //         posts = postService.getAllPostsBySearchedValue(searchedValue, start, limit);
    //     } else {
    //         posts = postService.getAllPostsBySearchedValueAndDates(
    //             searchedValue, start, limit, fromDate, toDate);
    //     }

    //     Map<Post, List<String>> postTagsMap = postService.getPostsAndTagsAsKeyValuePair(posts);
    //     Set<Integer> authorIdsSet = new HashSet<>(authorIds);
    //     Set<Integer> tagIdsSet = new HashSet<>(tagIds);

    //     model.addAttribute("authorIdsSet", authorIdsSet);
    //     model.addAttribute("tagIdsSet", tagIdsSet);
    //     model.addAttribute("postTagMap", postTagsMap);
    //     model.addAttribute("totalResultCount", postTagsMap.size());
    //     model.addAttribute("authors", userService.getAllUsers());
    //     model.addAttribute("tags", tagService.getAllTags());
    //     if (posts.size() >= PAGE_LIMIT) {
    //         model.addAttribute("currentPage", (start / limit) + 1);
    //     } else {
    //         model.addAttribute("currentPage", "last");
    //     }
    //     return "index";
    // }

    // @GetMapping(value = "/", params = { "start", "limit", "search", "author" })
    // public String getSearchedPostsByAuthor(
    //         @RequestParam("start") int start,
    //         @RequestParam("limit") int limit,
    //         @RequestParam(value = "search") String searchedValue,
    //         @RequestParam(value = "author") List<Integer> authorIds,
    //         @RequestParam(value = "tagId", required = false, defaultValue = ""+NO_DATA+"")
    //              List<Integer> tagIds,
    //         @RequestParam(value = "fromDate", required = false) String fromDate,
    //         @RequestParam(value = "toDate", required = false) String toDate,
    //         Model model) {
    //     List<Post> posts = null;
    //     if (fromDate == null || toDate == null) {
    //         posts = postService.getAllPostsBySearchedValueAndAuthor(
    //             searchedValue, authorIds, start, limit);
    //     } else {
    //         posts = postService.getAllPostsBySearchedValueAndAuthorAndDates(
    //             searchedValue, start, limit, authorIds, fromDate, toDate);
    //     }

    //     Map<Post, List<String>> postTagMap = postService.getPostsAndTagsAsKeyValuePair(posts);
    //     Set<Integer> authorIdsSet = new HashSet<>(authorIds);
    //     Set<Integer> tagIdsSet = new HashSet<>(tagIds);

    //     model.addAttribute("authorIdsSet", authorIdsSet);
    //     model.addAttribute("tagIdsSet", tagIdsSet);
    //     model.addAttribute("postTagMap", postTagMap);
    //     model.addAttribute("totalResultCount", postTagMap.size());
    //     model.addAttribute("authors", userService.getAllUsers());
    //     model.addAttribute("tags", tagService.getAllTags());
    //     if (posts.size() >= PAGE_LIMIT) {
    //         model.addAttribute("currentPage", (start / limit) + 1);
    //     } else {
    //         model.addAttribute("currentPage", "last");
    //     }
    //     return "index";
    // }

    // @GetMapping(value = "/", params = { "start", "limit", "search", "tagId" })
    // public String getSearchedPostsByTag(
    //         @RequestParam("start") int start,
    //         @RequestParam("limit") int limit,
    //         @RequestParam(value = "search") String searchedValue,
    //         @RequestParam(value = "author", required = false, defaultValue = ""+NO_DATA+"")
    //             List<Integer> authorIds,
    //         @RequestParam(value = "tagId", required = false, defaultValue = ""+NO_DATA+"") 
    //             List<Integer> tagIds,
    //         @RequestParam(value = "fromDate", required = false) String fromDate,
    //         @RequestParam(value = "toDate", required = false) String toDate,
    //         Model model) {
    //     List<Post> posts = null;
    //     if (fromDate == null || toDate == null) {
    //         posts = postService.getAllPostsBySearchedValueAndTag(
    //             searchedValue, tagIds, start, limit);
    //     } else {
    //         posts = postService.getAllPostsBySearchedValueAndTagAndDates(
    //             searchedValue, start, limit, tagIds, fromDate, toDate);
    //     }

    //     Map<Post, List<String>> postTagMap = postService.getPostsAndTagsAsKeyValuePair(posts);
    //     Set<Integer> authorIdsSet = new HashSet<>(authorIds);
    //     Set<Integer> tagIdsSet = new HashSet<>(tagIds);

    //     model.addAttribute("authorIdsSet", authorIdsSet);
    //     model.addAttribute("tagIdsSet", tagIdsSet);
    //     model.addAttribute("postTagMap", postTagMap);
    //     model.addAttribute("totalResultCount", postTagMap.size());
    //     model.addAttribute("authors", userService.getAllUsers());
    //     model.addAttribute("tags", tagService.getAllTags());

    //     if (posts.size() >= PAGE_LIMIT) {
    //         model.addAttribute("currentPage", (start / limit) + 1);
    //     } else {
    //         model.addAttribute("currentPage", "last");
    //     }
    //     return "index";
    // }

    // @GetMapping(value = "/", params = { "start", "limit", "search", "author", "tagId" })
    // public String getSearchedPostsByAuthorAndTagWithoutSort(
    //         @RequestParam("start") int start,
    //         @RequestParam("limit") int limit,
    //         @RequestParam(value = "search") String searchedValue,
    //         @RequestParam(value = "author") List<Integer> authorIds,
    //         @RequestParam(value = "tagId", required = false, defaultValue = ""+NO_DATA+"") 
    //             List<Integer> tagIds,
    //         @RequestParam(value = "fromDate", required = false) String fromDate,
    //         @RequestParam(value = "toDate", required = false) String toDate,
    //         Model model) {
    //     List<Post> posts = null;
    //     if (fromDate == null || toDate == null) {
    //         posts = postService.getAllPostsBySearchedValueAndAuthorAndTag(
    //             searchedValue, authorIds, tagIds, start, limit);
    //     } else {
    //         posts = postService.getAllPostsBySearchedValueAndAuthorAndTagAndDates(
    //             searchedValue, start, limit, authorIds, tagIds, fromDate, toDate);
    //     }

    //     Set<Integer> authorIdsSet = new HashSet<>(authorIds);
    //     Set<Integer> tagIdsSet = new HashSet<>(tagIds);
    //     Map<Post, List<String>> postTagsMap = postService.getPostsAndTagsAsKeyValuePair(posts);

    //     model.addAttribute("authorIdsSet", authorIdsSet);
    //     model.addAttribute("tagIdsSet", tagIdsSet);
    //     model.addAttribute("postTagMap", postTagsMap);
    //     model.addAttribute("totalResultCount", postTagsMap.size());
    //     model.addAttribute("authors", userService.getAllUsers());
    //     model.addAttribute("tags", tagService.getAllTags());
    //     if (posts.size() >= PAGE_LIMIT) {
    //         model.addAttribute("currentPage", (start / limit) + 1);
    //     } else {
    //         model.addAttribute("currentPage", "last");
    //     }
    //     return "index";
    // }

    // @GetMapping(
    //     value = "/",
    //     params = { "start", "limit", "search", "author", "sortField", "order" })
    // public String getSearchedPostsByAuthorInSortedWay(
    //         @RequestParam("start") int start,
    //         @RequestParam("limit") int limit,
    //         @RequestParam(value = "search") String searchedValue,
    //         @RequestParam(value = "author") List<Integer> authorIds,
    //         @RequestParam(value = "tagId", required = false, defaultValue = ""+NO_DATA+"")
    //              List<Integer> tagIds,
    //         @RequestParam(value = "fromDate", required = false) String fromDate,
    //         @RequestParam(value = "toDate", required = false) String toDate,
    //         @RequestParam(value = "sortField") String sortField,
    //         @RequestParam(value = "order") String order,
    //         Model model) {
    //     List<Post> posts = null;
    //     if (fromDate == null || toDate == null) {
    //         posts = postService.getAllPostsBySearchedValueAndAuthorAndSorted(
    //             searchedValue, authorIds, start, limit, order);
    //     } else {
    //         posts = postService.getAllPostsBySearchedValueAndAuthorAndDatesAndSorted(
    //             searchedValue, start, limit, authorIds, fromDate, toDate, order);
    //     }

    //     Set<Integer> authorIdsSet = new HashSet<>(authorIds);
    //     Set<Integer> tagIdsSet = new HashSet<>(tagIds);
    //     Map<Post, List<String>> postTagMap = postService.getPostsAndTagsAsKeyValuePair(posts);

    //     model.addAttribute("authorIdsSet", authorIdsSet);
    //     model.addAttribute("tagIdsSet", tagIdsSet);
    //     model.addAttribute("postTagMap", postTagMap);
    //     model.addAttribute("totalResultCount", postTagMap.size());
    //     model.addAttribute("authors", userService.getAllUsers());
    //     model.addAttribute("tags", tagService.getAllTags());
    //     if (posts.size() >= PAGE_LIMIT) {
    //         model.addAttribute("currentPage", (start / limit) + 1);
    //     } else {
    //         model.addAttribute("currentPage", "last");
    //     }
    //     return "index";
    // }

    // @GetMapping(
    //     value = "/", 
    //     params = { "start", "limit", "search", "author", "sortField", "order", "tagId" })
    // public String getSearchedPostsByAuthorAndTagInSortedWay(
    //         @RequestParam("start") int start,
    //         @RequestParam("limit") int limit,
    //         @RequestParam(value = "search") String searchedValue,
    //         @RequestParam(value = "author", defaultValue = ""+NO_DATA+"") List<Integer> authorIds,
    //         @RequestParam(value = "tagId", defaultValue = ""+NO_DATA+"") List<Integer> tagIds,
    //         @RequestParam(value = "sortField", defaultValue = "publishedAt") String sortField,
    //         @RequestParam(value = "publishedDatesRange", required = false)
    //              List<String> publishedDatesRange,
    //         @RequestParam(value = "order") String order,
    //         @RequestParam(value = "fromDate", required = false) String fromDate,
    //         @RequestParam(value = "toDate", required = false) String toDate,
    //         Model model) {
    //     List<Post> posts = null;

    //     if (fromDate == null || toDate == null) {
    //         posts = postService.getAllPostsBySearchedValueAndAuthorAndTagAndSorted(
    //             searchedValue, authorIds, tagIds, start, limit, order);
    //     } else {
    //         posts = postService.getAllPostsBySearchedValueAndAuthorAndTagAndDatesAndSorted(
    //             searchedValue, authorIds, tagIds, start, limit, fromDate, toDate, order);
    //     }

    //     Set<Integer> authorIdsSet = new HashSet<>(authorIds);
    //     Set<Integer> tagIdsSet = new HashSet<>(tagIds);
    //     Map<Post, List<String>> postTagMap = postService.getPostsAndTagsAsKeyValuePair(posts);

    //     model.addAttribute("authorIdsSet", authorIdsSet);
    //     model.addAttribute("tagIdsSet", tagIdsSet);
    //     model.addAttribute("postTagMap", postTagMap);
    //     model.addAttribute("totalResultCount", postTagMap.size());
    //     model.addAttribute("authors", userService.getAllUsers());
    //     model.addAttribute("tags", tagService.getAllTags());
    //     if (posts.size() >= PAGE_LIMIT) {
    //         model.addAttribute("currentPage", (start / limit) + 1);
    //     } else {
    //         model.addAttribute("currentPage", "last");
    //     }
    //     return "index";
    // }
}
