package cn.ericweb.timetable.queryI;

import cn.ericweb.timetable.domain.Classtable;
import cn.ericweb.timetable.domain.QueryInfo;
import cn.ericweb.timetable.domain.QueryLoginResult;
import org.apache.commons.httpclient.Cookie;

/**
 * Created by eric on 17-2-25.
 */
public interface QueryI {
    public void init(QueryInfo info);
    public boolean ifExistCheckcode();
    public QueryLoginResult login();
    public Classtable getClasstable();
    public Cookie[] getCookies();
    public void setCookies(Cookie[] cookies);
}
