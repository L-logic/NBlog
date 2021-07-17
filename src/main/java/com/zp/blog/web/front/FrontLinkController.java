package com.zp.blog.web.front;

import com.zp.blog.domain.Link;
import com.zp.blog.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/front/link")
public class FrontLinkController {

    @Autowired
    private LinkService linkService;

    @RequestMapping("/showAll")
    public String showAll(Model model){
        List<Link> links = linkService.selectAllFront ();
        model.addAttribute ("links",links);
        return "links";
    }
}
