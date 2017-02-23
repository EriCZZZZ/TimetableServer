package cn.ericweb.timetable.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by eric on 17-2-5.
 */
@Controller
@RequestMapping(value="/timetable")
public class ClasstableController {
    @RequestMapping(method = RequestMethod.GET, value = {"", "/", "index"})
    public String index() {
        return "/jsp/classtable";
    }
}
