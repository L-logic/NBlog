package com.zp.blog.service.impl;

import com.github.pagehelper.PageHelper;
import com.zp.blog.dao.LinkMapper;
import com.zp.blog.domain.Link;
import com.zp.blog.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service("linkService")
@CacheConfig(cacheNames = "link", keyGenerator = "myKeyGenerator", cacheManager = "objCacheManager") //全局指定一些公共的属性
public class LinkServiceImpl implements LinkService {

    @Autowired
    private LinkMapper linkMapper;

    @Override
    public List<Link> selectAll(Integer pageNum, Integer pageSize, String name) {
        name = "%" + name + "%";

        PageHelper.startPage (pageNum, pageSize);
        return linkMapper.selectAll (name);
    }

    @Override
    public Link selectById(Integer id) {
        return linkMapper.selectByPrimaryKey (id);
    }

    @CacheEvict(allEntries = true)
    @Override
    public void save(Link link) {
        linkMapper.insert (link);
    }

    @CacheEvict(allEntries = true)
    @Override
    public void deleteById(Integer id) {
        linkMapper.deleteByPrimaryKey (id);
    }

    @CacheEvict(allEntries = true)
    @Override
    public void update(Link link) {
        linkMapper.updateByPrimaryKey (link);
    }

    @Cacheable
    @Override
    public List<Link> selectAllFront() {
        return linkMapper.selectByExample (null);
    }
}
