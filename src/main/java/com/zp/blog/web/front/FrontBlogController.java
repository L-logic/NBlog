package com.zp.blog.web.front;

import com.github.pagehelper.PageInfo;
import com.zp.blog.domain.Blog;
import com.zp.blog.domain.Tag;
import com.zp.blog.domain.Type;
import com.zp.blog.service.BlogService;
import com.zp.blog.service.TagService;
import com.zp.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/front/blog")
public class FrontBlogController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    @RequestMapping("/showAll")
    public String showAll(@RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                          @RequestParam(name = "pageSize", required = false, defaultValue = "6") Integer pageSize , Model model){

        //获取所有博客分页
        PageInfo<Blog> pageInfo = blogService.selectAll (pageNum, pageSize);
        model.addAttribute ("pageInfo",pageInfo);

        //获取分类标签按照博客数量排序，截取6个
        List<Type> types = typeService.selectAllWithCount();
        model.addAttribute ("types",types);

        //获取标签下的博客数量降序排序，截取10个
        List<Tag> tags = tagService.selectAllWithCount();
        model.addAttribute ("tags",tags);

        //获取推荐的博客名称8个
        List<Blog> recommentBlogs = blogService.selectByRecommend ();
        model.addAttribute ("recommentBlogs",recommentBlogs);

        return "index";
    }

    @RequestMapping("/showAll/search")
    public String search(@RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                          @RequestParam(name = "pageSize", required = false, defaultValue = "6") Integer pageSize , Model model){

        //获取所有博客分页
        PageInfo<Blog> pageInfo = blogService.selectAll (pageNum, pageSize);
        model.addAttribute ("pageInfo",pageInfo);



        return "index::blogList";
    }


    @RequestMapping("/search")
    public String searchContent(@RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                          @RequestParam(name = "pageSize", required = false, defaultValue = "6") Integer pageSize ,
                         @RequestParam(name = "searchContent", required = false, defaultValue = "") String searchContent,
                         Model model){

        List<Blog> blogs = blogService.search(pageNum, pageSize, searchContent);
        PageInfo<Blog> pageInfo = new PageInfo<> (blogs, 5);
        model.addAttribute ("pageInfo",pageInfo);
        model.addAttribute ("searchContent",searchContent);

        return "search";
    }

    @RequestMapping("/search/page")
    public String searchContentByPage(@RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                          @RequestParam(name = "pageSize", required = false, defaultValue = "6") Integer pageSize ,
                         @RequestParam(name = "searchContent", required = false, defaultValue = "") String searchContent,
                         Model model){

        List<Blog> blogs = blogService.search(pageNum, pageSize, searchContent);
        PageInfo<Blog> pageInfo = new PageInfo<> (blogs, 5);
        model.addAttribute ("pageInfo",pageInfo);

        return "search::blogList";
    }

    @RequestMapping("/details")
    public String details(Long id, Model model){

        Blog blog = blogService.selectByIdOfFront (id);
        model.addAttribute ("blog",blog);

        return "blog";
    }

    @RequestMapping("/getHotBlogs")
    public String getHotBlogs(Model model){
        List<Blog> blogs = blogService.selectHotBlogs (); //根据查看的次数降序获取4个
        model.addAttribute ("blogs",blogs);

        return "commons :: hotBlogs";
    }
}
