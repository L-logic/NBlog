package com.zp.blog.web.front;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/front/about")
public class FrontAboutController {

    @RequestMapping("/about")
    public String about(){
        return "about";
    }
}
