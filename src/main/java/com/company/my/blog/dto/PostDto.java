package com.company.my.blog.dto;


import java.util.Date;
import java.util.List;

public class PostDto {
    private Integer id;
    private String title;
    private String excerpt;
    private String content;
    private Date publishedAt;
    private Date createdAt;
    private UserDto author;

    List<CommentDto> comments;
    List<TagDto> tags;

    public PostDto(Integer id, String title, String excerpt, String content, Date publishedAt,
        Date createdAt, Integer authorId, String authorName, String authorEmail) {
        this.id = id;
        this.title = title;
        this.excerpt = excerpt;
        this.content = content;
        this.publishedAt = publishedAt;
        this.createdAt = createdAt;
        this.author = new UserDto(authorId, authorName, authorEmail);
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getExcerpt() {
        return excerpt;
    }
    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public UserDto getAuthor() {
        return author;
    }
    public void setAuthor(UserDto author) {
        this.author = author;
    }
    public Date getPublishedAt() {
        return publishedAt;
    }
    public void setPublishedAt(Date publishedAt) {
        this.publishedAt = publishedAt;
    }
    public Date getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    public List<CommentDto> getComments() {
        return comments;
    }
    public void setComments(List<CommentDto> comments) {
        this.comments = comments;
    }
    public List<TagDto> getTags() {
        return tags;
    }
    public void setTags(List<TagDto> tags) {
        this.tags = tags;
    }   
    
}
