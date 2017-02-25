package cn.ericweb.timetable.server.controller;

import cn.ericweb.timetable.domain.QueryInfo;
import cn.ericweb.timetable.queryI.query.Uestc;
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
        QueryInfo tmp = new QueryInfo();
        tmp.setId("2015060107021");
        tmp.setPwd("*13792503708");
        tmp.setStartYear("2016");
        tmp.setIndexSemester("1");
        Uestc uestc = new Uestc();
        uestc.init(tmp);
        uestc.login();
        uestc.getClasstable();

        return "/jsp/classtable";
    }
}
