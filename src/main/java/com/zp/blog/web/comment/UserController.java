package com.zp.blog.web.comment;

import cn.hutool.core.util.RandomUtil;
import com.zp.blog.domain.UserInfo;
import com.zp.blog.service.UserService;
import com.zp.blog.utils.SendEmailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/user")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger (this.getClass ());

    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    public String login() {
        return "user/login";
    }

    @RequestMapping("toRegister")
    public String toRegister() {
        return "user/register";
    }

    @RequestMapping("/toUpload")
    public String toUpload() {
        return "user/upload";
    }


    //更换图片
    @PostMapping("/upload")
    @ResponseBody
    public Map<String, Object> upload(MultipartFile file, HttpServletRequest request) throws FileNotFoundException {
        Map<String, Object> map = new HashMap<> ();
        //文件上传的地址  static下的upload文件夹
        String property = System.getProperty ("user.dir");  //获取当前目录。相当于linux的pwd命令
        String separator = System.getProperty ("file.separator");  //获取文件分隔符 Windows是\  linux是/
        property = property + separator + "upload";
        File file2 = new File (property);
        // 创建File对象，一会向该路径下上传文件
        //不存在则创建
        if (!file2.exists ()) {
            file2.mkdir ();
        }
        System.out.println (file2.getAbsolutePath ());
        //上传的文件
        //获取到要上传文件的文件名
        String fileName = file.getOriginalFilename ();
        //生成一个唯一的文件名
        String uuid = UUID.randomUUID ().toString ().replace ("-", "").toUpperCase ();
        fileName = separator +  uuid + "-" + fileName;

        logger.info ("上传文件路径{}", file2.getAbsolutePath () + fileName);

        //上传
        try {
            file.transferTo (new File (file2.getAbsolutePath (), fileName));
            map.put ("code", -1);
            //设置保存在/static/upload下的访问路径 ---> /upload/图片名称
            String uploadPath = file2.getAbsolutePath () + fileName;
            uploadPath = uploadPath.substring (uploadPath.indexOf (separator+"upload"));
            //改变存储在session的头像地址
            UserInfo user = (UserInfo) SecurityContextHolder.getContext ().getAuthentication ().getPrincipal ();

            //获取之前头像在服务器端的路径
            String oldPath = file2.getAbsolutePath ().substring (0, file2.getAbsolutePath().indexOf (separator+"upload"));
            oldPath = oldPath + user.getHeadPortrait ();

            user.setHeadPortrait (uploadPath);
            //改变数据库中的地址
            userService.updateHeadPortrait (user.getId (), user.getHeadPortrait ());

            if (!oldPath.contains ("default.jpg")) { //不删除默认头像
                //更换成功后删除之前的头像
                File file1 = new File (oldPath);
                if (file1.exists ()) {
                    file1.delete ();
                }
            }

        } catch (IOException e) {
            e.printStackTrace ();
            map.put ("code", 1);
        }

        return map;
    }


    //注册
    @ResponseBody
    @RequestMapping("/register")
    public Map<String, Object> register(UserInfo userInfo, HttpServletRequest request) {
        //用于存储返回响应的信息
        Map<String, Object> map = new HashMap<> ();
        userInfo.setCreateTime (new Date ());
        //默认头像
        userInfo.setHeadPortrait ("/upload/default.jpg");
        userInfo.setStatus (true);
        userInfo.setUpdateTime (new Date ());

        boolean register = userService.register (userInfo);
        if (register) {
           // String message = "你好，" + userInfo.getNickname () + "! <a href='http://59.110.60.4:80/user/active?email=" + userInfo.getEmail () + "'>点击激活</a>";
            // SendEmailUtil.sendMessage (userInfo.getEmail (),  message);
            map.put ("flag", true);
        } else {
            map.put ("flag", false);
            map.put ("reg_error", "邮箱已存在");
        }

        return map;
    }

    //邮箱激活
    @RequestMapping("/active")
    public String active(String email, RedirectAttributes attributes) {
        UserInfo userInfo = userService.findByEmail (email);
        Boolean status = userInfo.getStatus ();

        try {
            userService.updateStatus (email, !status);
            attributes.addFlashAttribute ("flag", true);
            attributes.addFlashAttribute ("msg", "激活成功！");
        } catch (Exception e) {
            e.printStackTrace ();
            attributes.addFlashAttribute ("flag", false);
            attributes.addFlashAttribute ("msg", "激活失败！服务器异常");
        }

        return "redirect:login";
    }

    //去忘记密码页面
    @RequestMapping("/forget")
    public String forget() {
        return "user/forget";
    }

    @RequestMapping("/forget/sendCode")
    @ResponseBody
    public Map<String, Object> sendCode(String email, Model model) {
        Map<String, Object> map = new HashMap<> ();
        //生成6位激活码
        String activeCode = RandomUtil.randomString (6);

        //发送激活码
        String message = "您好，你的个人博客激活码为：<strong>" + activeCode + "</strong>";
        try {
            SendEmailUtil.sendMessage (email, message);
            map.put ("email", email);
            map.put ("flag", true);
            map.put ("msg", "验证码发送成功！");
        } catch (Exception e) {
            map.put ("flag", false);
            map.put ("msg", "服务器异常！");
            e.printStackTrace ();
        }

        //把激活码存入session
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes ();
        HttpServletRequest request = servletRequestAttributes.getRequest ();
        HttpSession session = request.getSession ();
        session.setAttribute ("activeCode", activeCode);

        return map;
    }

    @RequestMapping("/forget/toUpdate")
    public String toUpdate(String email, Model model) {
        model.addAttribute ("email",email);
        return "user/forget-update";
    }

    @RequestMapping("/forget/update")
    @ResponseBody
    public Map<String, Object> updatePassword(String email, String password, String vercode) {
        Map<String, Object> map = new HashMap<> ();

        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes ();
        HttpServletRequest request = servletRequestAttributes.getRequest ();
        HttpSession session = request.getSession ();
        String activeCode = (String) session.getAttribute ("activeCode");
        if (activeCode != null && activeCode.equals (vercode)) {
            try {
                userService.forgetPassword (email, password);
                map.put ("flag", true);
                map.put ("success_message", "修改成功，是否前往登录页面？");
            } catch (Exception e) {
                map.put ("flag", false);
                map.put ("fail_message", "修改失败，服务器异常!");
                e.printStackTrace ();
            }
        }

        return map;
    }

    //去修改昵称页面
    @RequestMapping("/toUpdateNickname")
    public String toUpdateNickname(){
        return "user/update-nickname";
    }

    //修改昵称
    @RequestMapping("/updateNickname")
    @ResponseBody
    public Map<String, Object> updateNickname(String nickname){
        Map<String, Object> map = new HashMap<> ();

        UserInfo user = (UserInfo) SecurityContextHolder.getContext ().getAuthentication ().getPrincipal ();
        Long id = user.getId ();

        try {
            userService.updateNickname(id, nickname);
            user.setNickname (nickname);
            map.put ("flag",true);
            map.put ("msg","修改成功");
        } catch (Exception e) {
            map.put ("flag",false);
            map.put ("msg","修改失败！服务器异常");
            e.printStackTrace ();
        }
        return map;
    }
    @RequestMapping("/toEdit")
    public String toEdit(){
        return "user/userInfo-edit";
    }

    @RequestMapping("/Edit")
    public String Edit(){
        return "user/userInfo-edit";
    }

    //去更新密码页面
    @RequestMapping("/toUpdatePassword")
    public String toUpdatePassword(){
        return "user/update-password";
    }

    //更新密码
    @RequestMapping("/updatePassword")
    @ResponseBody
    public Map<String, Object> updatePassword(String password){
        Map<String, Object> map = new HashMap<> ();
        UserInfo user = (UserInfo) SecurityContextHolder.getContext ().getAuthentication ().getPrincipal ();
        Long id = user.getId ();

        try {
            userService.updatePassword (id, password);
            user = null;
            map.put ("flag",true);
            map.put ("msg","修改成功");
        } catch (Exception e) {
            map.put ("flag",false);
            map.put ("msg","修改失败！服务器异常");
            e.printStackTrace ();
        }

        return map;
    }
}
