package com.zp.blog.service;

import com.github.pagehelper.PageInfo;
import com.zp.blog.domain.Blog;

import java.util.List;
import java.util.Map;

public interface BlogService {
    //根据标题模糊查询、类别id和是否推荐查询所有进行分页
    public List<Blog> selectAll(Integer pageNum, Integer pageSize,String title, Integer typeId, Boolean recommend);

    //前台查询所有
    public PageInfo<Blog> selectAll(Integer pageNum, Integer pageSize);

    public Blog selectById(Long id);  //后台根据博客id查询（用于编辑）

    public Boolean save(Blog blog);

    public Boolean update(Blog blog);

    public Boolean deleteById(Long id);

    List<Blog> selectByRecommend();

    List<Blog> search(Integer pageNum, Integer pageSize, String searchContent);

    Blog selectByIdOfFront(Long id);  //前台根据博客id查询（用于展示）

   PageInfo<Blog> selectByTypeId(Integer pageNum, Integer pageSize, Integer id); //根据类别id获取博客

    PageInfo<Blog> selectByTagId(Integer pageNum, Integer pageSize, Integer id); //根据标签id获取博客

    Map<String, List<Blog>> selectAllArchives(); //按年份归档博客

    Integer count();

    List<Blog> selectHotBlogs();
}
