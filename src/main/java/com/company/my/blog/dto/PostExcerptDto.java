package com.company.my.blog.dto;

import java.util.Date;
import java.util.List;

public class PostExcerptDto {
    private Integer id;
    private String title;
    private String excerpt;
    private Date publishedAt;
    private UserDto author;

    List<TagDto> tags;

    public PostExcerptDto(Integer id, String title, String excerpt, Date publishedAt, Integer authorId, String authorName, String authorEmail) {
        this.id = id;
        this.title = title;
        this.excerpt = excerpt;
        this.publishedAt = publishedAt;
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

    public Date getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Date publishedAt) {
        this.publishedAt = publishedAt;
    }

    public UserDto getAuthor() {
        return author;
    }

    public void setAuthor(UserDto author) {
        this.author = author;
    }

    public List<TagDto> getTags() {
        return tags;
    }

    public void setTags(List<TagDto> tags) {
        this.tags = tags;
    }
    
    

    
}
