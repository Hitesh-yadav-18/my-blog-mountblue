package com.company.my.blog.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "tag")
public class Tag {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer tagId;
    @Column(name = "tag_name", nullable = false, unique = true)
    private String tagName;
    private Date createdAt;
    private Date updatedAt;
   
    public Integer getTagId() {
        return tagId;
    }
    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }
    public String getTagName() {
        return tagName;
    }
    public void setTagName(String tagName) {
        this.tagName = tagName;
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
    @Override
    public String toString() {
        return "Tag [createdAt=" + createdAt + ", posts=" + posts + ", tagId=" + tagId + ", tagName=" + tagName
                + ", updatedAt=" + updatedAt + "]";
    }

    
}