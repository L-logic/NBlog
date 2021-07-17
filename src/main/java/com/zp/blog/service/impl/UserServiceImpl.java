package com.zp.blog.service.impl;

import com.github.pagehelper.PageHelper;
import com.zp.blog.dao.UserInfoMapper;
import com.zp.blog.domain.Role;
import com.zp.blog.domain.UserInfo;
import com.zp.blog.domain.UserInfoExample;
import com.zp.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean register(UserInfo userInfo) {
        UserInfo userInfo1 = userInfoMapper.selectByEmail (userInfo.getEmail ());
        if (userInfo1 == null){
            //邮箱不存在
            //添加用户
            userInfo.setPassword (passwordEncoder.encode (userInfo.getPassword ()));
            userInfoMapper.insert (userInfo);

            //默认添加ROLE_USER角色
            userInfoMapper.addRole(userInfo.getId(),2);
            return true;
        } else {

            return false;
        }
    }

    @Override
    public boolean login(String email, String password) {
        return false;
    }

    @Override
    public void updateStatus(String email, Boolean status) {
        userInfoMapper.updateStatus(email,status);
    }

    @Override
    public void updatePassword(long id, String password) {
        password = passwordEncoder.encode (password);
        userInfoMapper.updatePassword(id, password);
    }

    @Override
    public UserInfo findByEmail(String email) {
        return userInfoMapper.selectByEmail (email);
    }

    @Override
    public void updateHeadPortrait(Long id, String headPortrait) {
        userInfoMapper.updateHeadPortrait(id, headPortrait);
    }

    @Override
    public List<UserInfo> selectAll(Integer pageNum, Integer pageSize, String email) {
        email = "%" + email + "%";
        PageHelper.startPage (pageNum, pageSize);
        List<UserInfo> userInfos = userInfoMapper.selectAll (email);
        return userInfos;
    }

    @Override
    public void forgetPassword(String email, String password) {
        password = passwordEncoder.encode (password);
        userInfoMapper.forgetPassword(email, password);
    }

    @Override
    public void updateNickname(Long id, String nickname) {
        userInfoMapper.updateNickname(id, nickname);
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserInfo userInfo = userInfoMapper.selectByEmail (email);

        List<GrantedAuthority> list = new ArrayList<> ();
        List<Role> roles = userInfo.getRoles ();
        for (Role role : roles) {
            list.add (new SimpleGrantedAuthority (role.getName ()));
        }
        userInfo.setAuthorities (list);


        return userInfo;
    }
}
