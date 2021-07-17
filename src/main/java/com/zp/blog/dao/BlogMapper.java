package com.zp.blog.dao;

import com.zp.blog.domain.Blog;
import com.zp.blog.domain.BlogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BlogMapper {
    long countByExample(BlogExample example);

    int deleteByExample(BlogExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Blog record);

    int insertSelective(Blog record);

    List<Blog> selectByExampleWithBLOBs(BlogExample example);

    List<Blog> selectByExample(BlogExample example);

    Blog selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Blog record, @Param("example") BlogExample example);

    int updateByExampleWithBLOBs(@Param("record") Blog record, @Param("example") BlogExample example);

    int updateByExample(@Param("record") Blog record, @Param("example") BlogExample example);

    int updateByPrimaryKeySelective(Blog record);

    int updateByPrimaryKeyWithBLOBs(Blog record);

    int updateByPrimaryKey(Blog record);

    List<Blog> selectAll(@Param ("title") String title, @Param ("typeId") Integer typeId, @Param ("recommend") Boolean recommend);

    void insertBlogAndTag(@Param ("tagId") Integer tagId, @Param ("blogId") Long blogId);

    void deleteByBlogIdForTagAndBlog(Long id);

    Blog selectById(Long id);

    int selectCountByTypeId(Integer typeId);

    List<Blog> selectAllFront();

    List<Blog> selectByRecommend();

    List<Blog> search(String searchContent);

    Blog selectByIdOfFront(Long id);

    void updateViews(@Param ("id") Long id, @Param ("views") int views);

    List<Blog> selectByTypeId(Integer typeId);

    List<Blog> selectByTagId(Integer tagId);

    List<String> selectYears();

    List<Blog> selectBlogsByYear(String year);

    //根据查看的次数降序获取4个
    List<Blog> selectHotBlogs();
}