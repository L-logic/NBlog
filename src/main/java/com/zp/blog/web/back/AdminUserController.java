package com.zp.blog.web.back;

import com.github.pagehelper.PageInfo;
import com.zp.blog.domain.Role;
import com.zp.blog.domain.UserInfo;
import com.zp.blog.service.RoleService;
import com.zp.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller("adminUserController")
@RequestMapping("/admin/user")
public class AdminUserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @RequestMapping("/showAll")
    public String showAll(@RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                          @RequestParam(name = "pageSize", required = false, defaultValue = "2") Integer pageSize,
                          @RequestParam(name = "email", required = false, defaultValue = "") String email, Model model) {

        show (pageNum, pageSize, email, model);
        return "admin/users";
    }

    public void show(Integer pageNum, Integer pageSize, String email, Model model){
        List<UserInfo> userInfos = userService.selectAll (pageNum, pageSize, email);
        PageInfo<UserInfo> pageInfo = new PageInfo<> (userInfos,8);
        model.addAttribute ("pageInfo",pageInfo);
        model.addAttribute ("email",email);
    }

    @RequestMapping("/showAll/search")
    public String search(@RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                          @RequestParam(name = "pageSize", required = false, defaultValue = "2") Integer pageSize,
                          @RequestParam(name = "email", required = false, defaultValue = "") String email, Model model) {

        show (pageNum, pageSize, email, model);
        return "admin/users :: userList";
    }

    @RequestMapping("/updateStatus")
    public String updateStatus(String email, RedirectAttributes attributes) {
        UserInfo userInfo = userService.findByEmail (email);
        Boolean status = userInfo.getStatus ();

        try {
            userService.updateStatus (email, !status);
            attributes.addFlashAttribute ("message","状态修改成功！");
        } catch (Exception e) {
            attributes.addFlashAttribute ("message","状态修改失败，服务器异常！");
            e.printStackTrace ();
        }


        return "redirect:showAll";
    }



}
