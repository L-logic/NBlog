package com.zp.blog.web.comment;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
public class ValidateCodeController {

    @RequestMapping("/getCode")
    public void getCode(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //定义图形验证码的长、宽、验证码字符数、干扰线宽度
        ShearCaptcha captcha = CaptchaUtil.createShearCaptcha (200, 100, 4, 0);

        // 获取验证码
        String code = captcha.getCode ();
//        System.out.println (code);

        //存储到Session中
        HttpSession session = req.getSession ();
        session.setAttribute ("code",code);

        //把验证码图片放到响应的字节流中
        captcha.write (resp.getOutputStream ());
    }

}
