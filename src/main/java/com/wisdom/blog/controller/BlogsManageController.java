package com.wisdom.blog.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/blogsmanage")
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
public class BlogsManageController {

    @GetMapping
    public ModelAndView view(Model model){
        return new ModelAndView("blogmanage/index","blogmodel",model);
    }

}
