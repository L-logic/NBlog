package com.zp.blog.web.front;

import com.github.pagehelper.PageInfo;
import com.zp.blog.domain.Blog;
import com.zp.blog.domain.Tag;
import com.zp.blog.service.BlogService;
import com.zp.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/front/tag")
public class FrontTagController {

    @Autowired
    private TagService tagService;

    @Autowired
    private BlogService blogService;

    @RequestMapping("/showAll")
    public String showAll(@RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                          @RequestParam(name = "pageSize", required = false, defaultValue = "5") Integer pageSize ,
                          @RequestParam(name = "id", required = false, defaultValue = "-1")Integer id, Model model){

        List<Tag> tags = tagService.selectAllTagsWithCount (); //获取所有类别按照其下面的博客数量排序
        model.addAttribute ("tags",tags);

        if (id == -1){
            id = tags.get (0).getId ();
        }
        model.addAttribute ("tagId",id);

        PageInfo<Blog> pageInfo = blogService.selectByTagId (pageNum, pageSize, id);
        model.addAttribute ("pageInfo",pageInfo);

        return "tags";
    }

    @RequestMapping("/showAll/search")
    public String search(@RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                          @RequestParam(name = "pageSize", required = false, defaultValue = "5") Integer pageSize ,
                          @RequestParam(name = "id", required = false, defaultValue = "-1")Integer id, Model model){

        model.addAttribute ("tagId",id);

        PageInfo<Blog> pageInfo = blogService.selectByTagId (pageNum, pageSize, id);
        model.addAttribute ("pageInfo",pageInfo);

        return "tags :: blogList";
    }
}
