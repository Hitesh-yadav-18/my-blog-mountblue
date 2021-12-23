package com.company.my.blog.Service;

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

}
