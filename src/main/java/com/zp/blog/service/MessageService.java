package com.zp.blog.service;

import com.zp.blog.domain.Comment;
import com.zp.blog.domain.Message;

import java.util.List;

public interface MessageService {

    public void saveMessage(Message message);

    public List<Message> selectAll(Integer pageNum, Integer pageSize);

    void deleteById(Integer id);

    List<Message> selectAllWithUser(Integer pageNum, Integer pageSize, String content, String email);
}
