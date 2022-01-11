package com.company.my.blog.service;

import java.util.List;

import com.company.my.blog.dto.PostDto;
import com.company.my.blog.dto.PostExcerptDto;
import com.company.my.blog.dto.TagDto;
import com.company.my.blog.model.Tag;
import com.company.my.blog.repository.TagRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    public Tag addTagToPost(Tag tag) {
        return tagRepository.save(tag);
    }

    public Tag getTagByName(String tag) {
        return tagRepository.findByTagName(tag);
    }

    public List<TagDto> getTagsName(PostExcerptDto postExcerptDto) {
        return tagRepository.findTagsByPostId(postExcerptDto.getId());
    }

    public List<TagDto> getTagsName(PostDto postDto) {
        return tagRepository.findTagsByPostId(postDto.getId());
    }

    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    public List<Tag> getAllTagsOfSelectedAuthor(List<Integer> authorId) {
        return tagRepository.findAllTagsOfSelectedAuthor(authorId);
    }

    

}
