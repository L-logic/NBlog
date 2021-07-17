package com.zp.blog.service;

import com.zp.blog.domain.Type;

import java.util.List;

public interface TypeService {

    //查询名称模糊查询所有
    public List<Type> selectAllByLikeName(Integer pageNum, Integer pageSize, String name);

    //根据id查询
    public Type selectById(Integer id);

    //增加类别
    public Boolean save(Type type);

    //修改类别
    public Boolean update(Type type);

    //根据id删除类别
    public Boolean deleteById(Integer id);

    //根据name查询
    public Type selectByName(String name);

    List<Type> selectAll();

    //获取6条类别，并用属于该类别的博客的数量排序
    List<Type> selectAllWithCount();

    //获取所有类别，并用属于该类别的博客的数量排序
    List<Type>  selectAllTypesWithCount();
}
