package com.zp.blog.service;

import com.zp.blog.domain.UserInfo;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    public boolean register(UserInfo userInfo);

    public boolean login(String email, String password);

    public void updateStatus(String email, Boolean status);

    public void updatePassword(long id, String password);

    public UserInfo findByEmail(String email);

    void updateHeadPortrait(Long id, String headPortrait);

    public List<UserInfo> selectAll(Integer pageNum, Integer pageSize, String email);

    void forgetPassword(String email, String password);

    void updateNickname(Long id, String nickname);
}
