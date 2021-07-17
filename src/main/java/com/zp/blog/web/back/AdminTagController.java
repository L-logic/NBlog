package com.zp.blog.web.back;

import com.github.pagehelper.PageInfo;
import com.zp.blog.domain.Tag;
import com.zp.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin/tag")
public class AdminTagController {

    @Autowired
    private TagService TagService;

    @RequestMapping("/showAll")
    public ModelAndView showAll(@RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                @RequestParam(name = "pageSize", required = false, defaultValue = "5") Integer pageSize,
                                @RequestParam(name = "name", required = false, defaultValue = "") String name){
        ModelAndView mv = new ModelAndView ();

        //调用service层获取数据
        List<Tag> tags = TagService.selectAllByLikeName (pageNum, pageSize, name);
        //使用pageInfo包装查询后的结果，封装了详细的查询数据，其中参数5是页码导航连续显示的页数
        PageInfo<Tag> pageInfo = new PageInfo<> (tags,5);

        mv.addObject ("pageInfo",pageInfo);
        mv.addObject ("name",name);
        mv.setViewName ("admin/tags");

        return mv;
    }

    @RequestMapping("/toAdd")
    public String toAdd(Model model){
        model.addAttribute ("tag",new Tag ());
        return "admin/tags-input";
    }

    @RequestMapping("/add")
    public String add(@Valid Tag tag, BindingResult result, RedirectAttributes attributes){

        Tag tag1 = TagService.selectByName (tag.getName ());
        if (tag1 != null){
            result.rejectValue ("name","nameError","不能添加重复的标签名称");
        }

        if (result.hasErrors ()){
            return "admin/Tags-input";
        }

        //调用service层保存标签
        Boolean save = TagService.save (tag);

        if (save){
            attributes.addFlashAttribute ("message","添加成功！");
        } else {
            attributes.addFlashAttribute ("message","添加失败！");
        }

        return "redirect:showAll";
    }


    @RequestMapping("/toUpdate")
    public ModelAndView toUpdate(Integer id){
        ModelAndView mv = new ModelAndView ();

        Tag tag = TagService.selectById (id);

        mv.addObject ("tag",tag);
        mv.setViewName ("admin/tags-update");
        return mv;
    }

    @RequestMapping("/update")
    public String update(@Valid Tag tag, BindingResult result, RedirectAttributes attributes){

        Tag tag1 = TagService.selectByName (tag.getName ());
        if (tag1 != null){
            result.rejectValue ("name","nameError","该标签名称已存在，不能修改！");
        }

        if (result.hasErrors ()){
            return "admin/Tags-update";
        }

        Boolean update = TagService.update (tag);
        if (update){
            attributes.addFlashAttribute ("message","修改成功！");
        } else {
            attributes.addFlashAttribute ("message","修改失败！");
        }

        return "redirect:showAll";
    }


    @RequestMapping("/deleteById")
    public String deleteById(Integer id, RedirectAttributes attributes){

        //调用service层删除标签
        Boolean aBoolean = TagService.deleteById (id);
        if (aBoolean){
            attributes.addFlashAttribute ("message","删除成功！");
        } else {
            attributes.addFlashAttribute ("message","删除失败，有博客属于该标签，不能删除！");
        }

        return "redirect:showAll";
    }
}
