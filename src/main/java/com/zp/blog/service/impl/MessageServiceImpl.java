package com.zp.blog.service.impl;

import com.github.pagehelper.PageHelper;
import com.zp.blog.dao.MessageMapper;
import com.zp.blog.domain.Message;
import com.zp.blog.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service("messageService")
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Override
    public void saveMessage(Message message) {
        messageMapper.insert (message);
    }

    @Override
    public List<Message> selectAll(Integer pageNum, Integer pageSize) {
        PageHelper.startPage (pageNum, pageSize);

        return messageMapper.selectAllByParentMessageIdIsNull(); //分页获取所有不要在这调用getAll()方法，在controller层用封装好的pageInfo的list作为参数调用
    }

    @Override
    public void deleteById(Integer id) {
        messageMapper.deleteByPrimaryKey (id);
    }

    @Override
    public List<Message> selectAllWithUser(Integer pageNum, Integer pageSize, String content, String email) {
        content = "%" + content + "%";
        email = "%" + email + "%";

        PageHelper.startPage (pageNum, pageSize);
        List<Message> messageList = messageMapper.selectAllWithUser (content, email);
        for (Message message : messageList) {
            if (message.getContent ().length () > 25){
                message.setContent (message.getContent ().substring (0,25)+"...");
            } else {
                message.setContent (message.getContent () + "...");
            }
        }
        return messageList;
    }

    private List<Message> replyMessageList = new ArrayList<> (); //用于保存每个顶级留言的后代留言，封装完一个之后记得清空
    //接收顶级留言，封装每个顶级留言的后代留言
    public List<Message> getAll(List<Message> topMessage){
        List<Message> messages = new ArrayList<> ();
        for (Message message : topMessage) {
            getChildMessage(message.getId (), message);  //根据parent_message_id获取所有后代留言
            message.setReplyList (replyMessageList); //设置这个顶级评论的后代评论
            messages.add (message); //获取到所有后代留言，封装进顶级留言
            replyMessageList = new ArrayList<> (); //清空
        }

        return messages;
    }

    private void getChildMessage(Integer id, Message parentMessage) {
        List<Message> messages = messageMapper.selectByParentId(id);  //根据parent_message_id获取Message
        if (messages == null ){  //为空，下面已经没有后代留言
            return; //返回上一级
        }

        for (Message message : messages) {
            message.setParentMessage (parentMessage);  //设置该留言的父留言
            replyMessageList.add (message); //将每个后代留言放进对于顶级留言的replyList中
            getChildMessage(message.getId (), message); //递归继续获取后代的后代下面所有的留言
        }

    }
}
