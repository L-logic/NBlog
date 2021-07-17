package com.zp.blog.service.impl;

import com.github.pagehelper.PageHelper;
import com.zp.blog.dao.TimeMapper;
import com.zp.blog.domain.Time;
import com.zp.blog.service.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("timeService")
@Transactional
@CacheConfig(cacheNames = "time", keyGenerator = "myKeyGenerator", cacheManager = "objCacheManager") //全局指定一些公共的属性
public class TimeServiceImpl implements TimeService {

    @Autowired
    private TimeMapper timeMapper;

    @Override
    public List<Time> selectAll(Integer pageNum, Integer pageSize) {
        PageHelper.startPage (pageNum, pageSize);
        return timeMapper.selectByExample (null);
    }

    @Override
    public Time selectById(Integer id) {
        return timeMapper.selectByPrimaryKey (id);
    }

    @CacheEvict(allEntries = true)
    @Override
    public void save(Time time) {
        timeMapper.insert (time);
    }

    @CacheEvict(allEntries = true)
    @Override
    public void deleteById(Integer id) {
        timeMapper.deleteByPrimaryKey (id);
    }

    @CacheEvict(allEntries = true)
    @Override
    public void update(Time time) {
        timeMapper.updateByPrimaryKey (time);
    }

    @Cacheable
    @Override
    public List<Time> selectAllFront() {
        return timeMapper.selectByExample (null);
    }
}
