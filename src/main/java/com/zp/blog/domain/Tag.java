package com.zp.blog.domain;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

public class Tag implements Serializable {
    private Integer id;

    @NotBlank(message = "标签名称不能重复")
    private String name;

    private Integer counts = 0;  //拥有该标签的博客数量

    public Tag() {
    }

    public Tag(Integer tagId) {
        this.id = tagId;
    }

    public Integer getCounts() {
        return counts;
    }

    public void setCounts(Integer counts) {
        this.counts = counts;
    }

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
}