package com.company.my.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/")
public class BlogController {

    @RequestMapping(value = "")
    public String index() {
        return "index";
    }

    @RequestMapping(value = "blog/{id}")
    public String blogView() {
        return "post";
    }

    @RequestMapping(value = "blog/create")
    public String blogCreate() {
        return "create";
    }



}
