package com.company.my.blog.controller;

import java.util.List;

import com.company.my.blog.dto.PostExcerptDto;
import com.company.my.blog.model.RequestMessage;
import com.company.my.blog.service.CustomMethodsService;
import com.company.my.blog.service.PostService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchController {

    private static final int NO_DATA = -1;

    @Autowired
    private PostService postService;

    @Autowired
    private CustomMethodsService customMethodsService;

    @GetMapping(value = "/", params = { "start", "limit", "search" })
    public ResponseEntity<?> getSearchedPosts(
            @RequestParam("start") String startAString,
            @RequestParam("limit") String limitAString,
            @RequestParam(value = "search") String searchedValue,
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new RequestMessage("Invalid type of start or limit,"+
                                            " must be integer also equal or greater than 0"));   
        }             
        if (customMethodsService.isValidPageNumbers(start, limit)) {
            List<PostExcerptDto> postsWithoutContent = null;
            if (fromDate == null || toDate == null) {
                postsWithoutContent = postService.getAllPostsBySearchedValue(searchedValue, start, limit);
            } else {
                postsWithoutContent = postService.getAllPostsBySearchedValueAndDates(
                        searchedValue, start, limit, fromDate, toDate);
            }

            if (postsWithoutContent.isEmpty() == false) {
                postsWithoutContent = postService.addTagstoPosts(postsWithoutContent);
                return ResponseEntity.ok(postsWithoutContent);
            } else {
                return ResponseEntity
                        .ok(new RequestMessage("No posts found for the given search criteria"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new RequestMessage("Invalid value of start or limit,"+
                                                " also should equal or greater than 0"));
        }
    }

    @GetMapping(value = "/", params = { "start", "limit", "search", "author" })
    public ResponseEntity<?> getSearchedPostsByAuthor(
            @RequestParam("start") String startAString,
            @RequestParam("limit") String limitAString,
            @RequestParam(value = "search") String searchedValue,
            @RequestParam(value = "author") List<Integer> authorIds,
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new RequestMessage("Invalid type of start or limit,"+
                                            " must be integer also equal or greater than 0"));   
        }                     
        if (customMethodsService.isValidPageNumbers(start, limit) ) {
            List<PostExcerptDto> postsWithoutContent = null;

            if (fromDate == null || toDate == null) {
                postsWithoutContent = postService.getAllPostsBySearchedValueAndAuthor(
                        searchedValue, authorIds, start, limit);
            } else {
                postsWithoutContent = postService.getAllPostsBySearchedValueAndAuthorAndDates(
                        searchedValue, start, limit, authorIds, fromDate, toDate);
            }

            if (postsWithoutContent.isEmpty() == false) {
                postsWithoutContent = postService.addTagstoPosts(postsWithoutContent);
                return ResponseEntity.ok(postsWithoutContent);
            } else {
                return ResponseEntity
                        .ok(new RequestMessage("No posts found for given searched criteria"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(new RequestMessage("Invalid value of start or limit,"+
                                                          " also should equal or greater than 0"));
        }
    }

    @GetMapping(value = "/", params = { "start", "limit", "search", "tagId" })
    public ResponseEntity<?> getSearchedPostsByTag(
            @RequestParam("start") String startAString,
            @RequestParam("limit") String limitAString,
            @RequestParam(value = "search") String searchedValue,
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new RequestMessage("Invalid type of start or limit,"+
                                    " must be integer also equal or greater than 0"));   
        }                     
        if (customMethodsService.isValidPageNumbers(start, limit)) {
            List<PostExcerptDto> postsWithoutContent = null;

            if (fromDate == null || toDate == null) {
                postsWithoutContent = postService.getAllPostsBySearchedValueAndTag(
                        searchedValue, tagIds, start, limit);
            } else {
                postsWithoutContent = postService.getAllPostsBySearchedValueAndTagAndDates(
                        searchedValue, start, limit, tagIds, fromDate, toDate);
            }

            if (postsWithoutContent.isEmpty() == false) {
                postsWithoutContent = postService.addTagstoPosts(postsWithoutContent);
                return ResponseEntity.ok(postsWithoutContent);
            } else {
                return ResponseEntity
                        .ok(new RequestMessage("No posts found on applied filters."));
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new RequestMessage("Invalid page numbers"));
        }
    }

    @GetMapping(value = "/", params = { "start", "limit", "search", "author", "tagId" })
    public ResponseEntity<?> getSearchedPostsByAuthorAndTagWithoutSort(
            @RequestParam("start") String startAString,
            @RequestParam("limit") String limitAString,
            @RequestParam(value = "search") String searchedValue,
            @RequestParam(value = "author") List<Integer> authorIds,
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new RequestMessage("Invalid type of start or limit,"+
                            " must be integer also equal or greater than 0"));   
        }                     
        if (customMethodsService.isValidPageNumbers(start, limit)) {
            List<PostExcerptDto> postsWithoutContent = null;

            if (fromDate == null || toDate == null) {
                postsWithoutContent = postService.getAllPostsBySearchedValueAndAuthorAndTag(
                        searchedValue, authorIds, tagIds, start, limit);
            } else {
                postsWithoutContent = postService.getAllPostsBySearchedValueAndAuthorAndTagAndDates(
                        searchedValue, start, limit, authorIds, tagIds, fromDate, toDate);
            }

            if (postsWithoutContent.isEmpty() == false) {
                postsWithoutContent = postService.addTagstoPosts(postsWithoutContent);
                return ResponseEntity.ok(postsWithoutContent);
            } else {
                return ResponseEntity
                        .ok(new RequestMessage("No posts found on applied filters"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new RequestMessage("Invalid value of start or limit,"+
                            " also should equal or greater than 0"));
        }

    }

    @GetMapping(value = "/", params = { "start", "limit", "search", "author", "sortField", "order" })
    public ResponseEntity<?> getSearchedPostsByAuthorInSortedWay(
            @RequestParam("start") String startAString,
            @RequestParam("limit") String limitAString,
            @RequestParam(value = "search") String searchedValue,
            @RequestParam(value = "author") List<Integer> authorIds,
            @RequestParam(value = "tagId", required = false, defaultValue = "" + NO_DATA + "") 
                List<Integer> tagIds,
            @RequestParam(value = "fromDate", required = false) String fromDate,
            @RequestParam(value = "toDate", required = false) String toDate,
            @RequestParam(value = "sortField") String sortField,
            @RequestParam(value = "order") String order) {
        int start = 0;
        int limit = 0;
        try{
            start = Integer.parseInt(startAString);
            limit = Integer.parseInt(limitAString);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new RequestMessage("Invalid type of start or limit,"+
                                        " must be integer also equal or greater than 0"));   
        }                     
        if (customMethodsService.isValidPageNumbers(start, limit)) {
            List<PostExcerptDto> postsWithoutContent = null;

            if (fromDate == null || toDate == null) {
                postsWithoutContent = postService.getAllPostsBySearchedValueAndAuthorAndSorted(
                        searchedValue, authorIds, start, limit, order);
            } else {
                postsWithoutContent = postService.getAllPostsBySearchedValueAndAuthorAndDatesAndSorted(
                        searchedValue, start, limit, authorIds, fromDate, toDate, order);
            }

            if (postsWithoutContent.isEmpty() == false) {
                postsWithoutContent = postService.addTagstoPosts(postsWithoutContent);
                return ResponseEntity.ok(postsWithoutContent);
            } else {
                return ResponseEntity
                        .ok(new RequestMessage("No posts found on applied filters"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new RequestMessage("Invalid value of start or limit,"+
                            " also should equal or greater than 0"));
        }
    }

    @GetMapping(value = "/", 
                params = { "start", "limit", "search", "author", "sortField", "order", "tagId" })
    public ResponseEntity<?> getSearchedPostsByAuthorAndTagInSortedWay(
            @RequestParam("start") String startAString,
            @RequestParam("limit") String limitAString,
            @RequestParam(value = "search") String searchedValue,
            @RequestParam(value = "author", defaultValue = "" + NO_DATA + "") 
                List<Integer> authorIds,
            @RequestParam(value = "tagId", defaultValue = "" + NO_DATA + "") 
                List<Integer> tagIds,
            @RequestParam(value = "sortField", defaultValue = "publishedAt") 
                String sortField,
            @RequestParam(value = "publishedDatesRange", required = false) 
                List<String> publishedDatesRange,
            @RequestParam(value = "order") String order,
            @RequestParam(value = "fromDate", required = false) String fromDate,
            @RequestParam(value = "toDate", required = false) String toDate) {
        int start = 0;
        int limit = 0;
        try{
            start = Integer.parseInt(startAString);
            limit = Integer.parseInt(limitAString);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new RequestMessage("Invalid type of start or limit,"+
                                            " must be integer also equal or greater than 0"));   
        }                     
        if (customMethodsService.isValidPageNumbers(start, limit)) {
            List<PostExcerptDto> postsWithoutContent = null;

            if (fromDate == null || toDate == null) {
                postsWithoutContent = postService.getAllPostsBySearchedValueAndAuthorAndTagAndSorted(
                        searchedValue, authorIds, tagIds, start, limit, order);
            } else {
                postsWithoutContent = postService.getAllPostsBySearchedValueAndAuthorAndTagAndDatesAndSorted(
                        searchedValue, authorIds, tagIds, start, limit, fromDate, toDate, order);
            }

            if (postsWithoutContent.isEmpty() == false) {
                postsWithoutContent = postService.addTagstoPosts(postsWithoutContent);
                return ResponseEntity.ok(postsWithoutContent);
            } else {
                return ResponseEntity
                        .ok(new RequestMessage("No posts found on applied filters"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new RequestMessage("Invalid value of start or limit,"+
                                " also should equal or greater than 0"));
        }
    }
}
