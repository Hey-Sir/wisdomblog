package com.wisdom.blog.repository;

import com.wisdom.blog.domain.Blog;
import com.wisdom.blog.domain.Catalog;
import com.wisdom.blog.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BlogRepository extends JpaRepository<Blog,Long> {

    Page<Blog> findByUserAndTitleLikeOrderByCreateTimeDesc(User user, String title, Pageable pageable);

    Page<Blog> findByUserAndTitleLike(User user,String title,Pageable pageable);

    Page<Blog> findByCatalog(Catalog catalog, Pageable pageable);
}
