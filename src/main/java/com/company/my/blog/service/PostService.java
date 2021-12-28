package com.company.my.blog.service;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.company.my.blog.model.Post;
import com.company.my.blog.model.User;
import com.company.my.blog.repository.PostRepository;
import com.company.my.blog.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagService tagService;

    public Post createNewPost(String title, String excerpt, String content) {
        Post post = new Post();
        post.setTitle(title);
        post.setExcerpt(excerpt);
        post.setContent(content);
        User user = userRepository.findById(1).get();
        post.setAuthor(user);
        post.setPublished(true);
        post.setPublishedAt(new Date());
        post.setCreatedAt(new Date());
        post.setUpdatedAt(new Date());

        return postRepository.save(post);
    }

    public Map<Post, List<String>> getPostsWithTagsAsHashMap(List<Post> posts) {
        List<String> tagList = null;
        Map<Post, List<String>> postTagMap = new LinkedHashMap<>();
        for (Post post : posts) {
            tagList = tagService.getTagsName(post);
            postTagMap.put(post, tagList);
        }
        return postTagMap;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post getParticularPost(int id) {
        return postRepository.findPostById(id);
    }

    public void deletePost(Integer id) {
        postRepository.deleteById(id);
    }

    public void updatePostById(
            int id,
            String title,
            String excerpt,
            String content,
            Date updatedAt) {
        postRepository.updatePostByPostId(id, title, excerpt, content, updatedAt);
    }

    public List<Post> getPostByAuthor(List<Integer> authorIds, Integer start, Integer limit) {
        Pageable pageable = PageRequest.of(start/limit, limit);
        return postRepository.findByAuthorId(authorIds, pageable);
    }

    public List<Post> getAllPostsByTagId(List<Integer> tagIds, int startPage, int endPage) {
        Pageable pageable = PageRequest.of(startPage / endPage, endPage,
                 Sort.by("publishedAt").descending());
        return postRepository.findAllByTagId(tagIds, pageable);
    }

    public List<Post> getPostByAuthorSelectedPublishedDate(List<Integer> authorId, int startPage, int endPage,
            Date sortField, String order) {
        Pageable pageable = PageRequest.of(startPage / endPage, endPage);
        return postRepository.findByAuthorIdAndPublished(authorId, sortField, pageable);
    }

    public List<Post> getAllPostsByPublishedDateDesc(int start, int limit) {
        Pageable pageable = PageRequest.of(start/limit, limit, Sort.by("publishedAt").descending());
        return postRepository.findPostOrderByPublisedAtDesc(pageable);
    }

    public List<Post> getAllPostsByPublishedDateAsc(int start, int limit) {
        Pageable pageable = PageRequest.of(start/limit, limit, Sort.by("publishedAt").ascending());
        return postRepository.findPostOrderByPublisedAtAsc(pageable);
    }    

    public List<Post> getAllPostsByPage(int startPage, int endPage) {
        Pageable pageable = PageRequest.of(startPage / endPage, endPage);
        return postRepository.findAllPostsByPage(pageable);
    }

    public List<Post> getAllPostsByAuthorAndTag(List<Integer> authorId, List<Integer> tagIds, int startPage, int endPage) {
        Pageable pageable = PageRequest.of(startPage / endPage, endPage);
        return postRepository.findAllPostsByAuthorAndTag(authorId, tagIds, pageable);
    }

    public List<Post> getAllPostsByTagIdsPublishedDateDesc(List<Integer> tagIds, int start, int limit) {
        Pageable pageable = PageRequest.of(start/limit, limit, Sort.by("publishedAt").descending());
        return postRepository.findAllPostsByTagIdsPublishedDateDesc(tagIds, pageable);
    }
    
    public List<Post> getAllPostsByTagIdsPublishedDateAsc(List<Integer> tagIds, int start, int limit) {
        Pageable pageable = PageRequest.of(start/limit, limit, Sort.by("publishedAt").ascending());
        return postRepository.findAllPostsByTagIdsPublishedDateAsc(tagIds, pageable);
    }

    public List<Post> getAllPostsByAuthorPublishedDateDesc(List<Integer> authorIds, int start, int limit) {
        Pageable pageable = PageRequest.of(start/limit, limit, Sort.by("publishedAt").descending());
        return postRepository.findAllPostsByAuthorInPublishedDateDesc(authorIds, pageable);
    }

    public List<Post> getAllPostsByAuthorPublishedDateAsc(List<Integer> authorIds, int start, int limit) {
        Pageable pageable = PageRequest.of(start/limit, limit, Sort.by("publishedAt").ascending());
        return postRepository.findAllPostsByAuthorInPublishedDateAsc(authorIds, pageable);
    }

    public List<Post> getAllPostsByAuthorAndTagPublishedDateDesc(List<Integer> authorIds, List<Integer> tagIds,
            int start, int limit) {
        Pageable pageable = PageRequest.of(start/limit, limit, Sort.by("publishedAt").descending());
        return postRepository.findAllPostsByAuthorAndTagPublishedDateDesc(authorIds, tagIds, pageable);
    }

    public List<Post> getAllPostsByAuthorAndTagPublishedDateAsc(List<Integer> authorIds, List<Integer> tagIds, int start,
            int limit) {
        Pageable pageable = PageRequest.of(start/limit, limit, Sort.by("publishedAt").ascending());
        return postRepository.findAllPostsByAuthorAndTagPublishedDateAsc(authorIds, tagIds, pageable);
    }

    public List<Post> getAllPostsBySearchedValueAndAuthor(String searchedValue, List<Integer> author, int start, int limit) {
        Pageable pageable = PageRequest.of(start/limit, limit);
        return postRepository.findAllPostsBySearchedValueAndAuthor(searchedValue, author, pageable);
    }

    public List<Post> getAllPostsBySearchedValueAndTag(String searchedValue, List<Integer> tagIds, int start,
            int limit) {
        Pageable pageable = PageRequest.of(start/limit, limit);
        return postRepository.findAllPostsBySearchedValueAndTag(searchedValue, tagIds, pageable);
    }

    public List<Post> getAllPostsBySearchedValue(String searchedValue, int start, int limit) {
        Pageable pageable = PageRequest.of(start/limit, limit);
        return postRepository.findAllPostsBySearchedValue(searchedValue, pageable);
    }

    public List<Post> getAllPostsBySearchedValueAndAuthorInDesc(
        String searchedValue, 
        List<Integer> authorIds, int start, int limit) {
        Pageable pageable = PageRequest.of(start/limit, limit, Sort.by("publishedAt").descending());

        return postRepository.findAllPostsBySearchedValueAndAuthorInDesc(
            searchedValue, authorIds, pageable);
    }

    public List<Post> getAllPostsBySearchedValueAndAuthorInAsc(
        String searchedValue, List<Integer> authorIds, int start, int limit) {
        Pageable pageable = PageRequest.of(start/limit, limit, Sort.by("publishedAt").ascending());

        return postRepository.findAllPostsBySearchedValueAndAuthorInAsc(
            searchedValue, authorIds, pageable);
    }

    public List<Post> getAllPostsBySearchedValueAuthorTagInDesc(
        String searchedValue, List<Integer> authorIds,
            List<Integer> tagIds, int start, int limit) {
        Pageable pageable = PageRequest.of(start/limit, limit, Sort.by("publishedAt").descending());

        return postRepository.findAllPostsBySearchedValueAuthorTagInDesc(
            searchedValue, authorIds, tagIds, pageable);
    }

    public List<Post> getAllPostsBySearchedValueAndAuthorTagInAsc(
        String searchedValue, List<Integer> authorIds,
            List<Integer> tagIds,
            int start, int limit) {
        Pageable pageable = PageRequest.of(start/limit, limit, Sort.by("publishedAt").ascending());

        return postRepository.findAllPostsBySearchedValueAuthorTagInAsc(
            searchedValue, authorIds, tagIds, pageable);
    }

    public List<Post> getAllPostsBySearchedValueAuthorTagWithoutSort(
        String searchedValue, List<Integer> authorIds,
            List<Integer> tagIds,
            int start, int limit) {
        Pageable pageable = PageRequest.of(start/limit, limit);        
        return postRepository.findAllPostsBySearchedValueAuthorTagNoSorting(
            searchedValue, authorIds, tagIds, pageable);
    }

    

    

    
}
