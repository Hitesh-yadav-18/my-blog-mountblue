package com.company.my.blog.model;

import javax.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String excerpt;
    @Column(length = 20000)
    private String content;
    private String author;
    private Date publishedAt;
    private boolean isPublished;
    private Date createdAt;
    private Date updatedAt;

    @ManyToMany(
        cascade = CascadeType.ALL,
        fetch = FetchType.EAGER
        )
    @JoinTable(
        name = "post_tag",
        joinColumns = @JoinColumn(name = "post_id",referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id",referencedColumnName = "tagId")
        )
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
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

    @Override
    public String toString() {
        return "Post [author=" + author + ", content=" + content + ", createdAt=" + createdAt + ", excerpt=" + excerpt
                + ", id=" + id + ", isPublished=" + isPublished + ", publishedAt=" + publishedAt + ", tags=" + tags
                + ", title=" + title + ", updatedAt=" + updatedAt + "]";
    }

}