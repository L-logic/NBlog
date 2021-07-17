package com.zp.blog.service.impl;

import com.github.pagehelper.PageHelper;
import com.zp.blog.dao.BlogMapper;
import com.zp.blog.dao.TypeMapper;
import com.zp.blog.domain.Type;
import com.zp.blog.domain.TypeExample;
import com.zp.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service("typeService")
@CacheConfig(cacheNames = "type", keyGenerator = "myKeyGenerator", cacheManager = "objCacheManager") //全局指定一些公共的属性
public class TypeServiceImpl implements TypeService {

    @Autowired
    private TypeMapper typeMapper;

    @Autowired
    private BlogMapper blogMapper;

    @Override
    public List<Type> selectAllByLikeName(Integer pageNum, Integer pageSize, String name) {
        name = "%" + name + "%";

        TypeExample typeExample = new TypeExample ();
        TypeExample.Criteria criteria = typeExample.createCriteria ();
        criteria.andNameLike (name);

        PageHelper.startPage (pageNum, pageSize);
        List<Type> types = typeMapper.selectByExample (typeExample);

        return types;
    }

    @Cacheable
    @Override
    public Type selectById(Integer id) {
        return typeMapper.selectByPrimaryKey (id);
    }

    @CacheEvict(allEntries = true)
    @Override
    public Boolean save(Type type) {

        boolean flag = true;
        try {
            typeMapper.insert (type);
        } catch (Exception e) {
            flag = false;
            e.printStackTrace ();
        }
        return flag;
    }

    @CacheEvict(allEntries = true)
    @Override
    public Boolean update(Type type) {
        boolean flag = true;
        try {
            typeMapper.updateByPrimaryKeySelective (type);
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
            int count = blogMapper.selectCountByTypeId (id);
            if (count != 0) {
                flag = false;
            } else {
                typeMapper.deleteByPrimaryKey (id);
            }
        } catch (Exception e) {
            flag = false;
            e.printStackTrace ();
        }
        return flag;
    }


    @Override
    public Type selectByName(String name) {

        TypeExample typeExample = new TypeExample ();
        TypeExample.Criteria criteria = typeExample.createCriteria ();
        criteria.andNameEqualTo (name);

        List<Type> types = typeMapper.selectByExample (typeExample);
        if (types.size () > 0) {
            return types.get (0);
        }

        return null;
    }

    @Cacheable
    @Override
    public List<Type> selectAll() {
        return typeMapper.selectByExample (null);
    }

    //获取6条类别，并用属于该类别的博客的数量排序
    @Cacheable
    @Override
    public List<Type> selectAllWithCount() {

        List<Type> types = typeMapper.selectAllWithCount ();
        if (types.size () < 6) {
            int count = 6 - types.size ();
            //其余类别都是下面没有博客的
            List<Type> typeList = typeMapper.selectByOther (count);
            for (Type type : typeList) {
                types.add (type);
            }
        }

        return types;
    }

    //获取所有类别，并用属于该类别的博客的数量排序
    @Cacheable
    @Override
    public List<Type> selectAllTypesWithCount() {
        List<Type> types = typeMapper.selectAllWithCount ();
        List<Type> typeList = typeMapper.selectByOtherTypes ();
        if (typeList != null && typeList.size () > 0) {
            for (Type type : typeList) {
                types.add (type);
            }
        }

        return types;
    }


}
