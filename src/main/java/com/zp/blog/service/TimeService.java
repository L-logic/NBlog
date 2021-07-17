package com.zp.blog.service;

import com.zp.blog.domain.Time;

import java.util.List;

public interface TimeService {

    List<Time> selectAll(Integer pageNum, Integer pageSize);

    Time selectById(Integer id);

    public void save(Time time);

    public void deleteById(Integer id);

    public void update(Time time);

    List<Time> selectAllFront();
}
