package com.zp.blog.domain;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

public class Type implements Serializable {
    private Integer id;
    //JSR303校验
    @NotBlank(message = "类别名称不能为空")
    private String name;

    private Integer counts = 0; //是该类别的博客数量

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getCounts() {
        return counts;
    }

    public void setCounts(Integer counts) {
        this.counts = counts;
    }
}