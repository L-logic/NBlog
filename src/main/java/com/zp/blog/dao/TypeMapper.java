package com.zp.blog.dao;

import com.zp.blog.domain.Type;
import com.zp.blog.domain.TypeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TypeMapper {
    long countByExample(TypeExample example);

    int deleteByExample(TypeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Type record);

    int insertSelective(Type record);

    List<Type> selectByExample(TypeExample example);

    Type selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Type record, @Param("example") TypeExample example);

    int updateByExample(@Param("record") Type record, @Param("example") TypeExample example);

    int updateByPrimaryKeySelective(Type record);

    int updateByPrimaryKey(Type record);

    List<Type> selectAllWithCount();

    List<Type> selectByOther(int count);  //获取指定条数其它类别下面博客数为0的类别

    List<Type> selectByOtherTypes(); //获取其它所有类别下面博客数为0的类别

}