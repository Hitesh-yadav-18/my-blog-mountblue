package com.company.my.blog.Service;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.company.my.blog.model.Post;
import com.company.my.blog.model.User;
import com.company.my.blog.repository.PostRepository;
import com.company.my.blog.repository.UserRepository;

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
       return  postRepository.findAll();
    }

    public Post getParticularPost(int id) {
        return postRepository.findPostById(id);
    }

    public List<Post> getPostByAuthor(List<Integer> authorId, int start, int limit) {
        return postRepository.findByAuthor(authorId, start, limit);
    }

    public List<Post> getAllPostsByTagId(List<Integer> tagIds, int startPage, int endPage) {
        Pageable pageable = PageRequest.of(startPage/endPage, endPage, Sort.by("publishedAt").descending());
        return postRepository.findAllByTagId(tagIds, pageable);
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

    public List<Post> getAllPostsByPublishedDateDesc(int start, int limit) {
        return postRepository.findPostOrderByPublisedAtDesc(start, limit);
    }

    public List<Post> getAllPostsByPublishedDateAsc(int start, int limit) {
        return postRepository.findPostOrderByPublisedAtAsc(start, limit);
    }

    public List<Post> getAllPostsBySearchedValue(String searchedValue){
        return postRepository.findAllPostsBySearchedValue(searchedValue);
    }

    public List<Post> getAllPostsByPage(int startPage, int endPage) {
        return postRepository.findAllPostsByPage(startPage, endPage);
    }

    public List<Post> getAllPostsByAuthorAndTag(List<Integer> authorId, List<Integer> tagIds) {
        return postRepository.findAllPostsByAuthorAndTag(authorId, tagIds);
    }

    public List<Post> getAllPostsByAuthorInPublishedDateDesc(int authorId) {
        return postRepository.findAllPostsByAuthorInPublishedDateDesc(authorId);
    }

    public List<Post> getAllPostsByAuthorInPublishedDateAsc(int authorId) {
        return postRepository.findAllPostsByAuthorInPublishedDateAsc(authorId);
    }

    public List<Post> getAllPostsBySearchedValueAndAuthor(String searchedValue, List<Integer> author) {
        return postRepository.findAllPostsBySearchedValueAndAuthor(searchedValue, author);
    }

    public List<Post> getAllPostsBySearchedValueAndAuthorInDesc(String searchedValue, List<Integer> authorIds) {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("publishedAt").descending());
        return postRepository.findAllPostsBySearchedValueAndAuthorInDesc(searchedValue, authorIds, pageable);
    }

    public List<Post> getAllPostsBySearchedValueAndAuthorInAsc(String searchedValue, List<Integer> authorIds) {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("publishedAt").ascending());
        return postRepository.findAllPostsBySearchedValueAndAuthorInAsc(searchedValue, authorIds, pageable);
    }

    public List<Post> getAllPostsBySearchedValueAuthorTagInDesc(String searchedValue, List<Integer> authorIds, List<Integer> tagIds) {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("publishedAt").descending());
        return postRepository.findAllPostsBySearchedValueAuthorTagInDesc(searchedValue, authorIds, tagIds, pageable);
    }

    public List<Post> getAllPostsBySearchedValueAndAuthorTagInAsc(String searchedValue, List<Integer> authorIds, List<Integer> tagIds) {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("publishedAt").ascending());
        return postRepository.findAllPostsBySearchedValueAuthorTagInAsc(searchedValue, authorIds, tagIds, pageable);
    }

    public List<Post> getAllPostsBySearchedValueAuthorTagWithoutSort(String searchedValue, List<Integer> authorIds,
            List<Integer> tagIds) {
        return postRepository.findAllPostsBySearchedValueAuthorTagNoSorting(searchedValue, authorIds, tagIds);
    }

    
   

}
