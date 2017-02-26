package cn.ericweb.timetable.queryI.query;

import cn.ericweb.timetable.domain.*;
import cn.ericweb.timetable.domain.Class;
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
import java.util.Arrays;
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

    final static int COUNT_OF_CLASSES_PER_DAY = 12;
    final static int START_HOUR = 8;
    final static int START_MINUTE = 30;
    final static int MINS_PER_CLASS = 45;
    final static ArrayList<Integer> INTERVAL_PER_COURSE = new ArrayList<Integer>(Arrays.asList(5, 15, 5, 155, 5, 15, 5, 95, 5, 5, 5));

    private QueryInfo loginInfo;

    private HttpClient client;
    private Cookie[] cookies;

    private String classtable_id;
    private String classtable_ids;

    private UestcSemesterCfg semesterCfg;
    private String classtable_semester_id;
    private Classtable classtable;

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

                this.classtable = new Classtable(Uestc.COUNT_OF_CLASSES_PER_DAY, Uestc.START_HOUR, Uestc.START_MINUTE, Uestc.MINS_PER_CLASS, Uestc.INTERVAL_PER_COURSE);
                // parse classtable
                String classtableHtml = classtablePost.getResponseBodyAsString();

                String[] subjectInfoRows = classtableHtml.split("activity\\s+=\\s+new\\s+TaskActivity");

                Pattern subjectInfoPattern = Pattern.compile("\\(\"([0-9,]+)\",\"([^\"]+)\",\"([^\"]+)\",\"([^\"\\(]+)[^\")]+\\)\",\"[^\"]*\",\"([^\"]*)\",\"([01]*)\"\\);");
                Pattern timeOfClassPattern = Pattern.compile("index =(\\d*)\\*unitCount\\+(\\d*)");

                for (String info : subjectInfoRows) {
                    Matcher subjectInfo = subjectInfoPattern.matcher(info);
                    if (true == subjectInfo.find()) {
                        String teacherName = subjectInfo.group(2);
                        String subjectId = subjectInfo.group(3);
                        String subjectTitle = subjectInfo.group(4);
                        String subjectAddress = subjectInfo.group(5);
                        String timeArrange = subjectInfo.group(6);

                        Teacher teacher = new Teacher();
                        teacher.setName(teacherName);

                        Subject subject = new Subject();
                        subject.setId(subjectId);
                        subject.setTeacher(teacher);
                        subject.setTitle(subjectTitle);
                        subject.setShortTitle(subjectTitle);

                        this.classtable.pushSubject(subject);

                        int day;
                        day = 0;
                        int classIndex;
                        classIndex = 0;

                        Matcher timeOfClassMatcher = timeOfClassPattern.matcher(info);
                        while (timeOfClassMatcher.find()) {

                            day = Integer.parseInt(timeOfClassMatcher.group(1));
                            classIndex = Integer.parseInt(timeOfClassMatcher.group(2));

                            Class _class = new Class(subject);
                            _class.setClass(true);
                            _class.setExistedWeek(timeArrange);
                            _class.setLocation(subjectAddress);
                            _class.setWhichWeekday(day);
                            _class.setStartClassIndex(classIndex);
                            _class.setEndClassIndex(classIndex);
                            _class.setTitle(_class.subject2Title());

                            this.classtable.pushActivities(_class);
                        }
                    }
                }
                this.classtable.fixClasses();
            } catch (HttpException e) {
                this.result.setStatus(QueryLoginResult.ERROR_UNKNOWN);
                this.isGood = false;
            } catch (IOException e) {
                this.result.setStatus(QueryLoginResult.ERROR_SCHOOL_SERVICE);
                this.isGood = false;
            } finally {
                classtableIDPost.releaseConnection();
                classtablePost.releaseConnection();
                classtableSemesterCfgPost.releaseConnection();
            }
        }
        return this.classtable;
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
