package com.company.my.blog.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.company.my.blog.dto.PostExcerptDto;
import com.company.my.blog.model.User;
import com.company.my.blog.service.PostService;
import com.company.my.blog.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequestMapping("/api")
public class HomeController {

    private static final int NO_DATA = -1;

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @PostMapping(value = "/signup")
    public ResponseEntity<String> getSignupDetails(@ModelAttribute User user) {
        userService.registerUser(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @GetMapping(value = "/", params = { "start", "limit" })
    public List<PostExcerptDto> getPostsByAuthorsTagsDates(
            @RequestParam("start") int startPage,
            @RequestParam("limit") int endPage,
            @RequestParam(value = "author", required = false, defaultValue = "" + NO_DATA + "") 
                List<Integer> authorIds,
            @RequestParam(value = "tagId", required = false, defaultValue = "" + NO_DATA + "") 
                List<Integer> tagIds,
            @RequestParam(value = "fromDate", required = false) String fromDate,
            @RequestParam(value = "toDate", required = false) String toDate,
            @RequestParam(value = "order", required = false, defaultValue = "" + NO_DATA + "") 
                String order,
            @SessionAttribute(value = "currentUser", required = false) User user) {
        Set<Integer> authorIdsSet = new HashSet<>(authorIds);
        Set<Integer> tagIdsSet = new HashSet<>(tagIds);
        List<PostExcerptDto> postsWithoutContent = null;

        if (fromDate == null || toDate == null) {
            if (authorIdsSet.contains(NO_DATA) && tagIdsSet.contains(NO_DATA)) {
                postsWithoutContent = postService.getAllPosts(startPage, endPage);
            } else if (!(authorIdsSet.contains(NO_DATA)) && tagIdsSet.contains(NO_DATA)) {
                postsWithoutContent = postService.getPostByAuthor(authorIds, startPage, endPage);
            } else if (authorIdsSet.contains(NO_DATA) && !(tagIdsSet.contains(NO_DATA))) {
                postsWithoutContent = postService.getAllPostsByTagId(tagIds, startPage, endPage);
            } else if (!(authorIdsSet.contains(NO_DATA)) && !(tagIdsSet.contains(NO_DATA))) {
                postsWithoutContent = postService.getAllPostsByAuthorAndTag(authorIds, tagIds, startPage, endPage);
            }
        } else {
            if (authorIdsSet.contains(NO_DATA) && tagIdsSet.contains(NO_DATA)) {
                postsWithoutContent = postService.getAllPostsBasedOnDates(
                        startPage, endPage, fromDate, toDate);
            } else if (!(authorIdsSet.contains(NO_DATA)) && tagIdsSet.contains(NO_DATA)) {
                postsWithoutContent = postService.getPostsByAuthorBasedOnDates(
                        authorIds, startPage, endPage, fromDate, toDate);
            } else if (authorIdsSet.contains(NO_DATA) && !(tagIdsSet.contains(NO_DATA))) {
                postsWithoutContent = postService.getAllPostsByTagIdBasedOnDates(
                        tagIds, startPage, endPage, fromDate, toDate);
            } else if (!(authorIdsSet.contains(NO_DATA)) && !(tagIdsSet.contains(NO_DATA))) {
                postsWithoutContent = postService.getAllPostsByAuthorAndTagBasedOnDates(
                        authorIds, tagIds, startPage, endPage, fromDate, toDate);
            }
        }

        assert postsWithoutContent != null;
        postsWithoutContent = postService.addTagstoPosts(postsWithoutContent);

        return postsWithoutContent;
    }

    @GetMapping(value = "/", params = { "start", "limit", "sortField", "order" })
    public List<PostExcerptDto> getSortedPostsAndFilterByPublishedDate(
            @RequestParam("start") int start,
            @RequestParam("limit") int limit,
            @RequestParam(value = "sortField") String sortField,
            @RequestParam("order") String order,
            @RequestParam(value = "author", required = false, defaultValue = "" + NO_DATA + "")
                List<Integer> authorIds,
            @RequestParam(value = "tagId", required = false, defaultValue = "" + NO_DATA + "") 
                List<Integer> tagIds,
            @RequestParam(value = "fromDate", required = false) String fromDate,
            @RequestParam(value = "toDate", required = false) String toDate,
            Model model) {
        Set<Integer> authorIdsSet = new HashSet<>(authorIds);
        Set<Integer> tagIdsSet = new HashSet<>(tagIds);
        List<PostExcerptDto> postsWithoutContent = null;

        if (fromDate == null || toDate == null) {
            if (authorIdsSet.contains(NO_DATA) && tagIdsSet.contains(NO_DATA)) {
                postsWithoutContent = postService.getAllPostsInSortingOrder(start, limit, order);
            } else if (authorIdsSet.contains(NO_DATA) && !(tagIdsSet.contains(NO_DATA))) {
                postsWithoutContent = postService.getAllPostsByTagIdsInSortingOrder(
                                                tagIds, start, limit, order);
            } else if (!(authorIdsSet.contains(NO_DATA)) && tagIdsSet.contains(NO_DATA)) {
                postsWithoutContent = postService.getAllPostsByAuthorIdsInSortingOrder(
                        authorIds, start, limit, order);
            } else if (!(authorIdsSet.contains(NO_DATA)) && !(tagIdsSet.contains(NO_DATA))) {
                postsWithoutContent = postService.getAllPostsByAuthorIdsAndTagIdsInSortingOrder(
                        authorIds, tagIds, start, limit, order);
            }
        } else {
            if (authorIdsSet.contains(NO_DATA) && tagIdsSet.contains(NO_DATA)) {
                postsWithoutContent = postService.getAllPostsInSortingOrderByDates(
                        start, limit, fromDate, toDate, order);
            } else if (authorIdsSet.contains(NO_DATA) && !(tagIdsSet.contains(NO_DATA))) {
                postsWithoutContent = postService.getAllPostsByTagIdsInSortingOrderByDates(
                        tagIds, start, limit, fromDate, toDate, order);
            } else if (!(authorIdsSet.contains(NO_DATA)) && tagIdsSet.contains(NO_DATA)) {
                postsWithoutContent = postService.getAllPostsByAuthorIdsInSortingOrderByDates(
                        authorIds, start, limit, fromDate,
                        toDate, order);
            } else if (!(authorIdsSet.contains(NO_DATA)) && !(tagIdsSet.contains(NO_DATA))) {
                postsWithoutContent = postService.getAllPostsByAuthorIdsAndTagIdsInSortingOrderByDates(
                        authorIds, tagIds, start, limit, fromDate, toDate, order);
            }
        }

        assert postsWithoutContent != null;
        postsWithoutContent = postService.addTagstoPosts(postsWithoutContent);

        return postsWithoutContent;
    }
}
