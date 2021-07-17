package com.zp.blog.web.back;

import com.github.pagehelper.PageInfo;
import com.zp.blog.domain.Type;
import com.zp.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin/type")
public class AdminTypeController {

    @Autowired
    private TypeService typeService;

    @RequestMapping("/showAll")
    public ModelAndView showAll(@RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                @RequestParam(name = "pageSize", required = false, defaultValue = "5") Integer pageSize,
                                @RequestParam(name = "name", required = false, defaultValue = "") String name){
        ModelAndView mv = new ModelAndView ();

        //调用service层获取数据
        List<Type> types = typeService.selectAllByLikeName (pageNum, pageSize, name);
        //使用pageInfo包装查询后的结果，封装了详细的查询数据，其中参数5是页码导航连续显示的页数
        PageInfo<Type> pageInfo = new PageInfo<> (types,5);

        mv.addObject ("pageInfo",pageInfo);
        mv.addObject ("name",name);
        mv.setViewName ("admin/types");

        return mv;
    }

    @RequestMapping("/toAdd")
    public String toAdd(Model model){
        model.addAttribute ("type",new Type ());
        return "admin/types-input";
    }

    @RequestMapping("/add")
    public String add(@Valid Type type, BindingResult result, RedirectAttributes attributes){

        Type type1 = typeService.selectByName (type.getName ());
        if (type1 != null){
            result.rejectValue ("name","nameError","不能添加重复的类别名称");
        }

        if (result.hasErrors ()){
            return "admin/types-input";
        }

        //调用service层保存类别
        Boolean save = typeService.save (type);

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

        Type type = typeService.selectById (id);

        mv.addObject ("type",type);
        mv.setViewName ("admin/types-update");
        return mv;
    }

    @RequestMapping("/update")
    public String update(@Valid Type type, BindingResult result, RedirectAttributes attributes){

        Type type1 = typeService.selectByName (type.getName ());
        if (type1 != null){
            result.rejectValue ("name","nameError","该类别名称已存在，不能修改！");
        }

        if (result.hasErrors ()){
            return "admin/types-update";
        }

        Boolean update = typeService.update (type);
        if (update){
            attributes.addFlashAttribute ("message","修改成功！");
        } else {
            attributes.addFlashAttribute ("message","修改失败！");
        }

        return "redirect:showAll";
    }


    @RequestMapping("/deleteById")
    public String deleteById(Integer id, RedirectAttributes attributes){

        //调用service层删除类别
        Boolean aBoolean = typeService.deleteById (id);
        if (aBoolean){
            attributes.addFlashAttribute ("message","删除成功！");
        } else {
            attributes.addFlashAttribute ("message","删除失败，有博客是该类别！");
        }

        return "redirect:showAll";
    }
}
