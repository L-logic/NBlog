package com.zp.blog.service.impl;

import com.github.pagehelper.PageHelper;
import com.zp.blog.dao.CommentMapper;
import com.zp.blog.domain.Comment;
import com.zp.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("commentService")
@Transactional
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public List<Comment> selectByBlogIdAndParentCommentIsNull(Integer pageNum, Integer pageSize, Long id) {
        //分页
        PageHelper.startPage (pageNum, pageSize);
        //获取顶级评论的集合（也就是parent_comment_id 字段为 null的）
        List<Comment> comments = commentMapper.selectByBlogIdAndParentCommentIsNull (id);
//        comments = getAll (comments);  //获取所有，封装后代评论  要想分页，不能再这操作，得操作pageInfo.getList
        return comments;
    }

    private List<Comment> replyList = new ArrayList<> (); //定义用于临时存储每个顶级评论下的所有后代评论的replyList，存储完一个之后记得清空

    //获取所有评论，把顶级评论下的评论封装到顶级评论的replyList中
    public List<Comment> getAll(List<Comment> comments){
        List<Comment> commentList = new ArrayList<> ();  //用于保存封装完的顶级评论（给顶级评论封装后代评论）
        for (Comment comment : comments) {
            //根据顶级评论的id查找所有属于他下面的后代评论
            getReplyList(comment.getId (),comment);  //这个方法会封装后代评论到replyList中
            comment.setReplyList (replyList); //设置这个顶级评论的后代评论
            commentList.add (comment);  //把封装好的这个顶级评论加到commentList中
            replyList = new ArrayList<> (); //清空已经给这个顶级评论封装后代评论，便于下个存储
        }

        return commentList;
    }

    //用于递归获取顶级评论为id下的所有后代评论
    private void getReplyList(Integer id, Comment parentComment) {


        List<Comment> commentList = commentMapper.selectByParentId (id); //根据parent_comment_id获取Comment

        if (commentList == null){  //为空，下面已经没有后代评论
            return;  //返回上一层
        }
        //获取后代评论
        for (Comment comment : commentList) {
            comment.setParentComment (parentComment); //设置该评论的父评论
            replyList.add (comment); //将每个后代评论放进对于顶级评论的replyList中
            getReplyList(comment.getId (),comment); //递归继续获取后代的后代下面所有的评论
        }

    }



    @Override
    public void saveComment(Comment comment) {
        commentMapper.insert (comment);
    }

    @Override
    public List<Comment> selectAll(Integer pageNum, Integer pageSize,  String content, String email, Long blogId) {
        content = "%" + content + "%";
        email = "%" + email + "%";

        PageHelper.startPage (pageNum, pageSize);
        List<Comment> commentList = commentMapper.selectAll (content, email, blogId);
        for (Comment comment : commentList) {
            if (comment.getContent ().length () > 25){
                comment.setContent (comment.getContent ().substring (0,25)+"...");
            } else {
                comment.setContent (comment.getContent () + "...");
            }
        }
        return commentList;
    }

    @Override
    public void deleteById(Integer id) {
        commentMapper.deleteByPrimaryKey (id);
    }
}
