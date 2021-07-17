package com.zp.blog.dao;

import com.zp.blog.domain.Comment;
import com.zp.blog.domain.CommentExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CommentMapper {
    long countByExample(CommentExample example);

    int deleteByExample(CommentExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Comment record);

    int insertSelective(Comment record);

    List<Comment> selectByExample(CommentExample example);

    Comment selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Comment record, @Param("example") CommentExample example);

    int updateByExample(@Param("record") Comment record, @Param("example") CommentExample example);

    int updateByPrimaryKeySelective(Comment record);

    int updateByPrimaryKey(Comment record);

    List<Comment> selectByBlogIdAndParentCommentIsNull(Long id);

    Comment selectById(Integer id);

    List<Comment> selectByParentId(Integer parentId);

    List<Comment> selectAll(@Param ("content") String content, @Param ("email") String email, @Param ("blogId") Long blogId);
}