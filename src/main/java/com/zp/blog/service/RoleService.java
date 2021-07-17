package com.zp.blog.service;

import com.zp.blog.domain.Role;

import java.util.List;

public interface RoleService {

    public List<Role> selectRolesByUserId(Long userId);

    public void deleteRole(Long userId, Integer roleId);

    public void userAddRole(Long userId, Integer roleId);

    List<Role> selectAll();
}
