package com.zp.blog.service.impl;

import com.zp.blog.dao.RoleMapper;
import com.zp.blog.domain.Role;
import com.zp.blog.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("roleService")
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<Role> selectRolesByUserId(Long userId) {
        return roleMapper.selectRolesByUserId (userId);
    }

    @Override
    public void deleteRole(Long userId, Integer roleId) {
        roleMapper.deleteRole (userId, roleId);
    }

    @Override
    public void userAddRole(Long userId, Integer roleId) {
        roleMapper.userAddRole (userId, roleId);
    }

    @Override
    public List<Role> selectAll() {
        return roleMapper.selectByExample (null);
    }
}
