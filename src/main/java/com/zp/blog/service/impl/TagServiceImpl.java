package com.zp.blog.service.impl;

import com.github.pagehelper.PageHelper;
import com.zp.blog.dao.TagMapper;
import com.zp.blog.domain.Tag;
import com.zp.blog.domain.TagExample;
import com.zp.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service("tagService")
@CacheConfig(cacheNames = "tag", keyGenerator = "myKeyGenerator", cacheManager = "objCacheManager") //全局指定一些公共的属性
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Override
    public List<Tag> selectAllByLikeName(Integer pageNum, Integer pageSize, String name) {
        name = "%" + name + "%";

        TagExample tagExample = new TagExample ();
        TagExample.Criteria criteria = tagExample.createCriteria ();
        criteria.andNameLike (name);

        PageHelper.startPage (pageNum, pageSize);
        List<Tag> tags = tagMapper.selectByExample (tagExample);

        return tags;
    }

    @Override
    public Tag selectById(Integer id) {
        return tagMapper.selectByPrimaryKey (id);
    }

    @CacheEvict(allEntries = true)
    @Override
    public Boolean save(Tag tag) {

        boolean flag = true;
        try {
            tagMapper.insert (tag);
        } catch (Exception e) {
            flag = false;
            e.printStackTrace ();
        }
        return flag;
    }

    @CacheEvict(allEntries = true)
    @Override
    public Boolean update(Tag tag) {
        boolean flag = true;
        try {
            tagMapper.updateByPrimaryKeySelective (tag);
        } catch (Exception e) {
            flag = false;
            e.printStackTrace ();
        }
        return flag;
    }

    @CacheEvict(allEntries = true)
    @Override
    public Boolean deleteById(Integer id) {
        boolean flag = true;
        try {
            int count = tagMapper.selectCountByTagIdForBlogAndTag(id);
            if (count != 0){
                flag = false;
            } else {
                tagMapper.deleteByPrimaryKey (id);
            }
        } catch (Exception e) {
            flag = false;
            e.printStackTrace ();
        }
        return flag;
    }


    @Override
    public Tag selectByName(String name) {

        TagExample tagExample = new TagExample ();
        TagExample.Criteria criteria = tagExample.createCriteria ();
        criteria.andNameEqualTo (name);

        List<Tag> tags = tagMapper.selectByExample (tagExample);
        if (tags.size () > 0) {
            return tags.get (0);
        }

        return null;
    }

    @Cacheable
    @Override
    public List<Tag> selectAll() {
        return tagMapper.selectByExample (null);
    }

    @Cacheable
    @Override
    public List<Tag> selectAllTagsWithCount() {

        List<Tag> tags = tagMapper.selectAllWithCount();
        List<Tag> tagList = tagMapper.selectByOtherTags();
        if (tagList != null && tagList.size () > 0){
            for (Tag tag : tagList) {
                tags.add (tag);
            }
        }
        return tags;
    }

    @Cacheable
    @Override
    public List<Tag> selectAllWithCount() {

        List<Tag> tags = tagMapper.selectAllWithCount();
        if (tags.size () < 10){  //小于10个标签，获取没有指定博客的标签
            int count = 10 -tags.size ();
            List<Tag> tagList = tagMapper.selectByOther(count);
            for (Tag tag : tagList) {
                tags.add (tag);
            }
        } else if (tags.size () > 10) {  //大于10个标签，截取10个
            List<Tag> tagList = new ArrayList<> ();
            for (int i = 0; i<10; i++){
                tagList.add (tags.get (i));
            }
            tags = tagList;
        }

        return tags;
    }
}
