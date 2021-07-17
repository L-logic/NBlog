package com.zp.blog.domain;

import java.util.Date;
import java.util.List;

public class Message {
    private Integer id;

    private String content;

    private Date createTime;

    private Message parentMessage;

    private UserInfo userInfo;

    private List<Message> replyList; //用于封装顶级留言下面的所有后代

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Message getParentMessage() {
        return parentMessage;
    }

    public void setParentMessage(Message parentMessage) {
        this.parentMessage = parentMessage;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public List<Message> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<Message> replyList) {
        this.replyList = replyList;
    }
}