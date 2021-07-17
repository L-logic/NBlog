package com.zp.blog.service.impl;

import com.github.pagehelper.PageHelper;
import com.zp.blog.dao.SysLogMapper;
import com.zp.blog.domain.SysLog;
import com.zp.blog.domain.SysLogExample;
import com.zp.blog.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("sysLogService")
@Transactional
public class SysLogServiceImpl implements SysLogService {

    @Autowired
    private SysLogMapper sysLogMapper;

    @Override
    public void save(SysLog sysLog) {
        sysLogMapper.insert (sysLog);
    }

    @Override
    public void delete(String id) {
        sysLogMapper.deleteByPrimaryKey (id);
    }

    @Override
    public void deleteSelect(List<String> ids) {
        SysLogExample example = new SysLogExample ();
        SysLogExample.Criteria criteria = example.createCriteria ();
        criteria.andIdIn (ids);

        sysLogMapper.deleteByExample (example);
    }

    @Override
    public List<SysLog> selectAll(Integer pageNum, Integer pageSize, String nickname) {
        nickname = "%" + nickname + "%";

        PageHelper.startPage (pageNum, pageSize);
        return sysLogMapper.selectAll (nickname);
    }
}
