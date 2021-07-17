package com.zp.blog.dao;

import com.zp.blog.domain.Tag;
import com.zp.blog.domain.TagExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TagMapper {
    long countByExample(TagExample example);

    int deleteByExample(TagExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Tag record);

    int insertSelective(Tag record);

    List<Tag> selectByExample(TagExample example);

    Tag selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Tag record, @Param("example") TagExample example);

    int updateByExample(@Param("record") Tag record, @Param("example") TagExample example);

    int updateByPrimaryKeySelective(Tag record);

    int updateByPrimaryKey(Tag record);

    int selectCountByTagIdForBlogAndTag(Integer id);

    List<Tag> selectAllWithCount();

    List<Tag> selectByOther(int count); //指定博客的标签不足10个时，获取count个其它没有指定博客的标签

    List<Tag> selectByOtherTags(); //获取其它没有指定博客的所有标签
}