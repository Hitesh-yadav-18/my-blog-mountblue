package com.company.my.blog.Service;

import com.company.my.blog.model.PostTag;
import com.company.my.blog.repository.PostTagRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostTagService {

    @Autowired
    private PostTagRepository postTagRepository;

    public void addTagToPost(PostTag postTag) {
        postTagRepository.save(postTag);
    }
}
