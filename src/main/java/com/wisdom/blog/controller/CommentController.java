package com.wisdom.blog.controller;

import com.wisdom.blog.domain.Blog;
import com.wisdom.blog.domain.Comment;
import com.wisdom.blog.domain.User;
import com.wisdom.blog.service.BlogService;
import com.wisdom.blog.service.CommentService;
import com.wisdom.blog.util.ConstraintViolationExceptionHandler;
import com.wisdom.blog.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.List;

@Controller
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private CommentService commentService;

    @GetMapping
    public String listComment(@RequestParam(value = "blogId",required = true) Long blogId, Model model){
        Blog blog = blogService.getBlogById(blogId);
        List<Comment> comments = blog.getComments();

        String commentOwner = "";
        if(SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
               && !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser") ){
            User princiPal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(princiPal != null){
                commentOwner = princiPal.getUsername();
            }
        }
        model.addAttribute("commentOwner",commentOwner);
        model.addAttribute("comments",comments);
        return "/userspace/blog :: #mainContainerRepleace";
    }
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<Response> createComment(Long blogId,String commentContent){
        try {
            blogService.createComment(blogId,commentContent);
        }catch (ConstraintViolationException e){
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        }catch (Exception e){
            return ResponseEntity.ok().body(new Response(true,e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "处理成功", null));

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteBlog(@PathVariable(value = "id")Long id,Long blogId){
        boolean isOwner = false;
        User user = commentService.getCommentById(id).getUser();

        if(SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                && !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")){
            User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(principal != null && user.getUsername().equals(principal.getUsername())){
                isOwner = true;
            }
        }

        if(!isOwner){
            return ResponseEntity.ok().body(new Response(false,"没有操作权限"));
        }
        try {
            blogService.removeComment(blogId, id);
            commentService.removeComment(id);
        } catch (ConstraintViolationException e)  {
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }

        return ResponseEntity.ok().body(new Response(true, "处理成功", null));
    }
}
