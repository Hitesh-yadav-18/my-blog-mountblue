package com.company.my.blog.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.company.my.blog.dto.PostDto;
import com.company.my.blog.dto.PostExcerptDto;
import com.company.my.blog.dto.TagDto;
import com.company.my.blog.model.Post;
import com.company.my.blog.model.User;
import com.company.my.blog.repository.PostRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TagService tagService;


    public Post createNewPost(String title, String excerpt, String content, User user) {
        Post post = new Post();
        post.setTitle(title);
        post.setExcerpt(excerpt);
        post.setContent(content);
        post.setAuthor(user);
        post.setPublished(true);
        post.setPublishedAt(new Date());
        post.setCreatedAt(new Date());
        post.setUpdatedAt(new Date());

        return postRepository.save(post);
    }

    public List<PostExcerptDto> addTagstoPosts(List<PostExcerptDto> postsWithoutContent) {
        List<TagDto> tagList = null;
        for (PostExcerptDto post : postsWithoutContent) {
            tagList = tagService.getTagsName(post);
            post.setTags(tagList);
        }
        return postsWithoutContent;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }
    public PostDto getParticularPost(int postId) {
        return postRepository.findPostById(postId);
    }

    public Post getParticularFullPost(Integer postId) {
        return postRepository.findFullPostById(postId);
    }

    public void deletePost(Integer id) {
        postRepository.deleteById(id);
    }

    public void updatePostById(
            Post post) {
        postRepository.save(post);
    }

    public void updatePostWithAuthorById(Post post) {
        postRepository.save(post);
    }

    public List<PostExcerptDto> getAllPosts(int startPage, int endPage) {
        Pageable pageable = PageRequest.of(startPage / endPage, endPage);
        return postRepository.findAllPostsByPage(pageable);
    }

    public List<PostExcerptDto> getPostByAuthor(List<Integer> authorIds, Integer start, Integer limit) {
        Pageable pageable = PageRequest.of(start / limit, limit);
        return postRepository.findByAuthorId(authorIds, pageable);
    }

    public List<PostExcerptDto> getAllPostsByTagId(List<Integer> tagIds, int startPage, int endPage) {
        Pageable pageable = PageRequest.of(startPage / endPage, endPage,
                Sort.by("publishedAt").descending());
        return postRepository.findAllByTagId(tagIds, pageable);
    }

    public List<PostExcerptDto> getAllPostsByAuthorAndTag(
        List<Integer> authorIds, List<Integer> tagIds, int startPage, int endPage) {
        Pageable pageable = PageRequest.of(startPage / endPage, endPage);
        return postRepository.findAllPostsByAuthorAndTag(authorIds, tagIds, pageable);
    }

    public List<PostExcerptDto> getAllPostsBasedOnDates(
        int startPage, int endPage, String fromDate, String toDate) {
        Pageable pageable = PageRequest.of(startPage / endPage, endPage);
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
            endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return postRepository.findAllPostsByDates(pageable, startDate, endDate);
    }

    public List<PostExcerptDto> getPostsByAuthorBasedOnDates(
        List<Integer> authorId, int startPage, int endPage, String fromDate, String toDate) {
        Pageable pageable = PageRequest.of(startPage / endPage, endPage);
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
            endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return postRepository.findPostsByAuthorAndDates(authorId, pageable, startDate, endDate);
    }

    public List<PostExcerptDto> getAllPostsByTagIdBasedOnDates(
        List<Integer> tagIds, int startPage, int endPage, String fromDate, String toDate) {
        Pageable pageable = PageRequest.of(startPage / endPage, endPage);
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
            endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return postRepository.findAllByTagIdAndDates(tagIds, pageable, startDate, endDate);
    }

    public List<PostExcerptDto> getAllPostsByAuthorAndTagBasedOnDates(
        List<Integer> authorIds, 
        List<Integer> tagIds, 
        int startPage,
        int endPage, 
        String fromDate, 
        String toDate) {
        Pageable pageable = PageRequest.of(startPage / endPage, endPage);
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
            endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return postRepository.findAllPostsByAuthorAndTagAndDates(
            authorIds, tagIds, pageable, startDate, endDate);
    }

    public List<PostExcerptDto> getAllPostsInSortingOrder(int start, int limit, String order) {
        Pageable pageable = null;
        if (order.equals("desc")) {
            pageable = PageRequest.of(start / limit, limit, Sort.by("publishedAt").descending());
        } else if (order.equals("asc")) {
            pageable = PageRequest.of(start / limit, limit, Sort.by("publishedAt").ascending());
        }
        return postRepository.findAllPostsInSortingOrder(pageable);
    }

    public List<PostExcerptDto> getAllPostsByTagIdsInSortingOrder(
        List<Integer> tagIds, int start, int limit, String order) {
        Pageable pageable = null;
        if (order.equals("desc")) {
            pageable = PageRequest.of(start / limit, limit, Sort.by("publishedAt").descending());
        } else if (order.equals("asc")) {
            pageable = PageRequest.of(start / limit, limit, Sort.by("publishedAt").ascending());
        }
        return postRepository.findAllPostsByTagIdsInSortingOrder(tagIds, pageable);
    }

    public List<PostExcerptDto> getAllPostsByAuthorIdsInSortingOrder(
        List<Integer> authorIds, int start, int limit, String order) {
        Pageable pageable = null;
        if (order.equals("desc")) {
            pageable = PageRequest.of(start / limit, limit, Sort.by("publishedAt").descending());
        } else if (order.equals("asc")) {
            pageable = PageRequest.of(start / limit, limit, Sort.by("publishedAt").ascending());
        }
        return postRepository.findAllPostsByAuthorIdsInSortingOrder(authorIds, pageable);
    }

    public List<PostExcerptDto> getAllPostsByAuthorIdsAndTagIdsInSortingOrder(
        List<Integer> authorIds, List<Integer> tagIds, int start, int limit, String order) {
        Pageable pageable = null;
        if (order.equals("desc")) {
            pageable = PageRequest.of(start / limit, limit, Sort.by("publishedAt").descending());
        } else if (order.equals("asc")) {
            pageable = PageRequest.of(start / limit, limit, Sort.by("publishedAt").ascending());
        }
        return postRepository.findAllPostsByAuthorIdsAndTagIdsInSortingOrder(authorIds, tagIds, pageable);
    }

    public List<PostExcerptDto> getAllPostsInSortingOrderByDates(
        int start, int limit, String fromDate, String toDate,String order) {
        Pageable pageable = null;
        Date startDate = null;
        Date endDate = null;
 
        if (order.equals("desc")) {
            pageable = PageRequest.of(start / limit, limit, Sort.by("publishedAt").descending());
            try {
                startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
                endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if (order.equals("asc")) {
            pageable = PageRequest.of(start / limit, limit, Sort.by("publishedAt").ascending());
            try {
                startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
                endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return postRepository.findAllPostsByDates(pageable, startDate, endDate);
    }

    public List<PostExcerptDto> getAllPostsByTagIdsInSortingOrderByDates(
        List<Integer> tagIds, int start, int limit, String fromDate, String toDate, String order) {
        Pageable pageable = null;
        Date startDate = null;
        Date endDate = null;

        if (order.equals("desc")) {
            pageable = PageRequest.of(start / limit, limit, Sort.by("publishedAt").descending());
            try {
                startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
                endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if (order.equals("asc")) {
            pageable = PageRequest.of(start / limit, limit, Sort.by("publishedAt").ascending());
            try {
                startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
                endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return postRepository.findAllByTagIdAndDates(tagIds, pageable, startDate, endDate);
    }

    public List<PostExcerptDto> getAllPostsByAuthorIdsInSortingOrderByDates(
        List<Integer> authorIds, int start, int limit, String fromDate, String toDate, String order) {
        Pageable pageable = null;
        Date startDate = null;
        Date endDate = null;

        if (order.equals("desc")) {
            pageable = PageRequest.of(start / limit, limit, Sort.by("publishedAt").descending());
            try {
                startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
                endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if (order.equals("asc")) {
            pageable = PageRequest.of(start / limit, limit, Sort.by("publishedAt").ascending());
            try {
                startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
                endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return postRepository.findPostsByAuthorAndDates(authorIds, pageable, startDate, endDate);
    }

    public List<PostExcerptDto> getAllPostsByAuthorIdsAndTagIdsInSortingOrderByDates(List<Integer> authorIds,
            List<Integer> tagIds, int start, int limit, String fromDate, String toDate, String order) {
        Pageable pageable = null;
        Date startDate = null;
        Date endDate = null;

        if (order.equals("desc")) {
            pageable = PageRequest.of(start / limit, limit, Sort.by("publishedAt").descending());
            try {
                startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
                endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if (order.equals("asc")) {
            pageable = PageRequest.of(start / limit, limit, Sort.by("publishedAt").ascending());
            try {
                startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
                endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return postRepository.findAllPostsByAuthorAndTagAndDates(
            authorIds, tagIds, pageable, startDate, endDate);
    }

    public List<PostExcerptDto> getAllPostsBySearchedValue(String searchedValue, int start, int limit) {
        Pageable pageable = PageRequest.of(start / limit, limit);
        return postRepository.findAllPostsBySearchedValue(searchedValue, pageable);
    }

    public List<PostExcerptDto> getAllPostsBySearchedValueAndAuthor(
        String searchedValue, List<Integer> authorIds, int start, int limit) {
        Pageable pageable = PageRequest.of(start / limit, limit);
        return postRepository.findAllPostsBySearchedValueAndAuthor(searchedValue, authorIds, pageable);
    }

    public List<PostExcerptDto> getAllPostsBySearchedValueAndTag(
        String searchedValue, List<Integer> tagIds, int start, int limit) {
        Pageable pageable = PageRequest.of(start / limit, limit);
        return postRepository.findAllPostsBySearchedValueAndTag(searchedValue, tagIds, pageable);
    }

    public List<PostExcerptDto> getAllPostsBySearchedValueAndAuthorAndTag(
        String searchedValue, List<Integer> authorIds, List<Integer> tagIds, int start, int limit) {
        Pageable pageable = PageRequest.of(start / limit, limit);
        return postRepository.findAllPostsBySearchedValueAndAuthorAndTag(
                searchedValue, authorIds, tagIds, pageable);
    }

    public List<PostExcerptDto> getAllPostsBySearchedValueAndAuthorAndSorted(
            String searchedValue, List<Integer> authorIds, int start, int limit, String order) {
        Pageable pageable = null;
        if (order.equals("desc")) {
            pageable = PageRequest.of(start / limit, limit, Sort.by("publishedAt").descending());
        } else if (order.equals("asc")) {
            pageable = PageRequest.of(start / limit, limit, Sort.by("publishedAt").ascending());
        }
       
        return postRepository.findAllPostsBySearchedValueAndAuthorAndSorted(
                searchedValue, authorIds, pageable);
    }

    public List<PostExcerptDto> getAllPostsBySearchedValueAndAuthorAndTagAndSorted(
            String searchedValue, 
            List<Integer> authorIds,
            List<Integer> tagIds, 
            int start, 
            int limit, 
            String order) {
        Pageable pageable = null;
        if (order.equals("desc")) {
            pageable = PageRequest.of(start / limit, limit, Sort.by("publishedAt").descending());
        } else if (order.equals("asc")) {
            pageable = PageRequest.of(start / limit, limit, Sort.by("publishedAt").ascending());
        }

        return postRepository.findAllPostsBySearchedValueAndAuthorAndTagAndSorted(
                searchedValue, authorIds, tagIds, pageable);
    }

    public List<PostExcerptDto> getAllPostsBySearchedValueAndDates(
        String searchedValue, int start, int limit, String fromDate, String toDate) {

        Date startDate = null;
        Date endDate = null;
        Pageable pageable = PageRequest.of(start / limit, limit, Sort.by("publishedAt").descending());
        try {
            startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
            endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return postRepository.findAllPostsBySearchedValueAndDates(
            searchedValue, pageable, startDate, endDate);
    }

    public List<PostExcerptDto> getAllPostsBySearchedValueAndAuthorAndDates(
            String searchedValue, 
            int start, 
            int limit,
            List<Integer> authorIds, 
            String fromDate, 
            String toDate) {
        Date startDate = null;
        Date endDate = null;
        Pageable pageable = PageRequest.of(start / limit, limit, Sort.by("publishedAt").descending());
        try {
            startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
            endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return postRepository.findAllPostsBySearchedValueAndAuthorAndDates(
            searchedValue, authorIds, pageable, startDate, endDate);
    }

    public List<PostExcerptDto> getAllPostsBySearchedValueAndTagAndDates(
            String searchedValue, 
            int start, 
            int limit,
            List<Integer> tagIds, 
            String fromDate, 
            String toDate) {
        Date startDate = null;
        Date endDate = null;
        Pageable pageable = PageRequest.of(start / limit, limit, Sort.by("publishedAt").descending());
        try {
            startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
            endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return postRepository.findAllPostsBySearchedValueAndTagAndDates(
            searchedValue, tagIds, pageable, startDate, endDate);
    }

    public List<PostExcerptDto> getAllPostsBySearchedValueAndAuthorAndTagAndDates(
            String searchedValue, 
            int start,
            int limit,
            List<Integer> authorIds, 
            List<Integer> tagIds, 
            String fromDate, 
            String toDate) {
        Date startDate = null;
        Date endDate = null;
        Pageable pageable = PageRequest.of(start / limit, limit, Sort.by("publishedAt").descending());
        try {
            startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
            endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return postRepository.findAllPostsBySearchedValueAndAuthorAndTagAndDates(
            searchedValue, authorIds, tagIds, pageable, startDate, endDate);
    }

    public List<PostExcerptDto> getAllPostsBySearchedValueAndAuthorAndDatesAndSorted(
            String searchedValue, 
            int start, 
            int limit,
            List<Integer> authorIds, 
            String fromDate, 
            String toDate, 
            String order) {
        Date startDate = null;
        Date endDate = null;
        Pageable pageable = null;

        if (order.equals("desc")) {
            pageable = PageRequest.of(start / limit, limit, Sort.by("publishedAt").descending());
        } else if (order.equals("asc")) {
            pageable = PageRequest.of(start / limit, limit, Sort.by("publishedAt").ascending());
        }

        try {
            startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
            endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return postRepository.findAllPostsBySearchedValueAndAuthorAndDatesAndSorted(
            searchedValue, authorIds, pageable,startDate, endDate);
    }

    public List<PostExcerptDto> getAllPostsBySearchedValueAndAuthorAndTagAndDatesAndSorted(
            String searchedValue,
            List<Integer> authorIds, 
            List<Integer> tagIds, 
            int start, 
            int limit, 
            String fromDate, 
            String toDate,
            String order) {
        Date startDate = null;
        Date endDate = null;
        Pageable pageable = null;

        if (order.equals("desc")) {
            pageable = PageRequest.of(start / limit, limit, Sort.by("publishedAt").descending());
        } else if (order.equals("asc")) {
            pageable = PageRequest.of(start / limit, limit, Sort.by("publishedAt").ascending());
        }

        try {
            startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
            endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return postRepository.findAllPostsBySearchedValueAndAuthorAndTagAndDatesAndSorted(
                searchedValue, authorIds, tagIds, pageable, startDate, endDate);
    }

      
   
}
