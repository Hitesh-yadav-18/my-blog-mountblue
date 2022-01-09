package com.company.my.blog.repository;

import java.util.List;

import com.company.my.blog.dto.TagDto;
import com.company.my.blog.model.Tag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {

        @Query(value = "SELECT * FROM tag WHERE tag_name = :tagName", 
                        nativeQuery = true)
        Tag findByTagName(@Param("tagName") String tagName);

        @Query("select new com.company.my.blog.dto.TagDto(t.tagId, t.tagName) "+
                "from Tag t, PostTag pt where pt.tag.tagId = t.tagId and pt.post.id = :postId")
        List<TagDto> findTagsByPostId(@Param("postId") Integer postId);

        @Query("SELECT DISTINCT t from Tag t, Post p, PostTag pt where p.id = pt.post " +
                        "and t.id = pt.tag and p.author.id in(:authorId)")
        List<Tag> findAllTagsOfSelectedAuthor(@Param("authorId") List<Integer> authorId);
}
