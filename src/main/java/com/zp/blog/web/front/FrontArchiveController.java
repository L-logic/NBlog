package com.zp.blog.web.front;

import com.zp.blog.domain.Blog;
import com.zp.blog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/front/archive")
public class FrontArchiveController {

    @Autowired
    private BlogService blogService;

    @RequestMapping("/showAll")
    public String showAll(Model model){

        //获取博客总数
        Integer count = blogService.count ();
        model.addAttribute ("count",count);

        //获取key为年份，value为对应年份发布的博客
        Map<String, List<Blog>> map = blogService.selectAllArchives();
        model.addAttribute ("map",map);
        return "archives";
    }


}
