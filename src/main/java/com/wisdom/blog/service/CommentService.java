package com.wisdom.blog.service;

import com.wisdom.blog.domain.Comment;

public interface CommentService {

    Comment getCommentById(Long id);

    void removeComment(Long id);
}
