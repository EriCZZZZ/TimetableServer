package cn.ericweb.timetable.server.controller;

import cn.ericweb.timetable.domain.Class;
import cn.ericweb.timetable.domain.Classtable;
import cn.ericweb.timetable.domain.QueryInfo;
import cn.ericweb.timetable.domain.QueryLoginResult;
import cn.ericweb.timetable.eframework.ResponseBean;
import cn.ericweb.timetable.queryI.query.Uestc;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import javax.xml.ws.Response;

/**
 * Created by eric on 17-2-5.
 */
@Controller
@RequestMapping(value="/timetable")
public class ClasstableController {
    @RequestMapping(method = RequestMethod.GET, value = {"", "/", "index"})
    public String index(HttpSession session) {
        return "/jsp/classtable";
    }

    @RequestMapping(method = RequestMethod.GET, value = "json", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String classtableJson(@Autowired QueryInfo queryInfo, HttpSession session) {
        ResponseBean response = new ResponseBean();
        response.sc = ResponseBean.SC_FAIL;
        response.content = "";

        String resultJson;

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

        Uestc uestc = new Uestc();
        uestc.init(queryInfo);
        QueryLoginResult result = uestc.login();
        if(result.getStatus() != QueryLoginResult.SUCCESS) {
            resultJson = gson.toJson(result);
        } else {
            Classtable classtable = uestc.getClasstable();
            resultJson = gson.toJson(classtable);
            response.sc = ResponseBean.SC_SUCCESS;
        }

        response.content = resultJson;

        return gson.toJson(response);
    }
}
