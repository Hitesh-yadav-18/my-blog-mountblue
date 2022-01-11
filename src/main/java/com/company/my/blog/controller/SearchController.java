package com.company.my.blog.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.company.my.blog.dto.PostExcerptDto;
import com.company.my.blog.model.BadRequestMessage;
import com.company.my.blog.model.Post;
import com.company.my.blog.service.PostService;
import com.company.my.blog.service.TagService;
import com.company.my.blog.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @GetMapping(value = "/", params = { "start", "limit", "search" })
    public ResponseEntity<?> getSearchedPosts(
            @RequestParam("start") int start,
            @RequestParam("limit") int limit,
            @RequestParam(value = "search") String searchedValue,
            @RequestParam(value = "author", required = false, defaultValue = ""+NO_DATA+"")
                 List<Integer> authorIds,
            @RequestParam(value = "tagId", required = false, defaultValue = ""+NO_DATA+"")
                 List<Integer> tagIds,
            @RequestParam(value = "fromDate", required = false) String fromDate,
            @RequestParam(value = "toDate", required = false) String toDate) {
        List<PostExcerptDto> postsWithoutContent = null;
        if (fromDate == null || toDate == null) {
            postsWithoutContent = postService.getAllPostsBySearchedValue(searchedValue, start, limit);
        } else {
            postsWithoutContent = postService.getAllPostsBySearchedValueAndDates(
                searchedValue, start, limit, fromDate, toDate);
        }

        if(postsWithoutContent.isEmpty()== false){
            postsWithoutContent = postService.addTagstoPosts(postsWithoutContent);
            return ResponseEntity.ok(postsWithoutContent);
        } else {
            return ResponseEntity.ok(new BadRequestMessage("No posts found for the given search criteria"));
        }
    }

    @GetMapping(value = "/", params = { "start", "limit", "search", "author" })
    public ResponseEntity<?> getSearchedPostsByAuthor(
            @RequestParam("start") int start,
            @RequestParam("limit") int limit,
            @RequestParam(value = "search") String searchedValue,
            @RequestParam(value = "author") List<Integer> authorIds,
            @RequestParam(value = "tagId", required = false, defaultValue = ""+NO_DATA+"")
                 List<Integer> tagIds,
            @RequestParam(value = "fromDate", required = false) String fromDate,
            @RequestParam(value = "toDate", required = false) String toDate) {
        List<PostExcerptDto> postsWithoutContent = null;
        if (fromDate == null || toDate == null) {
            postsWithoutContent = postService.getAllPostsBySearchedValueAndAuthor(
                searchedValue, authorIds, start, limit);
        } else {
            postsWithoutContent = postService.getAllPostsBySearchedValueAndAuthorAndDates(
                searchedValue, start, limit, authorIds, fromDate, toDate);
        }

        if(postsWithoutContent.isEmpty() == false){
            postsWithoutContent = postService.addTagstoPosts(postsWithoutContent);
            return ResponseEntity.ok(postsWithoutContent);
        }else{
            return ResponseEntity.ok(new BadRequestMessage("No posts found for given searched criteria"));
        }
    }

    @GetMapping(value = "/", params = { "start", "limit", "search", "tagId" })
    public ResponseEntity<?> getSearchedPostsByTag(
            @RequestParam("start") int start,
            @RequestParam("limit") int limit,
            @RequestParam(value = "search") String searchedValue,
            @RequestParam(value = "author", required = false, defaultValue = ""+NO_DATA+"")
                List<Integer> authorIds,
            @RequestParam(value = "tagId", required = false, defaultValue = ""+NO_DATA+"") 
                List<Integer> tagIds,
            @RequestParam(value = "fromDate", required = false) String fromDate,
            @RequestParam(value = "toDate", required = false) String toDate,
            Model model) {
        List<PostExcerptDto> postsWithoutContent = null;
        if (fromDate == null || toDate == null) {
            postsWithoutContent = postService.getAllPostsBySearchedValueAndTag(
                searchedValue, tagIds, start, limit);
        } else {
            postsWithoutContent = postService.getAllPostsBySearchedValueAndTagAndDates(
                searchedValue, start, limit, tagIds, fromDate, toDate);
        }

        if(postsWithoutContent.isEmpty() == false){
            postsWithoutContent = postService.addTagstoPosts(postsWithoutContent);
            return ResponseEntity.ok(postsWithoutContent);
        }else{
            return ResponseEntity.ok(new BadRequestMessage("No posts found on applied filters."));
        }
    }

    @GetMapping(value = "/", params = { "start", "limit", "search", "author", "tagId" })
    public ResponseEntity<?> getSearchedPostsByAuthorAndTagWithoutSort(
            @RequestParam("start") int start,
            @RequestParam("limit") int limit,
            @RequestParam(value = "search") String searchedValue,
            @RequestParam(value = "author") List<Integer> authorIds,
            @RequestParam(value = "tagId", required = false, defaultValue = ""+NO_DATA+"") 
                List<Integer> tagIds,
            @RequestParam(value = "fromDate", required = false) String fromDate,
            @RequestParam(value = "toDate", required = false) String toDate,
            Model model) {
        List<PostExcerptDto> postsWithoutContent = null;
        if (fromDate == null || toDate == null) {
            postsWithoutContent = postService.getAllPostsBySearchedValueAndAuthorAndTag(
                searchedValue, authorIds, tagIds, start, limit);
        } else {
            postsWithoutContent = postService.getAllPostsBySearchedValueAndAuthorAndTagAndDates(
                searchedValue, start, limit, authorIds, tagIds, fromDate, toDate);
        }

        if(postsWithoutContent.isEmpty() == false){
            postsWithoutContent = postService.addTagstoPosts(postsWithoutContent);
            return ResponseEntity.ok(postsWithoutContent);
        }else{
            return ResponseEntity.ok(new BadRequestMessage("No posts found on applied filters"));
        }
       

       
    }

    @GetMapping(
        value = "/",
        params = { "start", "limit", "search", "author", "sortField", "order" })
    public ResponseEntity<?> getSearchedPostsByAuthorInSortedWay(
            @RequestParam("start") int start,
            @RequestParam("limit") int limit,
            @RequestParam(value = "search") String searchedValue,
            @RequestParam(value = "author") List<Integer> authorIds,
            @RequestParam(value = "tagId", required = false, defaultValue = ""+NO_DATA+"")
                 List<Integer> tagIds,
            @RequestParam(value = "fromDate", required = false) String fromDate,
            @RequestParam(value = "toDate", required = false) String toDate,
            @RequestParam(value = "sortField") String sortField,
            @RequestParam(value = "order") String order) {
        List<PostExcerptDto> postsWithoutContent = null;
        if (fromDate == null || toDate == null) {
            postsWithoutContent = postService.getAllPostsBySearchedValueAndAuthorAndSorted(
                searchedValue, authorIds, start, limit, order);
        } else {
            postsWithoutContent = postService.getAllPostsBySearchedValueAndAuthorAndDatesAndSorted(
                searchedValue, start, limit, authorIds, fromDate, toDate, order);
        }

        if(postsWithoutContent.isEmpty() == false){
            postsWithoutContent = postService.addTagstoPosts(postsWithoutContent);
            return ResponseEntity.ok(postsWithoutContent);
        }else{
            return ResponseEntity.ok(new BadRequestMessage("No posts found on applied filters"));
        }
    }

    @GetMapping(
        value = "/", 
        params = { "start", "limit", "search", "author", "sortField", "order", "tagId" })
    public ResponseEntity<?> getSearchedPostsByAuthorAndTagInSortedWay(
            @RequestParam("start") int start,
            @RequestParam("limit") int limit,
            @RequestParam(value = "search") String searchedValue,
            @RequestParam(value = "author", defaultValue = ""+NO_DATA+"") List<Integer> authorIds,
            @RequestParam(value = "tagId", defaultValue = ""+NO_DATA+"") List<Integer> tagIds,
            @RequestParam(value = "sortField", defaultValue = "publishedAt") String sortField,
            @RequestParam(value = "publishedDatesRange", required = false)
                 List<String> publishedDatesRange,
            @RequestParam(value = "order") String order,
            @RequestParam(value = "fromDate", required = false) String fromDate,
            @RequestParam(value = "toDate", required = false) String toDate) {
        List<PostExcerptDto> postsWithoutContent = null;

        if (fromDate == null || toDate == null) {
            postsWithoutContent = postService.getAllPostsBySearchedValueAndAuthorAndTagAndSorted(
                searchedValue, authorIds, tagIds, start, limit, order);
        } else {
            postsWithoutContent = postService.getAllPostsBySearchedValueAndAuthorAndTagAndDatesAndSorted(
                searchedValue, authorIds, tagIds, start, limit, fromDate, toDate, order);
        }

        if(postsWithoutContent.isEmpty() == false){
            postsWithoutContent = postService.addTagstoPosts(postsWithoutContent);
            return ResponseEntity.ok(postsWithoutContent);
        }else{
            return ResponseEntity.ok(new BadRequestMessage("No posts found on applied filters"));
        }
       
        
    }
}
