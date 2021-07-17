package com.zp.blog.dao;

import com.zp.blog.domain.Time;
import com.zp.blog.domain.TimeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TimeMapper {
    long countByExample(TimeExample example);

    int deleteByExample(TimeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Time record);

    int insertSelective(Time record);

    List<Time> selectByExample(TimeExample example);

    Time selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Time record, @Param("example") TimeExample example);

    int updateByExample(@Param("record") Time record, @Param("example") TimeExample example);

    int updateByPrimaryKeySelective(Time record);

    int updateByPrimaryKey(Time record);
}