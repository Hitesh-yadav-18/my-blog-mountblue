package com.company.my.blog.repository;

import java.util.List;

import com.company.my.blog.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    List<Post> findAll();

    Post findPostById(int id);

    List<Post> findByAuthor(String Author);

    // List<Post> findByTags(String tags);
}