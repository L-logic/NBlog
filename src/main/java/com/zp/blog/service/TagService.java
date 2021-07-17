package com.zp.blog.service;

import com.zp.blog.domain.Tag;
import com.zp.blog.domain.Type;

import java.util.List;

public interface TagService {

    //查询名称模糊查询所有
    public List<Tag> selectAllByLikeName(Integer pageNum, Integer pageSize, String name);

    //根据id查询
    public Tag selectById(Integer id);

    //增加标签
    public Boolean save(Tag tag);

    //修改标签
    public Boolean update(Tag tag);

    //根据id删除标签
    public Boolean deleteById(Integer id);

    //根据name查询
    public Tag selectByName(String name);

    //查询所有标签
    List<Tag> selectAll();

    //获取前10条标签博客排行
    List<Tag> selectAllWithCount();

    //获取所有标签博客排行
    List<Tag> selectAllTagsWithCount();
}
