package cn.ericweb.timetable.domain;

/**
 * 保存科目的信息 不包含上课时间与位置的信息
 * @author eric
 * Created by eric on 17-2-21.
 */
public class Subject {
    private String id;
    private String title;
    private String shortTitle;
    private Teacher teacher;
    private ExamInfo examInfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortTitle() {
        return shortTitle;
    }

    public void setShortTitle(String shortTitle) {
        this.shortTitle = shortTitle;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public ExamInfo getExamInfo() {
        return examInfo;
    }

    public void setExamInfo(ExamInfo examInfo) {
        this.examInfo = examInfo;
    }
}
