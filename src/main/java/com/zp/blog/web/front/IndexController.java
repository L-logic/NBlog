package com.zp.blog.web.front;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @RequestMapping("/")
    public String index(){
        return "redirect:front/blog/showAll";
    }

//    @RequestMapping("/")
//    public String index(){
//        return "test";
//    }


    @RequestMapping("/blog")
    public String blog() {
        return "blog";
    }
}
