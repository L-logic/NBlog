package com.zp.blog;

import com.zp.blog.dao.UserInfoMapper;
import com.zp.blog.domain.UserInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogApplicationTests {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Test
    public void contextLoads() {
        /*String password = passwordEncoder.encode ("zhangpeng98@aliyun.com");
        UserInfo userInfo = new UserInfo ("张鹏", "zhangpeng98@aliyun.com", password, true, "//sdas", new Date (), new Date ());
        userInfo.setId (1L);
        userInfoMapper.insert (userInfo);*/
        System.out.println (userInfoMapper.selectByPrimaryKey (1L));


    }


    @Autowired
    private DataSource dataSource;

    @Test
    public void getDatasource(){
        System.out.println (dataSource);
    }
}
