package com.company.my.blog.repository;

import com.company.my.blog.model.PostTag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostTagRepository extends JpaRepository<PostTag, Integer> {
    @Query(
        value = "INSERT INTO post_tag (post_id, tag_id) VALUES (?1, ?2)",
        nativeQuery = true
    )
    void addPostIdAndTagIdIntoTable(int postId, int tagId);

}
