package com.zp.blog.service;

import com.zp.blog.domain.Comment;

import java.util.List;

public interface CommentService {

    public List<Comment> selectByBlogIdAndParentCommentIsNull(Integer pageNum, Integer pageSize, Long id);

    public void saveComment(Comment comment);

    List<Comment> selectAll(Integer pageNum, Integer pageSize,  String content, String email, Long blogId);

    void deleteById(Integer id);
}
