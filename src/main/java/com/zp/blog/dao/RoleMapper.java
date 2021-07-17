package com.zp.blog.dao;

import com.zp.blog.domain.Role;
import com.zp.blog.domain.RoleExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RoleMapper {
    long countByExample(RoleExample example);

    int deleteByExample(RoleExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Role record);

    int insertSelective(Role record);

    List<Role> selectByExample(RoleExample example);

    Role selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Role record, @Param("example") RoleExample example);

    int updateByExample(@Param("record") Role record, @Param("example") RoleExample example);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);

    List<Role> selectRolesByUserId(Long userId);

    void deleteRole(@Param ("userId") Long userId, @Param ("roleId") Integer roleId);

    void userAddRole(@Param ("userId") Long userId, @Param ("roleId") Integer roleId);
}