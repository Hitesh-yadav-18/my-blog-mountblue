package com.company.my.blog.repository;

import java.util.List;

import com.company.my.blog.model.Post;
import com.company.my.blog.model.Tag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {


    @Query(
        value = "SELECT * FROM tag WHERE tag_name = :tagName",
        nativeQuery = true
    )
    Tag findByTagName(@Param("tagName") String tagName);
   
    @Query(
        value="select tag_name from tag "+
        "inner join post_tag on tag.tag_id = post_tag.tag_id "+
        "inner join post on post.id = post_tag.post_id where post.id = :postId ",
        nativeQuery = true
    )
    List <String> findTagsByName(@Param("postId") int postId);
}
