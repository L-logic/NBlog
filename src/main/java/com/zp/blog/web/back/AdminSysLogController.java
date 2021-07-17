package com.zp.blog.web.back;

import com.github.pagehelper.PageInfo;
import com.zp.blog.domain.SysLog;
import com.zp.blog.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/syslog")
public class AdminSysLogController {

    @Autowired
    private SysLogService sysLogService;

    @RequestMapping("/showAll")
    public String showAll(@RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                          @RequestParam(name = "pageSize", required = false, defaultValue = "6") Integer pageSize,
                          @RequestParam(name = "nickname", required = false, defaultValue = "") String nickname, Model model) {
        show (pageNum, pageSize, nickname, model);
        model.addAttribute ("nickname", nickname);
        return "admin/syslogs";
    }

    public void show(Integer pageNum, Integer pageSize, String nickname, Model model) {
        List<SysLog> sysLogs = sysLogService.selectAll (pageNum, pageSize, nickname);
        PageInfo<SysLog> pageInfo = new PageInfo<> (sysLogs, 10);
        model.addAttribute ("pageInfo", pageInfo);
    }

    @RequestMapping("/showAll/search")
    public String search(@RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                         @RequestParam(name = "pageSize", required = false, defaultValue = "6") Integer pageSize,
                         @RequestParam(name = "nickname", required = false, defaultValue = "") String nickname, Model model) {
        show (pageNum, pageSize, nickname, model);
        return "admin/syslogs :: syslogList";
    }

    @RequestMapping("/deleteById")
    public String deleteById(String id, RedirectAttributes attributes) {

        try {
            sysLogService.delete (id);
            attributes.addFlashAttribute ("message", "删除成功！");
        } catch (Exception e) {
            attributes.addFlashAttribute ("message", "删除失败，服务器异常！");
            e.printStackTrace ();
        }

        return "redirect:showAll";
    }

    @RequestMapping("/deleteBySelect")
    public String deleteBySelect(@RequestParam(name = "ids") List<String> ids, RedirectAttributes attributes) {

        try {
            sysLogService.deleteSelect (ids);
            attributes.addFlashAttribute ("message", "删除成功！");
        } catch (Exception e) {
            attributes.addFlashAttribute ("message", "删除失败，服务器异常！");
            e.printStackTrace ();
        }

        return "redirect:showAll";
    }
}
