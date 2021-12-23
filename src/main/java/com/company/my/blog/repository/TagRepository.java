package com.company.my.blog.repository;

import java.util.List;

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

}
