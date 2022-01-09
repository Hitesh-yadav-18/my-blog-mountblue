package com.company.my.blog.dto;


import java.util.Date;
import java.util.List;

import com.company.my.blog.model.Comment;
import com.company.my.blog.model.Tag;

public class PostDto {
    private Integer id;
    private String title;
    private String excerpt;
    private String content;
    private UserDto author;
    private Date publishedAt;

    List<Comment> comments;
    List<Tag> tags;
    
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
    public List<Comment> getComments() {
        return comments;
    }
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
    public List<Tag> getTags() {
        return tags;
    }
    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    
    
    
}
