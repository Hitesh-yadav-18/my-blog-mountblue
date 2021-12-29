package com.company.my.blog.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.company.my.blog.model.Comment;
import com.company.my.blog.model.Post;
import com.company.my.blog.model.PostTag;
import com.company.my.blog.model.Tag;
import com.company.my.blog.model.User;
import com.company.my.blog.service.CommentService;
import com.company.my.blog.service.PostService;
import com.company.my.blog.service.PostTagService;
import com.company.my.blog.service.TagService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequestMapping(value = "/post")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private PostTagService postTagService;

    @Autowired
    private TagService tagService;

    @Autowired
    private CommentService commentService;

    @GetMapping(value = "/{id}")
    public String getPostById(@PathVariable(value = "id") int id, Model model) {
        Post post = postService.getParticularPost(id);
        List<Comment> comments = commentService.getCommentsByPostId(post);
        List<Tag> tags = new ArrayList<>();
        for (Integer i = 0; i < post.getPostTags().size(); i++) {
            tags.add(post.getPostTags().get(i).getTag());
        }
        model.addAttribute("comments", comments);
        model.addAttribute("post", post);
        model.addAttribute("tags", tags);
        return "post";
    }

    @GetMapping(value = "/create")
    public String getCreatePostPage() {
        return "create";
    }

    @PostMapping(value = "/create/save")
    public String newPostCreate(HttpServletRequest request, @SessionAttribute("currentUser") User user, Model model) {
        String title = request.getParameter("title");
        String excerpt = request.getParameter("excerpt");
        String content = request.getParameter("content");
        String tags = request.getParameter("tagsList");
        if (title != null && excerpt != null && content != null && tags != null) {
            Post savedPostData = postService.createNewPost(title, excerpt, content, user);

            if (savedPostData != null) {
                Set<Tag> tagSet = new HashSet<Tag>();
                String allTags[] = tags.split(",");
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
                    postTag.setPost(savedPostData);
                    postTag.setTag(tag);
                    postTag.setCreatedAt(new Date());
                    postTag.setUpdatedAt(new Date());
                    postTagService.addTagToPost(postTag);
                }
            }
        }
        return "redirect:/post/create";
    }

    @GetMapping(value = "/editPost/{id}")
    public String showPostEditForm(@PathVariable(value = "id") int id, Model model) {
        Post post = postService.getParticularPost(id);
        model.addAttribute("post", post);
        return "edit-post";
    }

    @PostMapping(value = "/postUpdate/{id}")
    public String processUpdatePostById(
            @PathVariable(value = "id") int postId,
            @ModelAttribute(value = "post") Post post) {
        post.setUpdatedAt(new Date());
        postService.updatePostById(postId, post.getTitle(),
                post.getExcerpt(), post.getContent(), post.getUpdatedAt());
        return "redirect:/post/" + postId;
    }

    @DeleteMapping(value = "/postDelete/{id}")
    @ResponseBody
    public String deletePost(@PathVariable(value = "id") Integer id) {
        postService.deletePost(id);
        return "deleted";
    }
}
