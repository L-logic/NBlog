package com.zp.blog.web.front;

import com.zp.blog.domain.Time;
import com.zp.blog.service.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/front/time")
public class FrontTimeController {

    @Autowired
    private TimeService timeService;

    @RequestMapping("/timeAxis")
    public String timeAxis(Model model){
        List<Time> times = timeService.selectAllFront ();
        model.addAttribute ("times",times);
        return "time-axis";
    }
}
