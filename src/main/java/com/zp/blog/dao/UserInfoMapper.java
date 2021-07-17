package com.zp.blog.dao;

import com.zp.blog.domain.UserInfo;
import com.zp.blog.domain.UserInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserInfoMapper {
    long countByExample(UserInfoExample example);

    int deleteByExample(UserInfoExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UserInfo record);

    int insertSelective(UserInfo record);

    List<UserInfo> selectByExample(UserInfoExample example);

    UserInfo selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") UserInfo record, @Param("example") UserInfoExample example);

    int updateByExample(@Param("record") UserInfo record, @Param("example") UserInfoExample example);

    int updateByPrimaryKeySelective(UserInfo record);

    int updateByPrimaryKey(UserInfo record);

    UserInfo selectByEmail(String email);

    void addRole(@Param ("userId") Long userId, @Param ("roleId") int roleId);

    void updateStatus(@Param ("email") String email, @Param ("status") Boolean status);

    void updateHeadPortrait(@Param ("id") Long id, @Param ("headPortrait") String headPortrait);

    List<UserInfo> selectAll(String email);

    void forgetPassword(@Param ("email") String email, @Param ("password") String password);

    void updateNickname(@Param ("id") Long id, @Param ("nickname") String nickname);

    void updatePassword(@Param ("id") long id, @Param ("password") String password);
}