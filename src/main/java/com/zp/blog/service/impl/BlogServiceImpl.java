package com.zp.blog.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zp.blog.dao.BlogMapper;
import com.zp.blog.domain.Blog;
import com.zp.blog.domain.Tag;
import com.zp.blog.service.BlogService;
import com.zp.blog.utils.MarkdownUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@CacheConfig(cacheNames = "blog", keyGenerator = "myKeyGenerator", cacheManager = "objCacheManager") //全局指定一些公共的属性
@Transactional
@Service("blogService")
public class BlogServiceImpl implements BlogService {
    @Autowired
    private BlogMapper blogMapper;

    @Override
    public List<Blog> selectAll(Integer pageNum, Integer pageSize, String title, Integer typeId, Boolean recommend) {
        title = "%" + title + "%";

        PageHelper.startPage (pageNum, pageSize);
        return blogMapper.selectAll (title, typeId, recommend);
    }

    @Cacheable
    @Override
    public PageInfo<Blog> selectAll(Integer pageNum, Integer pageSize) {

        PageHelper.startPage (pageNum, pageSize);
        List<Blog> blogs = blogMapper.selectAllFront ();
        PageInfo<Blog> pageInfo = new PageInfo<> (blogs, 5);
        return pageInfo;
    }

    @Cacheable
    @Override
    public Blog selectById(Long id) {
        return blogMapper.selectById (id);
    }

    @CacheEvict(cacheNames = "blog", allEntries = true) //新增的话就清空cacheNames为blog这个缓存
    @Override
    public Boolean save(Blog blog) {
        boolean flag = false;

        try {
            blogMapper.insert (blog);
            flag = true;
            //保存成功，再在t_blog_tag存入信息
            List<Tag> tags = blog.getTags ();
            for (Tag tag : tags) {
                blogMapper.insertBlogAndTag (tag.getId (), blog.getId ());
            }
        } catch (Exception e) {
            e.printStackTrace ();
        }

        return flag;
    }

    @CacheEvict(cacheNames = "blog", allEntries = true) //修改的话就清空cacheNames为blog这个缓存
    @Override
    public Boolean update(Blog blog) {
        boolean flag = false;

        try {

            blogMapper.updateByPrimaryKeySelective (blog);
            flag = true;
            //修改成功，在修改t_blog_tag存入信息
            //先删除之前t_blog_tag存入的信息blogId 为 修改blog的id
            blogMapper.deleteByBlogIdForTagAndBlog (blog.getId ());

            List<Tag> tags = blog.getTags ();
            for (Tag tag : tags) {
                blogMapper.insertBlogAndTag (tag.getId (), blog.getId ());
            }
        } catch (Exception e) {
            e.printStackTrace ();
        }

        return flag;
    }

    @CacheEvict(cacheNames = "blog", allEntries = true) //删除的话就清空cacheNames为blog这个缓存
    @Override
    public Boolean deleteById(Long id) {
        boolean flag = false;

        try {

            //先删除关联表t_blog_tag中blog_id = id的
            blogMapper.deleteByBlogIdForTagAndBlog (id);

            blogMapper.deleteByPrimaryKey (id);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace ();
        }

        return flag;
    }

    @Cacheable
    @Override
    public List<Blog> selectByRecommend() {
        return blogMapper.selectByRecommend ();
    }


    @Override
    public List<Blog> search(Integer pageNum, Integer pageSize, String searchContent) {
        searchContent = "%" + searchContent + "%";
        PageHelper.startPage (pageNum, pageSize);
        List<Blog> blogs = blogMapper.search (searchContent);
        return blogs;
    }

    @Cacheable
    @Override
    public Blog selectByIdOfFront(Long id) {
        //查询详情 views+1
        Blog blog = blogMapper.selectByIdOfFront (id);
        int views = blog.getViews () + 1;
        blogMapper.updateViews (blog.getId (), views);
        blog.setViews (views);

        //把content的Markdown转为HTML
        String content = MarkdownUtil.markdownToHtmlExtensions (blog.getContent ());
        blog.setContent (content);

        return blog;
    }

    @Cacheable
    @Override
    public PageInfo<Blog> selectByTypeId(Integer pageNum, Integer pageSize, Integer id) {

        PageHelper.startPage (pageNum, pageSize);
        List<Blog> blogs = blogMapper.selectByTypeId (id);
        PageInfo<Blog> pageInfo = new PageInfo<> (blogs, 5);
        return pageInfo;
    }

    @Cacheable
    @Override
    public PageInfo<Blog> selectByTagId(Integer pageNum, Integer pageSize, Integer id) {
        PageHelper.startPage (pageNum, pageSize);
        List<Blog> blogs = blogMapper.selectByTagId (id);
        PageInfo<Blog> pageInfo = new PageInfo<> (blogs, 5);
        return pageInfo;
    }

    @Cacheable
    @Override
    public Map<String, List<Blog>> selectAllArchives() {
        //LinkedHashMap 可以记住插入顺序  HashMap是无序的
        Map<String, List<Blog>> map = new LinkedHashMap<> ();

        //获取所有年份
        List<String> years = blogMapper.selectYears ();

        for (String year : years) {
            //根据年份获取对应时间发布的博文
            List<Blog> blogs = blogMapper.selectBlogsByYear (year);
            map.put (year, blogs);
        }

        return map;
    }

    @Cacheable
    @Override
    public Integer count() {
        return Math.toIntExact (blogMapper.countByExample (null));
    }

    @Cacheable
    @Override
    public List<Blog> selectHotBlogs() {
        return blogMapper.selectHotBlogs ();
    }
}
