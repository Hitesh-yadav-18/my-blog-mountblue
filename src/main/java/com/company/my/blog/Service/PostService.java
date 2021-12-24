package com.company.my.blog.Service;

import java.util.Date;
import java.util.List;

import com.company.my.blog.model.Post;
import com.company.my.blog.model.User;
import com.company.my.blog.repository.PostRepository;
import com.company.my.blog.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;
    
    @Autowired
    private UserRepository userRepository;

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

    public List<Post> getAllPosts() {
       return  postRepository.findAll();
    }

    public Post getParticularPost(int id) {
        return postRepository.findPostById(id);
    }

    public List<Post> getPostByAuthor(User author) {
        return postRepository.findByAuthor(author.getName());
    }

    public void deletePost(Integer id) {
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

}
