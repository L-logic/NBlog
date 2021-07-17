package com.zp.blog.web.front;

import com.github.pagehelper.PageInfo;
import com.zp.blog.domain.Message;
import com.zp.blog.domain.UserInfo;
import com.zp.blog.service.MessageService;
import com.zp.blog.service.impl.MessageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/front/message")
public class FrontMessageController {

    @Autowired
    private MessageService messageService;

    @RequestMapping("/messages")
    public String messages(@RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                           @RequestParam(name = "pageSize", required = false, defaultValue = "6") Integer pageSize,
                           Model model) {

        showAll (pageNum, pageSize, model);

        return "messages";
    }

    public void showAll(Integer pageNum, Integer pageSize, Model model){
        List<Message> messages = messageService.selectAll (pageNum, pageSize);
        PageInfo<Message> pageInfo = new PageInfo<> (messages, 8);
        MessageServiceImpl messageServiceImpl = (MessageServiceImpl) messageService;
        pageInfo.setList (messageServiceImpl.getAll (pageInfo.getList ()));

        model.addAttribute ("pageInfo", pageInfo);
    }

    @RequestMapping("/addMessage")
    public String addMessage(Message message) {

        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext ().getAuthentication ().getPrincipal ();

        message.setUserInfo (userInfo);
        message.setCreateTime (new Date ());

        if (message.getParentMessage ().getId () == -1) {
            message.getParentMessage ().setId (null);
        }

        messageService.saveMessage (message);

        return "redirect:showAllMessages";
    }



    @RequestMapping("/showAllMessages")
    public String showAllMessages(@RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                  @RequestParam(name = "pageSize", required = false, defaultValue = "6") Integer pageSize,
                                  Model model) {

        showAll (pageNum, pageSize, model);
        return "messages :: messageList";
    }


}
