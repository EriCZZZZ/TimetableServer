package cn.ericweb.timetable.queryI.query;

import cn.ericweb.timetable.domain.Classtable;
import cn.ericweb.timetable.domain.QueryInfo;
import cn.ericweb.timetable.domain.QueryLoginResult;
import cn.ericweb.timetable.queryI.QueryI;
import com.google.gson.Gson;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by eric on 17-2-25.
 */
public class Uestc implements QueryI, Serializable {
    final static String ID = "username";
    final static String PWD = "password";
    final static String URL_LOGIN = "http://idas.uestc.edu.cn/authserver/login";
    final static String URL_CHECK_CHECK_CODE = "http://idas.uestc.edu.cn/authserver/needCaptcha.html";
    final static String URL_CLASSTABLE_ID = "http://eams.uestc.edu.cn/eams/courseTableForStd.action";
    final static String URL_CLASSTABLE_SEMESTER_CONFIG = "http://eams.uestc.edu.cn/eams/dataQuery.action";
    final static String URL_CLASSTABLE = "http://eams.uestc.edu.cn/eams/courseTableForStd!courseTable.action";

    private QueryInfo loginInfo;

    private HttpClient client;
    private Cookie[] cookies;

    private String classtable_id;
    private String classtable_ids;

    private UestcSemesterCfg semesterCfg;
    private String classtable_semester_id;

    private HashMap<String, String> hiddenInfo;

    private boolean isGood;
    private boolean isInited;
    private boolean isLogined;
    boolean isExistCheckcode = false;

    QueryLoginResult result = new QueryLoginResult();

    public void init(QueryInfo info) {
        this.loginInfo = info;

        this.isGood = true;
        this.isInited = false;
        this.isLogined = false;
        // 准备结果类
        this.result.setInfo("");
        this.result.setStatus(QueryLoginResult.ERROR_UNKNOWN);

        this.client = new HttpClient();
        GetMethod checkIsSchoolWebsiteGood = new GetMethod(Uestc.URL_LOGIN);
        GetMethod checkIsCheckcodeExist = new GetMethod(Uestc.URL_CHECK_CHECK_CODE);
        checkIsCheckcodeExist.getParams().setParameter(Uestc.ID, this.loginInfo.getId());
        try {
            int statusCode = this.client.executeMethod(checkIsSchoolWebsiteGood);
            if(statusCode != HttpStatus.SC_OK) {
                this.result.setStatus(QueryLoginResult.ERROR_SCHOOL_SERVICE);
                this.isGood = false;
            } else {
                this.hiddenInfo = new HashMap<String, String>();

                // parse html
                Document loginIndex = Jsoup.parse(checkIsSchoolWebsiteGood.getResponseBodyAsString());
                for(Element elm : loginIndex.select("form#casLoginForm>input[type=hidden]")) {
                    this.hiddenInfo.put(elm.attr("name"), elm.attr("value"));
                }
            }
            statusCode = this.client.executeMethod(checkIsCheckcodeExist);
            if(statusCode == HttpStatus.SC_OK) {
                if("true".equals(checkIsCheckcodeExist.getResponseBodyAsString())) {
                    this.isExistCheckcode = true;
                }
            }
            this.cookies = client.getState().getCookies();

            this.isInited = true;
        } catch (HttpException e) {
            this.result.setStatus(QueryLoginResult.ERROR_UNKNOWN);
            this.isGood = false;
        } catch (IOException e) {
            this.result.setStatus(QueryLoginResult.ERROR_SCHOOL_SERVICE);
            this.isGood = false;
        } finally {
            checkIsSchoolWebsiteGood.releaseConnection();
            checkIsCheckcodeExist.releaseConnection();
        }
    }

    public boolean ifExistCheckcode() {
        return this.isExistCheckcode;
    }

    public QueryLoginResult login() {
        if(this.isGood && this.isInited) {
            PostMethod login = new PostMethod(Uestc.URL_LOGIN);
            login.setParameter(Uestc.ID, this.loginInfo.getId());
            login.setParameter(Uestc.PWD, this.loginInfo.getPwd());
            for(Map.Entry<String, String> entry : this.hiddenInfo.entrySet()) {
                login.setParameter(entry.getKey(), entry.getValue());
            }

            try {
                int status = this.client.executeMethod(login);
                if(status == HttpStatus.SC_OK) {
                    Document doc = Jsoup.parse(login.getResponseBodyAsString());
                    this.result.setStatus(QueryLoginResult.ERROR_UNKNOWN);
                    this.result.setInfo(doc.select("span#msg").html());
                } else if(status == HttpStatus.SC_MOVED_TEMPORARILY) {
                    this.result.setStatus(QueryLoginResult.SUCCESS);
                    this.cookies = client.getState().getCookies();
                    this.isLogined = true;
                }
            } catch (HttpException e) {
                this.result.setStatus(QueryLoginResult.ERROR_UNKNOWN);
                this.isGood = false;
            } catch (IOException e) {
                this.result.setStatus(QueryLoginResult.ERROR_SCHOOL_SERVICE);
                this.isGood = false;
            } finally {
                login.releaseConnection();
            }
        }
        return this.result;
    }

    public Classtable getClasstable() {
        if(this.isLogined) {
            GetMethod classtableIDPost = new GetMethod(Uestc.URL_CLASSTABLE_ID);
            PostMethod classtableSemesterCfgPost = new PostMethod(Uestc.URL_CLASSTABLE_SEMESTER_CONFIG);
            PostMethod classtablePost = new PostMethod(Uestc.URL_CLASSTABLE);
            try {
                // get classtable id and ids
                int status = this.client.executeMethod(classtableIDPost);
                if(status == HttpStatus.SC_OK) {
                    Pattern id = Pattern.compile("(?<=id=\"toolbar).*(?=\">)");
                    Matcher idMatcher = id.matcher(classtableIDPost.getResponseBodyAsString());
                    if(idMatcher.find()) {
                        this.classtable_id = idMatcher.group();
                    } else {
                        throw new HttpException();
                    }
                    Pattern ids = Pattern.compile("(?<=ids\",\").*(?=\")");
                    Matcher idsMatcher = ids.matcher(classtableIDPost.getResponseBodyAsString());
                    if(idsMatcher.find()) {
                        this.classtable_ids = idsMatcher.group();
                    } else {
                        throw new HttpException();
                    }
                }

                // get semester config
                classtableSemesterCfgPost.setParameter("tagId", "semesterBar" + this.classtable_id + "Semester");
                classtableSemesterCfgPost.setParameter("dataType", "semesterCalendar");
                classtableSemesterCfgPost.setParameter("value", "143");
                classtableSemesterCfgPost.setParameter("empty", "false");
                status = this.client.executeMethod(classtableSemesterCfgPost);
                if(status == HttpStatus.SC_OK) {
                    Gson gson = new Gson();
                    this.semesterCfg = gson.fromJson(classtableSemesterCfgPost.getResponseBodyAsString(), UestcSemesterCfg.class);
                } else {
                    throw new HttpException();
                }
                for(Map.Entry<String, ArrayList<HashMap<String, String>>> entry : this.semesterCfg.getSemesters().entrySet()) {
                    for(HashMap<String, String> semester : entry.getValue()) {
                        if(semester.get("schoolYear").indexOf(this.loginInfo.getStartYear()) == 0 && semester.get("name").equals(this.loginInfo.getIndexSemester())) {
                            this.classtable_semester_id = semester.get("id");
                        }
                    }
                }

                classtablePost.setParameter("ignoreHead", "1");
                classtablePost.setParameter("setting.kind", "std");
                classtablePost.setParameter("startWeek", "1");
                classtablePost.setParameter("semester.id", this.classtable_semester_id);
                classtablePost.setParameter("ids", this.classtable_ids);

                status = this.client.executeMethod(classtablePost);

                System.out.println(classtablePost.getResponseBodyAsString());
            } catch (HttpException e) {
                this.result.setStatus(QueryLoginResult.ERROR_UNKNOWN);
                this.isGood = false;
            } catch (IOException e) {
                this.result.setStatus(QueryLoginResult.ERROR_SCHOOL_SERVICE);
                this.isGood = false;
            }
        }
        return null;
    }

    public Cookie[] getCookies() {
        return this.cookies;
    }

    public void setCookies(Cookie[] cookies) {
        this.cookies = cookies;
        this.client.getState().clearCookies();
        this.client.getState().addCookies(this.cookies);
    }
}
