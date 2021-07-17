package com.zp.blog.web.front;

import com.github.pagehelper.PageInfo;
import com.zp.blog.domain.Comment;
import com.zp.blog.domain.UserInfo;
import com.zp.blog.service.CommentService;
import com.zp.blog.service.impl.CommentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/front/comment")
public class FrontCommentController {

    @Autowired
    private CommentService commentService;

    @RequestMapping("/addComment")
    public String addComment(Comment comment){

        //获取登录的用户
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext ().getAuthentication ().getPrincipal ();
        comment.setUserInfo (userInfo);
        comment.setCreateTime (new Date ());
        if (comment.getParentComment ().getId () == -1){
            comment.getParentComment ().setId (null);
        }

        commentService.saveComment (comment);
        return "redirect:showAllCommentByBlogId?id="+comment.getBlog ().getId ();
    }

    @RequestMapping("/showAllCommentByBlogId")
    public String showAllCommentByBlogId(@RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                         @RequestParam(name = "pageSize", required = false, defaultValue = "6") Integer pageSize,
                                         @RequestParam(name = "id") Long id, Model model){

        List<Comment> comments = commentService.selectByBlogIdAndParentCommentIsNull (pageNum, pageSize, id);
        PageInfo<Comment> pageInfo = new PageInfo<> (comments,8);

        CommentServiceImpl commentServiceImpl = (CommentServiceImpl) commentService;
        List<Comment> commentList = commentServiceImpl.getAll (pageInfo.getList ());  //获取后代评论
        pageInfo.setList (commentList); //封装好的顶级评论和后代评论设置给pageInfo的list

        model.addAttribute ("pageInfo",pageInfo);

        return "blog :: commentList";
    }
}
