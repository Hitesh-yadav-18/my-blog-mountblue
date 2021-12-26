package com.company.my.blog.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.company.my.blog.Service.CommentService;
import com.company.my.blog.Service.PostService;
import com.company.my.blog.Service.PostTagService;
import com.company.my.blog.Service.TagService;
import com.company.my.blog.Service.UserService;
import com.company.my.blog.model.Comment;
import com.company.my.blog.model.Post;
import com.company.my.blog.model.PostTag;
import com.company.my.blog.model.Tag;
import com.company.my.blog.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
    private UserService userService;

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
    public String newPostCreate(HttpServletRequest request, Model model) {
        String title = request.getParameter("title");
        String excerpt = request.getParameter("excerpt");
        String content = request.getParameter("content");
        String tags = request.getParameter("tagsList");
        if (title != null && excerpt != null && content != null && tags != null) {
            Post savedPostData = postService.createNewPost(title, excerpt, content);

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
        } else {
            model.addAttribute("error", "Please insert all fields");
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

    @GetMapping(value = "/mypost", params = {})
    public String getAuthorFilterOnPosts(
        @RequestParam(value= "author", required = false, defaultValue="-1") List<Integer> authorId,
            @RequestParam(value="tagId", required = false, defaultValue = "-1") List<Integer> tagIds,
        Model model) {
        User user = new User();
        user = userService.getUserById(1);
        List<Integer> userIds = new ArrayList<>();
        userIds.add(user.getId());
        List<Post> posts = postService.getPostByAuthor(userIds,0,4);
        Map<Post, List<String>> postTagMap = postService.getPostsWithTagsAsHashMap(posts);
        Set<Integer> authorIdsSet = new HashSet<>(authorId);        
        Set<Integer> tagIdsSet = new HashSet<>(tagIds);
        
        model.addAttribute("authorIdsSet", authorIdsSet);
        model.addAttribute("tagIdsSet", tagIdsSet);
        model.addAttribute("postTagMap", postTagMap);
        model.addAttribute("authors", userService.getAllUsers());
        model.addAttribute("tags", tagService.getAllTags());
        return "index";
    }

    @GetMapping(value = "/mypost", params = { "sortField", "order" })
    public String getSortPostsByPublishedDateDesc(
            @RequestParam(value = "sortField") String sortField,
            @RequestParam("order") String order,
            @RequestParam(value= "author", required = false, defaultValue="-1") List<Integer> authorIdList,
            @RequestParam(value="tagId", required = false, defaultValue = "-1") List<Integer> tagIds, 
            Model model) {
        int authorId = userService.getUserById(1).getId();
        List<Post> posts = null;
        if (order.equals("desc")) {
            posts = postService.getAllPostsByAuthorInPublishedDateDesc(authorId);
        } else if (order.equals("asc")) {
            posts = postService.getAllPostsByAuthorInPublishedDateAsc(authorId);
        }
        Map<Post, List<String>> postTagMap = postService.getPostsWithTagsAsHashMap(posts);
        Set<Integer> authorIdsSet = new HashSet<>(authorIdList);
        Set<Integer> tagIdsSet = new HashSet<>(tagIds);
        model.addAttribute("authorIdsSet", authorIdsSet);
        model.addAttribute("tagIdsSet", tagIdsSet);
        model.addAttribute("postTagMap", postTagMap);
        model.addAttribute("authors", userService.getAllUsers());
        model.addAttribute("tags", tagService.getAllTags());
        return "index";
    }

}
