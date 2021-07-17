package com.zp.blog.web.back;

import com.github.pagehelper.PageInfo;
import com.zp.blog.domain.Comment;
import com.zp.blog.domain.Message;
import com.zp.blog.service.CommentService;
import com.zp.blog.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/message")
public class AdminMessageController {

    @Autowired
    private MessageService messageService;

    @RequestMapping("/showAll")
    public String showAll(@RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                          @RequestParam(name = "pageSize", required = false, defaultValue = "6") Integer pageSize,
                          @RequestParam(name = "content", required = false, defaultValue = "") String content,
                          @RequestParam(name = "email", required = false, defaultValue = "") String email,
                          Model model) {



        show (pageNum, pageSize, content, email, model);
        model.addAttribute ("pageSize", pageSize);
        model.addAttribute ("content", content);
        model.addAttribute ("email", email);
        return "admin/messages";
    }

    public void show(Integer pageNum, Integer pageSize,  String content, String email, Model model){
        List<Message> messages = messageService.selectAllWithUser(pageNum, pageSize, content, email);
        PageInfo<Message> pageInfo = new PageInfo<> (messages, 8);
        model.addAttribute ("pageInfo", pageInfo);

    }

    @RequestMapping("/showAll/search")
    public String search(@RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                          @RequestParam(name = "pageSize", required = false, defaultValue = "6") Integer pageSize,
                          @RequestParam(name = "content", required = false, defaultValue = "") String content,
                          @RequestParam(name = "email", required = false, defaultValue = "") String email,
                          Model model) {

        show (pageNum, pageSize, content, email, model);

        return "admin/messages :: messageList";
    }

    @RequestMapping("/deleteById")
    public String deleteById(Integer id, RedirectAttributes attributes){

        try {
            messageService.deleteById(id);
            attributes.addFlashAttribute ("message","删除成功！");
        } catch (Exception e) {
            attributes.addFlashAttribute ("message","删除失败，服务器异常！");
            e.printStackTrace ();
        }

        return "redirect:showAll";
    }
}
