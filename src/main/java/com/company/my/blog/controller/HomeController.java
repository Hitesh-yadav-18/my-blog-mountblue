package com.company.my.blog.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.company.my.blog.dto.PostExcerptDto;
import com.company.my.blog.model.User;
import com.company.my.blog.service.CustomMethodsService;
import com.company.my.blog.service.PostService;
import com.company.my.blog.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HomeController {

    private static final int NO_DATA = -1;

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Autowired
    private CustomMethodsService customMethodsService;

    @PostMapping(value = "/signup")
    public ResponseEntity<String> getSignupDetails(@ModelAttribute User user) {
        userService.registerUser(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @GetMapping(value = "/", params = { "start", "limit" })
    public ResponseEntity<?> getPostsByAuthorsTagsDates(
            @RequestParam("start") String startAString,
            @RequestParam("limit") String limitAString,
            @RequestParam(value = "author", required = false, defaultValue = "" + NO_DATA + "") 
                List<Integer> authorIds,
            @RequestParam(value = "tagId", required = false, defaultValue = "" + NO_DATA + "") 
                List<Integer> tagIds,
            @RequestParam(value = "fromDate", required = false) String fromDate,
            @RequestParam(value = "toDate", required = false) String toDate,
            @RequestParam(value = "order", required = false, defaultValue = "" + NO_DATA + "") 
                String order)  {
        int start = 0;
        int limit = 0;
        try{
            start = Integer.parseInt(startAString);
            limit = Integer.parseInt(limitAString);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid start or limit..Enter numbers only");   
        }     
        if (customMethodsService.isValidPageNumbers(start, limit)) {
            Set<Integer> authorIdsSet = new HashSet<>(authorIds);
            Set<Integer> tagIdsSet = new HashSet<>(tagIds);
            List<PostExcerptDto> postsWithoutContent = null;

            if (fromDate == null || toDate == null) {
                if (authorIdsSet.contains(NO_DATA) && tagIdsSet.contains(NO_DATA)) {
                    postsWithoutContent = postService.getAllPosts(start, limit);
                } else if (!(authorIdsSet.contains(NO_DATA)) && tagIdsSet.contains(NO_DATA)) {
                    postsWithoutContent = postService.getPostByAuthor(authorIds, start, limit);
                } else if (authorIdsSet.contains(NO_DATA) && !(tagIdsSet.contains(NO_DATA))) {
                    postsWithoutContent = postService.getAllPostsByTagId(tagIds, start, limit);
                } else if (!(authorIdsSet.contains(NO_DATA)) && !(tagIdsSet.contains(NO_DATA))) {
                    postsWithoutContent = postService
                                .getAllPostsByAuthorAndTag(authorIds, tagIds, start, limit);
                }
            } else {
                if (authorIdsSet.contains(NO_DATA) && tagIdsSet.contains(NO_DATA)) {
                    postsWithoutContent = postService.getAllPostsBasedOnDates(
                            start, limit, fromDate, toDate);
                } else if (!(authorIdsSet.contains(NO_DATA)) && tagIdsSet.contains(NO_DATA)) {
                    postsWithoutContent = postService.getPostsByAuthorBasedOnDates(
                            authorIds, start, limit, fromDate, toDate);
                } else if (authorIdsSet.contains(NO_DATA) && !(tagIdsSet.contains(NO_DATA))) {
                    postsWithoutContent = postService.getAllPostsByTagIdBasedOnDates(
                            tagIds, start, limit, fromDate, toDate);
                } else if (!(authorIdsSet.contains(NO_DATA)) && !(tagIdsSet.contains(NO_DATA))) {
                    postsWithoutContent = postService.getAllPostsByAuthorAndTagBasedOnDates(
                            authorIds, tagIds, start, limit, fromDate, toDate);
                }
            }

            assert postsWithoutContent != null;
            postsWithoutContent = postService.addTagstoPosts(postsWithoutContent);

            return ResponseEntity.ok(postsWithoutContent);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid page numbers");
        }

    }

    @GetMapping(value = "/", params = { "start", "limit", "sortField", "order" })
    public ResponseEntity<?> getSortedPostsAndFilterByPublishedDate(
            @RequestParam("start") String startAString,
            @RequestParam("limit") String limitAString,
            @RequestParam(value = "sortField") String sortField,
            @RequestParam("order") String order,
            @RequestParam(value = "author", required = false, defaultValue = "" + NO_DATA + "") 
                List<Integer> authorIds,
            @RequestParam(value = "tagId", required = false, defaultValue = "" + NO_DATA + "") 
                List<Integer> tagIds,
            @RequestParam(value = "fromDate", required = false) String fromDate,
            @RequestParam(value = "toDate", required = false) String toDate) {
        int start = 0;
        int limit = 0;
        try{
            start = Integer.parseInt(startAString);
            limit = Integer.parseInt(limitAString);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid start or limit..Enter numbers only");   
        }             
        if (customMethodsService.isValidPageNumbers(start, limit)) {
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

            return ResponseEntity.ok(postsWithoutContent);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid page numbers");
        }
    }
}
