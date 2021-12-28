package com.company.my.blog.repository;

import java.util.Date;
import java.util.List;

import com.company.my.blog.model.Post;

import org.springframework.data.domain.Pageable;
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

        @Query(value = "select p from Post p, PostTag pt, Tag t where p.id = pt.post and " +
                        " t.id = pt.tag and t.id in (:tagIds) ", nativeQuery = true)
        List<Post> findPostByTag(@Param("tagId") List<Integer> tagIds);

        @Query("SELECT p FROM Post p WHERE p.author.id in (:authorId)")
        List<Post> findByAuthorId(
                        @Param("authorId") List<Integer> Author,
                        Pageable pageable);

        @Query("select p from Post p, PostTag pt, Tag t where p.id = pt.post and " +
                        " t.id = pt.tag and t.id in (:tagIds)")
        List<Post> findAllByTagId(
                        @Param("tagIds") List<Integer> tagIds,
                        Pageable pageable);

        @Modifying
        @Transactional
        @Query(value = "UPDATE post SET title = :title, excerpt = :excerpt," +
                        "content = :content, updated_at = :updatedAt  WHERE id = :postId",
                        nativeQuery = true)
        void updatePostByPostId(
                        @Param("postId") int postId,
                        @Param("title") String title,
                        @Param("excerpt") String excerpt,
                        @Param("content") String content,
                        @Param("updatedAt") Date updatedAt);

        @Query(value = "SELECT p FROM Post p WHERE isPublished = true ")
        List<Post> findPostOrderByPublisedAtDesc(Pageable pageable);

        @Query(value = "SELECT p FROM Post p WHERE isPublished = true")
        List<Post> findPostOrderByPublisedAtAsc(Pageable pageable);

        @Query("select p from Post p where lower(p.title) like lower(concat('%',:searchedValue,'%')) " +
                        "or lower(p.content) like lower(concat('%',:searchedValue,'%')) " +
                        "or lower(p.excerpt) like lower(concat('%',:searchedValue,'%')) " +
                        "or lower(p.author.name) like lower(concat('%',:searchedValue,'%')) ")
        List<Post> findAllPostsBySearchedValue(@Param("searchedValue") String searchedValue, Pageable pageable);

        @Query("SELECT p from Post p")
        List<Post> findAllPostsByPage(Pageable pageable);

        @Query("select p from Post p, PostTag pt, Tag t where p.id = pt.post and " +
                        " t.id = pt.tag and p.author.id in (:authorIds) and t.id in (:tagIds) ")
        List<Post> findAllPostsByAuthorAndTag(
                        @Param("authorIds") List<Integer> authorId,
                        @Param("tagIds") List<Integer> tagIds,
                        Pageable pageable);
        @Query("select p from Post p, PostTag pt, Tag t where p.id = pt.post and " +
                        " t.id = pt.tag and p.author.id in (:authorIds) and p.publishedAt = :publishedAt ")
        List<Post> findByAuthorIdAndPublished(
                @Param("authorIds") List<Integer> authorId, 
                @Param("publishedAt") Date sortField, Pageable pageable);                

        @Query(value = "SELECT * FROM post WHERE is_published = true "+
                        "and author = :authorId ORDER BY published_at DESC", 
                        nativeQuery = true)
        List<Post> findAllPostsByAuthorInPublishedDateDesc(int authorId);

        @Query(value = "SELECT * FROM post WHERE is_published = true " +
                        "AND author = :authorId ORDER BY published_at ASC", nativeQuery = true)
        List<Post> findAllPostsByAuthorInPublishedDateAsc(int authorId);

        @Query("select p from Post p, PostTag pt, Tag t where p.id = pt.post and " +
                        " t.id = pt.tag and t.id in (:tagIds) ")
        List<Post> findAllPostsByTagIdsPublishedDateDesc(List<Integer> tagIds, Pageable pageable);

        @Query("select p from Post p, PostTag pt, Tag t where p.id = pt.post and " +
                        " t.id = pt.tag and t.id in (:tagIds) ")
        List<Post> findAllPostsByTagIdsPublishedDateAsc(List<Integer> tagIds, Pageable pageable);

        @Query("select p from Post p where p.author.id in (:authorIds)")
        List<Post> findAllPostsByAuthorInPublishedDateDesc(List<Integer> authorIds, Pageable pageable);

        @Query("select p from Post p where p.author.id in (:authorIds)")
        List<Post> findAllPostsByAuthorInPublishedDateAsc(List<Integer> authorIds, Pageable pageable);

        @Query("select p from Post p, PostTag pt, Tag t where p.id = pt.post and " +
                        " t.id = pt.tag and p.author.id in (:authorIds) AND t.id in (:tagIds) ")
        List<Post> findAllPostsByAuthorAndTagPublishedDateDesc(List<Integer> authorIds, List<Integer> tagIds,
                Pageable pageable);

        @Query("select p from Post p, PostTag pt, Tag t where p.id = pt.post and " +
                " t.id = pt.tag and p.author.id in (:authorIds) AND t.id in (:tagIds) ")        
        List<Post> findAllPostsByAuthorAndTagPublishedDateAsc(List<Integer> authorIds, List<Integer> tagIds,
                Pageable pageable);

        @Query("select p from Post p, PostTag pt, Tag t " +
                        "where p.id = pt.post and t.tagId = pt.tag and " +
                        "(lower(p.author.name) like lower(concat('%', :searchedValue,'%')) or " +
                        "lower(p.title) like lower(concat('%', :searchedValue,'%')) or " +
                        "lower(p.content) like lower(concat('%', :searchedValue,'%')) or " +
                        "lower(p.excerpt) like lower(concat('%', :searchedValue,'%')) ) and " +
                        "p.author.id in (:authorIds) group by p.id")
        List<Post> findAllPostsBySearchedValueAndAuthor(
                        @Param("searchedValue") String searchedValue,
                        @Param("authorIds") List<Integer> authorIds,
                        Pageable pageable);

        @Query("select p from Post p, PostTag pt, Tag t " +
                        "where p.id = pt.post and t.tagId = pt.tag and " +
                        "(lower(p.author.name) like lower(concat('%', :searchedValue,'%')) or " +
                        "lower(p.title) like lower(concat('%', :searchedValue,'%')) or " +
                        "lower(p.content) like lower(concat('%', :searchedValue,'%')) or " +
                        "lower(p.excerpt) like lower(concat('%', :searchedValue,'%')) ) and " +
                        "t.tagId in (:tagIds) group by p.id")
        List<Post> findAllPostsBySearchedValueAndTag(String searchedValue, List<Integer> tagIds, Pageable pageable);
                

        @Query("select p from Post p, PostTag pt, Tag t " +
                        "where p.id = pt.post and t.tagId = pt.tag and " +
                        "(lower(p.author.name) like lower(concat('%', :searchedValue,'%')) or " +
                        "lower(p.title) like lower(concat('%', :searchedValue,'%')) or " +
                        "lower(p.content) like lower(concat('%', :searchedValue,'%')) or " +
                        "lower(p.excerpt) like lower(concat('%', :searchedValue,'%')) ) and " +
                        "p.author.id in (:authorIds) and t.tagId in (:tagIds) group by p.id")
        List<Post> findAllPostsBySearchedValueAuthorTagNoSorting(
                        @Param("searchedValue") String searchedValue,
                        @Param("authorIds") List<Integer> authorIds,
                        @Param("tagIds") List<Integer> tagIds,
                        Pageable pageable);

        @Query("select p from Post p, PostTag pt, Tag t " +
                        "where p.id = pt.post and t.tagId = pt.tag and " +
                        "(lower(p.author.name) like lower(concat('%', :searchedValue,'%')) or " +
                        "lower(p.title) like lower(concat('%', :searchedValue,'%')) or " +
                        "lower(p.content) like lower(concat('%', :searchedValue,'%')) or " +
                        "lower(p.excerpt) like lower(concat('%', :searchedValue,'%')) ) and " +
                        "p.author.id in (:authorIds) group by p.id")
        List<Post> findAllPostsBySearchedValueAndAuthorInDesc(
                        @Param("searchedValue") String searchedValue,
                        @Param("authorIds") List<Integer> authorId,
                        Pageable pageable);

        @Query("select p from Post p, PostTag pt, Tag t " +
                        "where p.id = pt.post and t.tagId = pt.tag and " +
                        "(lower(p.author.name) like lower(concat('%', :searchedValue,'%')) or " +
                        "lower(p.title) like lower(concat('%', :searchedValue,'%')) or " +
                        "lower(p.content) like lower(concat('%', :searchedValue,'%')) or " +
                        "lower(p.excerpt) like lower(concat('%', :searchedValue,'%')) ) and " +
                        "p.author.id in (:authorIds) group by p.id")
        List<Post> findAllPostsBySearchedValueAndAuthorInAsc(
                        @Param("searchedValue") String searchedValue,
                        @Param("authorIds") List<Integer> authorIds,
                        Pageable pageable);

        @Query("select p from Post p, PostTag pt, Tag t " +
                        "where p.id = pt.post and t.tagId = pt.tag and " +
                        "(lower(p.author.name) like lower(concat('%', :searchedValue,'%')) or " +
                        "lower(p.title) like lower(concat('%', :searchedValue,'%')) or " +
                        "lower(p.content) like lower(concat('%', :searchedValue,'%')) or " +
                        "lower(p.excerpt) like lower(concat('%', :searchedValue,'%')) ) and " +
                        "p.author.id in (:authorIds) and t.tagId in (:tagIds) group by p.id")
        List<Post> findAllPostsBySearchedValueAuthorTagInDesc(
                        @Param("searchedValue") String searchedValue,
                        @Param("authorIds") List<Integer> authorId,
                        @Param("tagIds") List<Integer> tagIds,
                        Pageable pageable);

        @Query("select p from Post p, PostTag pt, Tag t " +
                        "where p.id = pt.post and t.tagId = pt.tag and " +
                        "(lower(p.author.name) like lower(concat('%', :searchedValue,'%')) or " +
                        "lower(p.title) like lower(concat('%', :searchedValue,'%')) or " +
                        "lower(p.content) like lower(concat('%', :searchedValue,'%')) or " +
                        "lower(p.excerpt) like lower(concat('%', :searchedValue,'%')) ) and " +
                        "p.author.id in (:authorIds) and t.tagId in (:tagIds) group by p.id")
        List<Post> findAllPostsBySearchedValueAuthorTagInAsc(
                        @Param("searchedValue") String searchedValue,
                        @Param("authorIds") List<Integer> authorId,
                        @Param("tagIds") List<Integer> tagIds,
                        Pageable pageable);

       

       
        

       

        

}