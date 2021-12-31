package com.company.my.blog.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    @Column(length = 1000)
    private String excerpt;
    @Column(length = 20000)
    private String content;

    @ManyToOne
    @JoinColumn(name = "author", referencedColumnName = "id")
    private User author;
    private Date publishedAt;
    private boolean isPublished;
    private Date createdAt;
    private Date updatedAt;

    @OneToMany(mappedBy = "post")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<PostTag> postTags;

    @OneToMany(mappedBy = "postId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Comment> comments;


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

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Date getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Date publishedAt) {
        this.publishedAt = publishedAt;
    }

    public boolean isPublished() {
        return isPublished;
    }

    public void setPublished(boolean published) {
        isPublished = published;
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
    

    public List<PostTag> getPostTags() {
        return postTags;
    }

    public void setPostTags(List<PostTag> postTags) {
        this.postTags = postTags;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Post [author=" + author + ", content=" + content + 
                ", createdAt=" + createdAt + ", excerpt=" + excerpt
                + ", id=" + id + ", isPublished=" + isPublished + 
                ", publishedAt=" + publishedAt + ", title=" + title +
                 ", updatedAt=" + updatedAt + "]";
    }

}