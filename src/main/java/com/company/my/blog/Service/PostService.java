package com.company.my.blog.Service;

import java.util.Date;
import java.util.List;

import com.company.my.blog.model.Post;
import com.company.my.blog.repository.PostRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public Post createNewPost(String title, String excerpt, String content) {
        Post post = new Post();
        post.setTitle(title);
        post.setExcerpt(excerpt);
        post.setContent(content);
        post.setAuthor("Hitesh");
        post.setPublished(true);
        post.setPublishedAt(new Date());
        post.setCreatedAt(new Date());
        post.setUpdatedAt(new Date());

        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
       return  postRepository.findAll();
    }

    public Post getParticularPost(int id) {
        return postRepository.findPostById(id);
    }

    public List<Post> getPostByAuthor(String author) {
        return postRepository.findByAuthor(author);
    }

    public void deletePost(int id) {
        postRepository.deleteById(id);
    }

    public void updatePostById(int id, String title, String excerpt, String content, Date updatedAt) {
        postRepository.updatePostByPostId(id, title, excerpt, content, updatedAt);
    }

    public List<Post> getAllPostsByPublishedDateDesc() {
        return postRepository.findPostOrderByPublisedAtDesc();
    }

    public List<Post> getAllPostsByPublishedDateAsc() {
        return postRepository.findPostOrderByPublisedAtAsc();
    }

    public List<Post> getAllPostsBySearchedValue(String searchedValue){
        return postRepository.findAllPostsBySearchedValue(searchedValue);
    }

    public List<Post> getAllPostsByPage(int startPage, int endPage) {
        return postRepository.findAllPostsByPage(startPage, endPage);
    }

    // public List<Post> getPostByTags(String tags) {
    //     return postRepository.findByTags(tags);
    // }
}
