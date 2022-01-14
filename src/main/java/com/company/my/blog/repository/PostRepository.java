package com.company.my.blog.repository;

import java.util.Date;
import java.util.List;

import com.company.my.blog.dto.PostDto;
import com.company.my.blog.dto.PostExcerptDto;
import com.company.my.blog.model.Post;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

        @Query("SELECT p FROM Post p WHERE p.id = :postId")
        Post findFullPostById(@Param("postId") int postId);
        
        @Query("SELECT new com.company.my.blog.dto.PostDto(p.id, p.title, p.excerpt, p.content, p.publishedAt, p.createdAt, p.author.id, p.author.name, p.author.email) FROM Post p WHERE p.id = :postId")
        PostDto findPostById(@Param("postId") int postId);

        @Query("select p from Post p, PostTag pt, Tag t where p.id = pt.post and " +
                        " t.id = pt.tag and t.id in (:tagIds) ")
        List<Post> findPostByTag(@Param("tagId") List<Integer> tagIds);

        @Query("SELECT new com.company.my.blog.dto.PostExcerptDto(p.id, p.title, p.excerpt, p.publishedAt, p.author.id, p.author.name, p.author.email) from Post p where isPublished = true")
        List<PostExcerptDto> findAllPostsByPage(Pageable pageable);

        @Query("SELECT new com.company.my.blog.dto.PostExcerptDto(p.id, p.title, p.excerpt, p.publishedAt, p.author.id, p.author.name, p.author.email) FROM Post p WHERE p.author.id in (:authorIds)")
        List<PostExcerptDto> findByAuthorId(
                        @Param("authorIds") List<Integer> Author,
                        Pageable pageable);

        @Query("select new com.company.my.blog.dto.PostExcerptDto(p.id, p.title, p.excerpt, p.publishedAt, p.author.id, p.author.name, p.author.email) from Post p, PostTag pt, Tag t where p.id = pt.post and " +
                        " t.id = pt.tag and t.id in (:tagIds)")
        List<PostExcerptDto> findAllByTagId(
                        @Param("tagIds") List<Integer> tagIds,
                        Pageable pageable);

        @Query("select new com.company.my.blog.dto.PostExcerptDto(p.id, p.title, p.excerpt, p.publishedAt, p.author.id, p.author.name, p.author.email) from Post p, PostTag pt, Tag t where p.id = pt.post and " +
                        " t.id = pt.tag and p.author.id in (:authorIds) and t.id in (:tagIds) ")
        List<PostExcerptDto> findAllPostsByAuthorAndTag(
                        @Param("authorIds") List<Integer> authorIds,
                        @Param("tagIds") List<Integer> tagIds,
                        Pageable pageable);

        @Query(value = "SELECT new com.company.my.blog.dto.PostExcerptDto(p.id, p.title, p.excerpt, p.publishedAt, p.author.id, p.author.name, p.author.email) FROM Post p WHERE isPublished = true ")
        List<PostExcerptDto> findAllPostsInSortingOrder(Pageable pageable);

        @Query("select new com.company.my.blog.dto.PostExcerptDto(p.id, p.title, p.excerpt, p.publishedAt, p.author.id, p.author.name, p.author.email) from Post p, PostTag pt, Tag t where p.id = pt.post and " +
                        " t.id = pt.tag and t.id in (:tagIds) ")
        List<PostExcerptDto> findAllPostsByTagIdsInSortingOrder(
                        @Param("tagIds") List<Integer> tagIds, 
                        Pageable pageable);

        @Query("select new com.company.my.blog.dto.PostExcerptDto(p.id, p.title, p.excerpt, p.publishedAt, p.author.id, p.author.name, p.author.email) from Post p where p.author.id in (:authorIds)")
        List<PostExcerptDto> findAllPostsByAuthorIdsInSortingOrder(
                        @Param("authorIds") List<Integer> authorIds, 
                        Pageable pageable);

        @Query("select new com.company.my.blog.dto.PostExcerptDto(p.id, p.title, p.excerpt, p.publishedAt, p.author.id, p.author.name, p.author.email) from Post p, PostTag pt, Tag t where p.id = pt.post and " +
                        " t.id = pt.tag and p.author.id in (:authorIds) AND t.id in (:tagIds) ")
        List<PostExcerptDto> findAllPostsByAuthorIdsAndTagIdsInSortingOrder(
                        @Param("authorIds") List<Integer> authorIds, 
                        @Param("tagIds") List<Integer> tagIds,
                        Pageable pageable);

        @Query("select new com.company.my.blog.dto.PostExcerptDto(p.id, p.title, p.excerpt, p.publishedAt, p.author.id, p.author.name, p.author.email) from Post p where lower(p.title) like lower(concat('%',:searchedValue,'%')) " +
                        "or lower(p.content) like lower(concat('%',:searchedValue,'%')) " +
                        "or lower(p.excerpt) like lower(concat('%',:searchedValue,'%')) " +
                        "or lower(p.author.name) like lower(concat('%',:searchedValue,'%')) ")
        List<PostExcerptDto> findAllPostsBySearchedValue(
                        @Param("searchedValue") String searchedValue,
                        Pageable pageable);

        @Query("SELECT new com.company.my.blog.dto.PostExcerptDto(p.id, p.title, p.excerpt, p.publishedAt, p.author.id, p.author.name, p.author.email) from Post p where publishedAt BETWEEN (:fromDate) and (:toDate)")
        List<PostExcerptDto> findAllPostsByDates(
                        Pageable pageable, 
                        @Param("fromDate") Date fromDate,
                        @Param("toDate") Date toDate);

        @Query("SELECT new com.company.my.blog.dto.PostExcerptDto(p.id, p.title, p.excerpt, p.publishedAt, p.author.id, p.author.name, p.author.email) from Post p where p.author.id in (:authorIds)"+
                " and (publishedAt BETWEEN (:fromDate) and (:toDate))")
        List<PostExcerptDto> findPostsByAuthorAndDates(
                        @Param("authorIds") List<Integer> authorIds,
                        Pageable pageable,
                        @Param("fromDate") Date fromDate, 
                        @Param("toDate") Date toDate);

        @Query("select new com.company.my.blog.dto.PostExcerptDto(p.id, p.title, p.excerpt, p.publishedAt, p.author.id, p.author.name, p.author.email) from Post p, PostTag pt, Tag t where p.id = pt.post and " +
                        "t.id = pt.tag and t.id in (:tagIds) and "+
                        "p.publishedAt BETWEEN (:fromDate) and (:toDate)")
        List<PostExcerptDto> findAllByTagIdAndDates(
                        @Param("tagIds") List<Integer> tagIds,
                        Pageable pageable, 
                        @Param("fromDate") Date fromDate,
                        @Param("toDate") Date toDate);

        @Query("select new com.company.my.blog.dto.PostExcerptDto(p.id, p.title, p.excerpt, p.publishedAt, p.author.id, p.author.name, p.author.email) from Post p, PostTag pt, Tag t where p.id = pt.post and " +
                        "t.id = pt.tag and p.author.id in (:authorIds) and t.id in (:tagIds) and "+
                        "(p.publishedAt BETWEEN (:fromDate) and (:toDate))")
        List<PostExcerptDto> findAllPostsByAuthorAndTagAndDates(
                        @Param("authorIds")List<Integer> authorIds, 
                        @Param("tagIds") List<Integer> tagIds, 
                        Pageable pageable,
                        @Param("fromDate") Date fromDate, 
                        @Param("toDate") Date toDate);

        @Query("select Distinct new com.company.my.blog.dto.PostExcerptDto(p.id, p.title, p.excerpt, p.publishedAt, p.author.id, p.author.name, p.author.email) from Post p, PostTag pt, Tag t " +
                        "where p.id = pt.post and t.tagId = pt.tag and " +
                        "(lower(p.author.name) like lower(concat('%', :searchedValue,'%')) or " +
                        "lower(p.title) like lower(concat('%', :searchedValue,'%')) or " +
                        "lower(p.content) like lower(concat('%', :searchedValue,'%')) or " +
                        "lower(p.excerpt) like lower(concat('%', :searchedValue,'%')) ) and " +
                        "p.author.id in (:authorIds)")
        List<PostExcerptDto> findAllPostsBySearchedValueAndAuthor(
                        @Param("searchedValue") String searchedValue,
                        @Param("authorIds") List<Integer> authorIds,
                        Pageable pageable);

        @Query("select Distinct new com.company.my.blog.dto.PostExcerptDto(p.id, p.title, p.excerpt, p.publishedAt, p.author.id, p.author.name, p.author.email) from Post p, PostTag pt, Tag t " +
                        "where p.id = pt.post and t.tagId = pt.tag and " +
                        "(lower(p.author.name) like lower(concat('%', :searchedValue,'%')) or " +
                        "lower(p.title) like lower(concat('%', :searchedValue,'%')) or " +
                        "lower(p.content) like lower(concat('%', :searchedValue,'%')) or " +
                        "lower(p.excerpt) like lower(concat('%', :searchedValue,'%')) ) and " +
                        "t.tagId in (:tagIds)")
        List<PostExcerptDto> findAllPostsBySearchedValueAndTag(
                        @Param("searchedValue") String searchedValue,
                        @Param("tagIds") List<Integer> tagIds,
                        Pageable pageable);

        @Query("select DISTINCT new com.company.my.blog.dto.PostExcerptDto(p.id, p.title, p.excerpt, p.publishedAt, p.author.id, p.author.name, p.author.email) from Post p, PostTag pt, Tag t " +
                        "where p.id = pt.post and t.tagId = pt.tag and " +
                        "(lower(p.author.name) like lower(concat('%', :searchedValue,'%')) or " +
                        "lower(p.title) like lower(concat('%', :searchedValue,'%')) or " +
                        "lower(p.content) like lower(concat('%', :searchedValue,'%')) or " +
                        "lower(p.excerpt) like lower(concat('%', :searchedValue,'%')) ) and " +
                        "p.author.id in (:authorIds) and t.tagId in (:tagIds)")
        List<PostExcerptDto> findAllPostsBySearchedValueAndAuthorAndTag(
                        @Param("searchedValue") String searchedValue,
                        @Param("authorIds") List<Integer> authorIds,
                        @Param("tagIds") List<Integer> tagIds,
                        Pageable pageable);

        @Query("select DISTINCT new com.company.my.blog.dto.PostExcerptDto(p.id, p.title, p.excerpt, p.publishedAt, p.author.id, p.author.name, p.author.email) from Post p, PostTag pt, Tag t " +
                        "where p.id = pt.post and t.tagId = pt.tag and " +
                        "(lower(p.author.name) like lower(concat('%', :searchedValue,'%')) or " +
                        "lower(p.title) like lower(concat('%', :searchedValue,'%')) or " +
                        "lower(p.content) like lower(concat('%', :searchedValue,'%')) or " +
                        "lower(p.excerpt) like lower(concat('%', :searchedValue,'%')) ) and " +
                        "p.author.id in (:authorIds)")
        List<PostExcerptDto> findAllPostsBySearchedValueAndAuthorAndSorted(
                        @Param("searchedValue") String searchedValue,
                        @Param("authorIds") List<Integer> authorId,
                        Pageable pageable);

        @Query("select DISTINCT new com.company.my.blog.dto.PostExcerptDto(p.id, p.title, p.excerpt, p.publishedAt, p.author.id, p.author.name, p.author.email) from Post p, PostTag pt, Tag t " +
                        "where p.id = pt.post and t.tagId = pt.tag and " +
                        "(lower(p.author.name) like lower(concat('%', :searchedValue,'%')) or " +
                        "lower(p.title) like lower(concat('%', :searchedValue,'%')) or " +
                        "lower(p.content) like lower(concat('%', :searchedValue,'%')) or " +
                        "lower(p.excerpt) like lower(concat('%', :searchedValue,'%')) ) and " +
                        "p.author.id in (:authorIds) and t.tagId in (:tagIds)")
        List<PostExcerptDto> findAllPostsBySearchedValueAndAuthorAndTagAndSorted(
                        @Param("searchedValue") String searchedValue,
                        @Param("authorIds") List<Integer> authorId,
                        @Param("tagIds") List<Integer> tagIds,
                        Pageable pageable);

        @Query("select DISTINCT new com.company.my.blog.dto.PostExcerptDto(p.id, p.title, p.excerpt, p.publishedAt, p.author.id, p.author.name, p.author.email) from Post p where (lower(p.title) like lower(concat('%',:searchedValue,'%')) " +
                        "or lower(p.content) like lower(concat('%',:searchedValue,'%')) " +
                        "or lower(p.excerpt) like lower(concat('%',:searchedValue,'%')) " +
                        "or lower(p.author.name) like lower(concat('%',:searchedValue,'%'))) " +
                        "and (p.publishedAt BETWEEN :startDate AND :endDate)")
        List<PostExcerptDto> findAllPostsBySearchedValueAndDates(
                        @Param("searchedValue") String searchedValue,
                        Pageable pageable,
                        @Param("startDate") Date startDate,
                        @Param("endDate") Date endDate);

        @Query("select DISTINCT new com.company.my.blog.dto.PostExcerptDto(p.id, p.title, p.excerpt, p.publishedAt, p.author.id, p.author.name, p.author.email) from Post p, PostTag pt, Tag t " +
                        "where p.id = pt.post and t.tagId = pt.tag and " +
                        "(lower(p.author.name) like lower(concat('%', :searchedValue,'%')) or " +
                        "lower(p.title) like lower(concat('%', :searchedValue,'%')) or " +
                        "lower(p.content) like lower(concat('%', :searchedValue,'%')) or " +
                        "lower(p.excerpt) like lower(concat('%', :searchedValue,'%')) ) and " +
                        "p.author.id in (:authorIds) " +
                        "and (p.publishedAt BETWEEN :startDate AND :endDate)")
        List<PostExcerptDto> findAllPostsBySearchedValueAndAuthorAndDates(
                        @Param("searchedValue") String searchedValue,
                        @Param("authorIds") List<Integer> authorIds,
                        Pageable pageable,
                        @Param("startDate") Date startDate,
                        @Param("endDate") Date endDate);

        @Query("select DISTINCT new com.company.my.blog.dto.PostExcerptDto(p.id, p.title, p.excerpt, p.publishedAt, p.author.id, p.author.name, p.author.email) from Post p, PostTag pt, Tag t " +
                        "where p.id = pt.post and t.tagId = pt.tag and " +
                        "(lower(p.author.name) like lower(concat('%', :searchedValue,'%')) or " +
                        "lower(p.title) like lower(concat('%', :searchedValue,'%')) or " +
                        "lower(p.content) like lower(concat('%', :searchedValue,'%')) or " +
                        "lower(p.excerpt) like lower(concat('%', :searchedValue,'%')) ) and " +
                        "t.tagId in (:tagIds) and " +
                        "(p.publishedAt BETWEEN :startDate AND :endDate)")
        List<PostExcerptDto> findAllPostsBySearchedValueAndTagAndDates(
                        @Param("searchedValue") String searchedValue,
                        @Param("tagIds") List<Integer> tagIds,
                        Pageable pageable,
                        @Param("startDate") Date startDate,
                        @Param("endDate") Date endDate);

        @Query("select DISTINCT new com.company.my.blog.dto.PostExcerptDto(p.id, p.title, p.excerpt, p.publishedAt, p.author.id, p.author.name, p.author.email) from Post p, PostTag pt, Tag t " +
                        "where p.id = pt.post and t.tagId = pt.tag and " +
                        "(lower(p.author.name) like lower(concat('%', :searchedValue,'%')) or " +
                        "lower(p.title) like lower(concat('%', :searchedValue,'%')) or " +
                        "lower(p.content) like lower(concat('%', :searchedValue,'%')) or " +
                        "lower(p.excerpt) like lower(concat('%', :searchedValue,'%')) ) and " +
                        "p.author.id in (:authorIds) and t.tagId in (:tagIds) " +
                        "and (p.publishedAt BETWEEN :startDate AND :endDate)")
        List<PostExcerptDto> findAllPostsBySearchedValueAndAuthorAndTagAndDates(
                        @Param("searchedValue") String searchedValue,
                        @Param("authorIds") List<Integer> authorIds,
                        @Param("tagIds") List<Integer> tagIds,
                        Pageable pageable,
                        @Param("startDate") Date startDate,
                        @Param("endDate") Date endDate);

        @Query("select DISTINCT new com.company.my.blog.dto.PostExcerptDto(p.id, p.title, p.excerpt, p.publishedAt, p.author.id, p.author.name, p.author.email) from Post p, PostTag pt, Tag t " +
                        "where p.id = pt.post and t.tagId = pt.tag and " +
                        "(lower(p.author.name) like lower(concat('%', :searchedValue,'%')) or " +
                        "lower(p.title) like lower(concat('%', :searchedValue,'%')) or " +
                        "lower(p.content) like lower(concat('%', :searchedValue,'%')) or " +
                        "lower(p.excerpt) like lower(concat('%', :searchedValue,'%')) ) and " +
                        "p.author.id in (:authorIds) " +
                        "and (p.publishedAt BETWEEN :startDate AND :endDate)")
        List<PostExcerptDto> findAllPostsBySearchedValueAndAuthorAndDatesAndSorted(
                        @Param("searchedValue") String searchedValue,
                        @Param("authorIds") List<Integer> authorIds,
                        Pageable pageable,
                        @Param("startDate") Date startDate,
                        @Param("endDate") Date endDate);

        @Query("select DISTINCT new com.company.my.blog.dto.PostExcerptDto(p.id, p.title, p.excerpt, p.publishedAt, p.author.id, p.author.name, p.author.email) from Post p, PostTag pt, Tag t " +
                        "where p.id = pt.post and t.tagId = pt.tag and " +
                        "(lower(p.author.name) like lower(concat('%', :searchedValue,'%')) or " +
                        "lower(p.title) like lower(concat('%', :searchedValue,'%')) or " +
                        "lower(p.content) like lower(concat('%', :searchedValue,'%')) or " +
                        "lower(p.excerpt) like lower(concat('%', :searchedValue,'%')) ) and " +
                        "p.author.id in (:authorIds) and t.tagId in (:tagIds) " +
                        "and (p.publishedAt BETWEEN :startDate AND :endDate)")
        List<PostExcerptDto> findAllPostsBySearchedValueAndAuthorAndTagAndDatesAndSorted(
                        @Param("searchedValue") String searchedValue,
                        @Param("authorIds") List<Integer> authorIds,
                        @Param("tagIds") List<Integer> tagIds,
                        Pageable pageable,
                        @Param("startDate") Date startDate,
                        @Param("endDate") Date endDate);
}