package com.company.my.blog.service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.company.my.blog.model.Post;
import com.company.my.blog.model.PostTag;
import com.company.my.blog.model.Tag;
import com.company.my.blog.repository.PostTagRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PostTagService {

    @Autowired
    private PostTagRepository postTagRepository;

    @Autowired
    private TagService tagService;

    

    public void splitTagsAndSavePostTags(String tags, Post post) {
        Set<Tag> tagSet = new HashSet<Tag>();
                String[] allTags = tags.split(",");
                for (String tagName : allTags) {
                    tagName = tagName.trim();
                    if (tagName.length() > 0) {
                        Tag tag = tagService.getTagByName(tagName);
                        if (tag == null) {
                            tag = new Tag();
                            tag.setTagName(tagName);
                            tag.setCreatedAt(new Date());
                            tag.setUpdatedAt(new Date());
                            tag = tagService.addTagToPost(tag);
                        }
                        tagSet.add(tag);
                    }
                }

                for (Tag tag : tagSet) {
                    PostTag postTag = new PostTag();
                    postTag.setPost(post);
                    postTag.setTag(tag);
                    postTag.setCreatedAt(new Date());
                    postTag.setUpdatedAt(new Date());
                    addTagsPostEntries(postTag);
                    System.out.println(postTag);
                }
    }

    public void addTagsPostEntries(PostTag postTag) {
        postTagRepository.save(postTag);
    }

   
    public void deleteAllPostTagsByPostId(Post post) {
        postTagRepository.deleteAllPostTagsByPostId(post);
    }

}
