package com.zp.blog.service;

import com.zp.blog.domain.Link;

import java.util.List;

public interface LinkService {

    public List<Link> selectAll(Integer pageNum, Integer pageSize, String name);

    public Link selectById(Integer id);

    public void save(Link link);

    public void deleteById(Integer id);

    public void update(Link link);

    List<Link> selectAllFront();
}
