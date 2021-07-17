package com.zp.blog.utils;


import cn.hutool.core.util.RandomUtil;

import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * @Description
 *      username  为发送用户的登录账号
 *      password  为发送用户的登录密码
 */
public class SendEmailUtil {

    public static String username = "2605811855@qq.com";
    private static String password = "ediaduzstjouecda";
    public static void sendMessage(String email,String message){

        //构建会话对象   包括会话类型   和 会话账号密码
        Properties props = System.getProperties();
        //设置主机参数
        props.setProperty("mail.smtp.host", "smtp.qq.com");
        props.setProperty("mail.debug", "true");// 开启debug调试        
        props.setProperty("mail.smtp.auth", "true");// 发送服务器需要身份验证                
        props.setProperty("mail.transport.protocol", "smtp");// 发送邮件协议名称
        props.setProperty("mail.smtp.port", "587");//端口号
     
        Session session = Session.getDefaultInstance(props);
        session.setDebug(true);

        //构建 会话内容
        MimeMessage mimeMessage = new MimeMessage(session);

        try {
            //设置发送地址
            InternetAddress fromAddress = new InternetAddress(username);
            mimeMessage.setFrom(fromAddress);

            //设置接收人
            InternetAddress reciAddress = new InternetAddress(email);
            mimeMessage.setRecipient(RecipientType.TO, reciAddress);

            //设置邮件的头
            mimeMessage.setSubject("个人博客系统账户激活！");
            mimeMessage.setContent(message, "text/html;charset=UTF-8"); //text/html就可以发带html的内容

            //构建连接
            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.qq.com",username, password);
            transport.sendMessage(mimeMessage,mimeMessage.getAllRecipients());
            transport.close();
//            System.out.println("send Message SuccessFul!");
        } catch (Exception e) {
            // TODO Auto-generated catch block
//            System.out.println("send Message fail!");
            e.printStackTrace();
        }

    }
    public static void sendCode(String email,String code){
        String context="【当当网】您好，您当前正在进行当当网的注册，您的验证码为："+code+"。为了给您提供更好的服务，请您尽快完成注册。！";
        //参数    要接收邮件的邮箱 ,发送的内容
        sendMessage(email,context);
    }

    public static void main(String[] args) {
    	/*String code = RandomUtil.randomString(6);
    	System.out.println(code);*/
    	String message = "你好，" + "! <a href='http://localhost:8081/user/active?email=zp981025@163.com'>点击激活</a>";
       sendMessage ("zp981025@163.com",message);
    }

}
