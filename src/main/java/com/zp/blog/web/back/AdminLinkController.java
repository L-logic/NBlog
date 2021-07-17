package com.zp.blog.web.back;

import com.github.pagehelper.PageInfo;
import com.zp.blog.domain.Link;
import com.zp.blog.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin/link")
public class AdminLinkController {

    @Autowired
    private LinkService linkService;

    @RequestMapping("/showAll")
    public String showAll(@RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                          @RequestParam(name = "pageSize", required = false, defaultValue = "5") Integer pageSize,
                          @RequestParam(name = "name", required = false, defaultValue = "") String name, Model model) {

        show (pageNum, pageSize, name, model);
        return "admin/links";
    }

    public void show(Integer pageNum, Integer pageSize, String name, Model model) {
        List<Link> links = linkService.selectAll (pageNum, pageSize, name);
        PageInfo<Link> pageInfo = new PageInfo<> (links, 8);
        model.addAttribute ("pageInfo", pageInfo);
    }

    @RequestMapping("/showAll/search")
    public String search(@RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                         @RequestParam(name = "pageSize", required = false, defaultValue = "5") Integer pageSize,
                         @RequestParam(name = "name", required = false, defaultValue = "") String name, Model model) {

        show (pageNum, pageSize, name, model);
        return "admin/links :: linkList";
    }

    @RequestMapping("/toAdd")
    public String toAdd() {
        return "admin/links-input";
    }

    @RequestMapping("/add")
    public String add(Link link, RedirectAttributes attributes) {

        link.setCreateTime (new Date ());
        link.setUpdateTime (new Date ());
        try {
            linkService.save (link);
            attributes.addFlashAttribute ("message", "添加成功！");
        } catch (Exception e) {
            e.printStackTrace ();
            attributes.addFlashAttribute ("message", "服务器出现异常");
        }


        return "redirect:showAll";
    }

    @RequestMapping("/toUpdate")
    public String toUpdate(Integer id, Model model) {
        Link link = linkService.selectById (id);
        model.addAttribute ("link", link);
        return "admin/links-update";
    }

    @RequestMapping("/update")
    public String update(Link link, RedirectAttributes attributes) {
        try {
            link.setUpdateTime (new Date ());
            linkService.update (link);
            attributes.addFlashAttribute ("message", "修改成功！");
        } catch (Exception e) {
            e.printStackTrace ();
            attributes.addFlashAttribute ("message", "服务器出现异常");
        }
        return "redirect:showAll";
    }

    @RequestMapping("/deleteById")
    public String deleteById(Integer id, RedirectAttributes attributes) {

        try {
            linkService.deleteById (id);
            attributes.addFlashAttribute ("message", "删除成功！");
        } catch (Exception e) {
            e.printStackTrace ();
            attributes.addFlashAttribute ("message", "服务器出现异常");
        }

        return "redirect:showAll";
    }

}
