package com.wisdom.blog.service;

import com.wisdom.blog.domain.*;
import com.wisdom.blog.domain.es.EsBlog;
import com.wisdom.blog.repository.BlogRepository;
import com.wisdom.blog.repository.es.EsBlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private EsBlogService esBlogService;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private EsBlogRepository esBlogRepository;
    @Override
    public Blog saveBlog(Blog blog) {
        boolean isNew = (blog.getId() == null);
        EsBlog esBlog = null;
        Blog returnBlog = blogRepository.save(blog);
        if(isNew){
            esBlog = new EsBlog(returnBlog);
        }else {
            esBlog = esBlogService.getEsBlogByBlogId(blog.getId());
            esBlog.update(blog);
        }
        esBlogService.updateEsBlog(esBlog);
        return returnBlog;
    }

    @Override
    public void removeBlog(Long id) {
        blogRepository.deleteById(id);
        EsBlog esBlog = esBlogService.getEsBlogByBlogId(id);
        esBlogService.removeEsBlog(esBlog.getId());
    }

    @Override
    public Blog updateBlog(Blog blog) {
        return blogRepository.save(blog);
    }

    @Override
    public Blog getBlogById(Long id) {
        return blogRepository.getOne(id);
    }

    @Override
    public Page<Blog> listBlogsByTitleLike(User user, String title, Pageable pageable) {
        // 模糊查询
        title = "%" + title + "%";
        Page<Blog> blogs = blogRepository.findByUserAndTitleLikeOrderByCreateTimeDesc(user, title, pageable);
        return blogs;
    }

    @Override
    public Page<Blog> listBlogsByTitleLikeAndSort(User user, String title, Pageable pageable) {
        // 模糊查询
        title = "%" + title + "%";
        Page<Blog> blogs = blogRepository.findByUserAndTitleLike(user, title, pageable);
        return blogs;
    }

    @Override
    public void readingIncrease(Long id) {
        Blog blog = blogRepository.getOne(id);
        blog.setReadSize(blog.getReadSize() + 1);
        blogRepository.save(blog);
        EsBlog esBlog = esBlogService.getEsBlogByBlogId(blog.getId());
        esBlog.setReadSize(esBlog.getReadSize() + 1);
        esBlogRepository.save(esBlog);
    }

    @Override
    public Blog createComment(Long blogId, String commentContent) {
        Blog originalBlog = blogRepository.findById(blogId).get();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Comment comment = new Comment(user,commentContent);
        originalBlog.addComment(comment);

        EsBlog esBlog = esBlogService.getEsBlogByBlogId(blogId);
        esBlog.setCommentSize(originalBlog.getCommentSize());
        esBlogRepository.save(esBlog);

        return blogRepository.save(originalBlog);
    }

    @Override
    public void removeComment(Long blogId, Long commentId) {
        Blog originalBlog = blogRepository.getOne(blogId);
        originalBlog.removeComment(commentId);
        blogRepository.save(originalBlog);

        EsBlog esBlog = esBlogService.getEsBlogByBlogId(blogId);
        esBlog.setCommentSize(originalBlog.getCommentSize());
        esBlogRepository.save(esBlog);
    }

    @Override
    public Blog createVote(Long blogId) {
        Blog originalBlog = blogRepository.getOne(blogId);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Vote vote = new Vote(user);
        boolean isExist = originalBlog.addVote(vote);
        if(isExist){
            throw  new IllegalArgumentException("该用户已点过赞");
        }

        EsBlog esBlog = esBlogService.getEsBlogByBlogId(blogId);
        esBlog.setVoteSize(originalBlog.getVoteSize());
        return this.saveBlog(originalBlog);
    }

    @Override
    public void removeVote(Long blogId, Long voteId) {
        Blog originalBlog = blogRepository.getOne(blogId);
        originalBlog.removeVote(voteId);

        EsBlog esBlog = esBlogService.getEsBlogByBlogId(blogId);
        esBlog.setVoteSize(originalBlog.getVoteSize());
        this.saveBlog(originalBlog);
    }

    @Override
    public Page<Blog> listBlogsByCatalog(Catalog catalog, Pageable pageable) {
        Page<Blog> blogs = blogRepository.findByCatalog(catalog, pageable);
        return blogs;
    }

}
