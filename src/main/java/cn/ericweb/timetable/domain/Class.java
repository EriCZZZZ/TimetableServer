package cn.ericweb.timetable.domain;

import cn.ericweb.timetable.utils.StringUtils;

/**
 * Created by eric on 17-2-21.
 */
public class Class extends Activity {
    private Subject subject;

    public Class(Subject subject) {
        this.subject = subject;
        this.setTitle(this.subject2Title());
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
        this.subject2Title();
    }

    private String subject2Title() {
        if(subject != null) {
            StringBuilder titleBuilder = new StringBuilder();
            titleBuilder.append(this.subject.getShortTitle());
            if(!StringUtils.isStringEmpty(this.getLocation())) {
                titleBuilder.append("@");
                titleBuilder.append(this.getLocation());
            }
            return titleBuilder.toString();
        }
        return "";
    }
}
