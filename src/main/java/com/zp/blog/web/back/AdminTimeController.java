package com.zp.blog.web.back;

import com.github.pagehelper.PageInfo;
import com.zp.blog.domain.Time;
import com.zp.blog.service.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin/time")
public class AdminTimeController {

    @Autowired
    private TimeService timeService;

    @RequestMapping("/showAll")
    public String showAll(@RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                          @RequestParam(name = "pageSize", required = false, defaultValue = "5") Integer pageSize, Model model){

        show (pageNum, pageSize, model);
        return "admin/times";
    }

    public void show(Integer pageNum, Integer pageSize, Model model){
        List<Time> times = timeService.selectAll (pageNum, pageSize);
        PageInfo<Time> pageInfo = new PageInfo<> (times, 8);
        model.addAttribute ("pageInfo", pageInfo);
    }

    @RequestMapping("/showAll/search")
    public String search(@RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                          @RequestParam(name = "pageSize", required = false, defaultValue = "5") Integer pageSize, Model model){

        show (pageNum, pageSize, model);
        return "admin/times :: timeList";
    }

    @RequestMapping("/toAdd")
    public String toAdd(){
        return "admin/times-input";
    }

    @RequestMapping("/add")
    public String add(Time time, RedirectAttributes attributes){

        time.setCreateTime (new Date ());
        try {
            timeService.save (time);
            attributes.addFlashAttribute ("message","添加成功！");
        } catch (Exception e) {
            e.printStackTrace ();
            attributes.addFlashAttribute ("message","服务器出现异常");
        }


        return "redirect:showAll";
    }

    @RequestMapping("/toUpdate")
    public String toUpdate(Integer id, Model model){
        Time time = timeService.selectById (id);
        model.addAttribute ("time",time);
        return "admin/times-update";
    }

    @RequestMapping("/update")
    public String update(Time time, RedirectAttributes attributes){
        try {
            timeService.update (time);
            attributes.addFlashAttribute ("message","修改成功！");
        } catch (Exception e) {
            e.printStackTrace ();
            attributes.addFlashAttribute ("message","服务器出现异常");
        }
        return "redirect:showAll";
    }

    @RequestMapping("/deleteById")
    public String deleteById(Integer id, RedirectAttributes attributes){

        try {
            timeService.deleteById (id);
            attributes.addFlashAttribute ("message","删除成功！");
        } catch (Exception e) {
            e.printStackTrace ();
            attributes.addFlashAttribute ("message","服务器出现异常");
        }

        return "redirect:showAll";
    }


}
