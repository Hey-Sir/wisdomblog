package com.wisdom.blog.service;

import com.wisdom.blog.domain.Blog;
import com.wisdom.blog.domain.Catalog;
import com.wisdom.blog.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BlogService {

    Blog saveBlog(Blog blog);

    void removeBlog(Long id);

    Blog updateBlog(Blog blog);

    Blog getBlogById(Long id);

    /**
     * 根据用户名进行分页模糊查询（最新）
     * @param user
     * @return
     */
    Page<Blog> listBlogsByTitleLike(User user, String title, Pageable pageable);

    /**
     * 根据用户名进行分页模糊查询（最热）
     * @param user
     * @return
     */
    Page<Blog> listBlogsByTitleLikeAndSort(User suser, String title, Pageable pageable);

    void readingIncrease(Long id);

    Blog createComment(Long blogId,String commentContent);

    void removeComment(Long blogId,Long commentId);

    Blog createVote(Long blogId);

    void removeVote(Long blogId,Long voteId);

    Page<Blog> listBlogsByCatalog(Catalog catalog,Pageable pageable);
}
