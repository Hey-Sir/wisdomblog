package com.wisdom.blog.service;

import com.wisdom.blog.domain.User;
import com.wisdom.blog.domain.es.EsBlog;
import com.wisdom.blog.vo.TagVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EsBlogService {

    void removeEsBlog(String id);

    EsBlog updateEsBlog(EsBlog esBlog);

    EsBlog getEsBlogByBlogId(Long blogId);

    Page<EsBlog> listNewestEsBlogs(String keyword, Pageable pageable);

    Page<EsBlog> listHotestEsBlogs(String keyword, Pageable pageable);

    Page<EsBlog> listEsBlogs(Pageable pageable);

    List<EsBlog> listTop5NewestEsBlogs();

    List<EsBlog> listTop5HotestEsBlogs();

    List<TagVO> listTop30Tags();

    List<User> listTop12Users();
}
