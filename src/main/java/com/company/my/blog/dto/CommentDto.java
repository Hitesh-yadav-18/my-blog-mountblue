package com.company.my.blog.dto;

import java.util.Date;

public class CommentDto{
    private int id;
    private String name;
    private String email;
    private String comment;
    private Date createdAt;
    private Date updatedAt;
    private Integer postId;

    public CommentDto(int id, String name, String email, String comment, Date createdAt, Date updatedAt, Integer postId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.comment = comment;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.postId = postId;
    }

   
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getPostId() {
        return postId;
    }


    public void setPostId(Integer postId) {
        this.postId = postId;
    }
    

    
    
}