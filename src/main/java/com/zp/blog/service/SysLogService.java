package com.zp.blog.service;

import com.zp.blog.domain.SysLog;

import java.util.List;

public interface SysLogService {

    public void save(SysLog sysLog);

    public void delete(String id);

    public void deleteSelect(List<String> ids);

    public List<SysLog> selectAll(Integer pageNum, Integer pageSize, String nickname);

}
