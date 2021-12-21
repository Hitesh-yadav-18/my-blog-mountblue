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

    public void createNewPost(String title, String excerpt, String content) {
        Post post = new Post();
        post.setTitle(title);
        post.setExcerpt(excerpt);
        post.setContent(content);
        post.setAuthor("Hitesh");
        post.setPublished(true);
        post.setPublishedAt(new Date());
        post.setCreatedAt(new Date());
        post.setUpdatedAt(new Date());

        postRepository.save(post);
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

    // public List<Post> getPostByTags(String tags) {
    //     return postRepository.findByTags(tags);
    // }
}
