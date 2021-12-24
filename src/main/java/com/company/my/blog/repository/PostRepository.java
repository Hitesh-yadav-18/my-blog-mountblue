package com.company.my.blog.repository;

import java.util.Date;
import java.util.List;

import com.company.my.blog.model.Post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    List<Post> findAll();

    Post findPostById(int id);

    List<Post> findByAuthor(String Author);

    @Modifying
    @Transactional
    @Query(value = "UPDATE post SET title = :title, excerpt = :excerpt, content = :content, updated_at = :updatedAt  WHERE id = :postId",
           nativeQuery = true
          )
    void updatePostByPostId(
        @Param("postId") int postId,
        @Param("title") String title,
        @Param("excerpt") String excerpt,
        @Param("content") String content,
        @Param("updatedAt") Date updatedAt);
    
    @Query(value = "SELECT * FROM post WHERE is_published = true ORDER BY published_at DESC",
           nativeQuery = true
          )    
     List<Post> findPostOrderByPublisedAtDesc();

    @Query(value = "SELECT * FROM post WHERE is_published = true ORDER BY published_at ASC",
           nativeQuery = true
          ) 
    List<Post> findPostOrderByPublisedAtAsc();

    @Query("select p from Post p where lower(p.title) like lower(concat('%',:searchedValue,'%')) " +
                        "or lower(p.content) like lower(concat('%',:searchedValue,'%')) " +
                        "or lower(p.excerpt) like lower(concat('%',:searchedValue,'%')) " +
                        "or lower(p.author.name) like lower(concat('%',:searchedValue,'%')) "
    )
    List<Post> findAllPostsBySearchedValue(@Param("searchedValue") String searchedValue);

    @Query(
        value = "SELECT * FROM post ORDER BY id ASC LIMIT :endPage OFFSET :startPage",
        nativeQuery = true
    )
    List<Post> findAllPostsByPage(@Param("startPage") int startPage, @Param("endPage") int endPage);    


}