package cn.ericweb.timetable.queryI.query;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by eric on 17-2-25.
 */
public class UestcSemesterCfg {
    private String yearDom;
    private HashMap<String, ArrayList<HashMap<String, String>>> semesters;
    private String yearIndex;
    private String termIndex;
    private String semesterId;

    @Override
    public String toString() {
        return "UestcSemesterCfg{" +
                "yearDom='" + yearDom + '\'' +
                ", semesters=" + semesters +
                ", yearIndex='" + yearIndex + '\'' +
                ", termIndex='" + termIndex + '\'' +
                ", semesterId='" + semesterId + '\'' +
                '}';
    }

    public String getYearDom() {
        return yearDom;
    }

    public void setYearDom(String yearDom) {
        this.yearDom = yearDom;
    }

    public HashMap<String, ArrayList<HashMap<String, String>>> getSemesters() {
        return semesters;
    }

    public void setSemesters(HashMap<String, ArrayList<HashMap<String, String>>> semesters) {
        this.semesters = semesters;
    }

    public String getYearIndex() {
        return yearIndex;
    }

    public void setYearIndex(String yearIndex) {
        this.yearIndex = yearIndex;
    }

    public String getTermIndex() {
        return termIndex;
    }

    public void setTermIndex(String termIndex) {
        this.termIndex = termIndex;
    }

    public String getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(String semesterId) {
        this.semesterId = semesterId;
    }
}
