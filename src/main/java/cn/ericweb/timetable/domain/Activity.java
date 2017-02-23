package cn.ericweb.timetable.domain;

/**
 * 展现于时间表上的所有物体的基类
 * Created by eric on 17-2-21.
 */
public class Activity {
    private String title;
    private String location;

    private int whichWeekday;
    private int startClassIndex;
    private int endClassIndex;

    private String existedWeek;

    private Color colorFont;
    private Color colorBg;

    @Override
    public String toString() {
        return "Activity{" +
                "title='" + title + '\'' +
                ", location='" + location + '\'' +
                ", whichWeekday=" + whichWeekday +
                ", startClassIndex=" + startClassIndex +
                ", endClassIndex=" + endClassIndex +
                ", existedWeek='" + existedWeek + '\'' +
                ", colorFont=" + colorFont +
                ", colorBg=" + colorBg +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getWhichWeekday() {
        return whichWeekday;
    }

    public void setWhichWeekday(int whichWeekday) {
        this.whichWeekday = whichWeekday;
    }

    public int getStartClassIndex() {
        return startClassIndex;
    }

    public void setStartClassIndex(int startClassIndex) {
        this.startClassIndex = startClassIndex;
    }

    public int getEndClassIndex() {
        return endClassIndex;
    }

    public void setEndClassIndex(int endClassIndex) {
        this.endClassIndex = endClassIndex;
    }

    public String getExistedWeek() {
        return existedWeek;
    }

    public void setExistedWeek(String existedWeek) {
        this.existedWeek = existedWeek;
    }

    public Color getColorFont() {
        return colorFont;
    }

    public void setColorFont(Color colorFont) {
        this.colorFont = colorFont;
    }

    public Color getColorBg() {
        return colorBg;
    }

    public void setColorBg(Color colorBg) {
        this.colorBg = colorBg;
    }
}
