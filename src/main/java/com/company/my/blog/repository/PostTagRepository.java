package com.company.my.blog.repository;

import com.company.my.blog.model.Post;
import com.company.my.blog.model.PostTag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostTagRepository extends JpaRepository<PostTag, Integer> {

    @Modifying
    @Query("DELETE FROM PostTag pt where pt.post = :postId")
    void deleteTagsByPostId(@Param("postId") Post post);
    
}
