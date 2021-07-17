package com.zp.blog.web.back;

import com.github.pagehelper.PageInfo;
import com.zp.blog.domain.Blog;
import com.zp.blog.domain.Tag;
import com.zp.blog.domain.Type;
import com.zp.blog.domain.UserInfo;
import com.zp.blog.service.BlogService;
import com.zp.blog.service.TagService;
import com.zp.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin/blog")
public class AdminBlogController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    @RequestMapping("/showAll")
    public ModelAndView showAll(@RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                @RequestParam(name = "pageSize", required = false, defaultValue = "5") Integer pageSize,
                                @RequestParam(name = "title", required = false, defaultValue = "") String title,
                                @RequestParam(name = "typeId", required = false, defaultValue = "0") Integer typeId,
                                @RequestParam(name = "recommend", required = false, defaultValue = "false") Boolean recommend) {
        if (recommend == false) {
            recommend = null;
        }

        ModelAndView mv = new ModelAndView ();

        //展示所有博客前查询所有类别
        List<Type> types = typeService.selectAll ();
        mv.addObject ("types", types);

        //调用业务层方法
        List<Blog> blogs = blogService.selectAll (pageNum, pageSize, title, typeId, recommend);
        PageInfo<Blog> pageInfo = new PageInfo<> (blogs, 5);

        mv.addObject ("pageInfo", pageInfo);
        mv.addObject ("title", title);
        mv.addObject ("typeId", typeId);
        mv.addObject ("recommend", recommend);

        mv.setViewName ("admin/blogs");
        return mv;
    }

    @RequestMapping("/showAll/search")
    public String search(@RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                         @RequestParam(name = "pageSize", required = false, defaultValue = "5") Integer pageSize,
                         @RequestParam(name = "title", required = false, defaultValue = "") String title,
                         @RequestParam(name = "typeId", required = false, defaultValue = "0") Integer typeId,
                         @RequestParam(name = "recommend", required = false, defaultValue = "false") Boolean recommend,
                         Model model) {

        if (recommend == false) {
            recommend = null;
        }


        //调用业务层方法
        List<Blog> blogs = blogService.selectAll (pageNum, pageSize, title, typeId, recommend);
        PageInfo<Blog> pageInfo = new PageInfo<> (blogs, 5);

        model.addAttribute ("pageInfo", pageInfo);
        return "admin/blogs::blogList";
    }

    @RequestMapping("/toAdd")
    public String toAdd(Model model) {

        //添加博客页前展示所有类别
        List<Type> types = typeService.selectAll ();
        model.addAttribute ("types", types);

        //添加博客页前展示所有标签
        List<Tag> tags = tagService.selectAll ();
        model.addAttribute ("tags", tags);

        return "admin/blogs-input";
    }

    @RequestMapping("/add")
    public String add(Blog blog, @RequestParam(name = "tagIds", required = false) List<Integer> tagIds, RedirectAttributes attributes) {
        blog.setViews (0);
        blog.setCreateTime (new Date ());
        blog.setUpdateTime (new Date ());
        blog.setCommentCount (0);

        blog.setTags (getTags (tagIds));
        //获取用户id
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext ().getAuthentication ().getPrincipal ();
        blog.setUserInfo (new UserInfo (userInfo.getId ()));

        //封装完成，调用service层保存
        Boolean save = blogService.save (blog);
        if (save) {
            attributes.addFlashAttribute ("message", "添加成功！");
        } else {
            attributes.addFlashAttribute ("message", "添加失败！");
        }

        return "redirect:showAll";
    }

    public static List<Tag> getTags(List<Integer> tagIds) {
        List<Tag> tags = new ArrayList<> ();
        for (Integer tagId : tagIds) {
            tags.add (new Tag (tagId));
        }
        return tags;
    }

    @RequestMapping("/toUpdate")
    public String toUpdate(Long id, Model model) {

        Blog blog = blogService.selectById (id);
        model.addAttribute ("blog", blog);

        List<Type> types = typeService.selectAll ();
        model.addAttribute ("types", types);

        List<Tag> tags = tagService.selectAll ();
        model.addAttribute ("tags", tags);


        //用于展示标签
        String tagIds = "";
        List<Tag> tagList = blog.getTags ();
        if (tagList.size () == 0 || tagList == null) {
            tagIds = tagIds;
        } else {
            for (Tag tag : tagList) {
                tagIds += "," + tag.getId ();
            }
            tagIds = tagIds.substring (1);
        }


        model.addAttribute ("tagIds", tagIds);

        return "admin/blogs-update";
    }

    @RequestMapping("/update")
    public String update(Blog blog, @RequestParam(name = "tagIds", required = false) List<Integer> tagIds, RedirectAttributes attributes) {
        blog.setUpdateTime (new Date ());
        blog.setTags (getTags (tagIds));
        //获取用户id
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext ().getAuthentication ().getPrincipal ();
        blog.setUserInfo (new UserInfo (userInfo.getId ()));
        Boolean update = blogService.update (blog);
        if (update) {
            attributes.addFlashAttribute ("message", "修改成功！");
        } else {
            attributes.addFlashAttribute ("message", "修改失败！");
        }

        return "redirect:showAll";
    }

    @RequestMapping("/deleteById")
    public String deleteById(Long id, RedirectAttributes attributes) {

        Boolean aBoolean = blogService.deleteById (id);
        if (aBoolean) {
            attributes.addFlashAttribute ("message", "删除成功！");
        } else {
            attributes.addFlashAttribute ("message", "删除失败！");
        }

        return "redirect:showAll";
    }
}
