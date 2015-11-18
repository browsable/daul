package timedao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table MY_TIME.
 */
public class MyTime {

    private Long id;
    /** Not-null value. */
    private String timecode;
    private int timetype;
    private String name;
    private Integer year;
    private Integer monthofyear;
    private Integer dayofmonth;
    private int dayofweek;
    private int starthour;
    private int startmin;
    private int endhour;
    private int endmin;
    private Long startmillis;
    private Long endmillis;
    private String memo;
    private String place;
    private Double lat;
    private Double lng;
    private Integer share;
    private Long alarm;
    private String repeat;
    private String color;

    public MyTime() {
    }

    public MyTime(Long id) {
        this.id = id;
    }

    public MyTime(Long id, String timecode, int timetype, String name, Integer year, Integer monthofyear, Integer dayofmonth, int dayofweek, int starthour, int startmin, int endhour, int endmin, Long startmillis, Long endmillis, String memo, String place, Double lat, Double lng, Integer share, Long alarm, String repeat, String color) {
        this.id = id;
        this.timecode = timecode;
        this.timetype = timetype;
        this.name = name;
        this.year = year;
        this.monthofyear = monthofyear;
        this.dayofmonth = dayofmonth;
        this.dayofweek = dayofweek;
        this.starthour = starthour;
        this.startmin = startmin;
        this.endhour = endhour;
        this.endmin = endmin;
        this.startmillis = startmillis;
        this.endmillis = endmillis;
        this.memo = memo;
        this.place = place;
        this.lat = lat;
        this.lng = lng;
        this.share = share;
        this.alarm = alarm;
        this.repeat = repeat;
        this.color = color;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getTimecode() {
        return timecode;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setTimecode(String timecode) {
        this.timecode = timecode;
    }

    public int getTimetype() {
        return timetype;
    }

    public void setTimetype(int timetype) {
        this.timetype = timetype;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonthofyear() {
        return monthofyear;
    }

    public void setMonthofyear(Integer monthofyear) {
        this.monthofyear = monthofyear;
    }

    public Integer getDayofmonth() {
        return dayofmonth;
    }

    public void setDayofmonth(Integer dayofmonth) {
        this.dayofmonth = dayofmonth;
    }

    public int getDayofweek() {
        return dayofweek;
    }

    public void setDayofweek(int dayofweek) {
        this.dayofweek = dayofweek;
    }

    public int getStarthour() {
        return starthour;
    }

    public void setStarthour(int starthour) {
        this.starthour = starthour;
    }

    public int getStartmin() {
        return startmin;
    }

    public void setStartmin(int startmin) {
        this.startmin = startmin;
    }

    public int getEndhour() {
        return endhour;
    }

    public void setEndhour(int endhour) {
        this.endhour = endhour;
    }

    public int getEndmin() {
        return endmin;
    }

    public void setEndmin(int endmin) {
        this.endmin = endmin;
    }

    public Long getStartmillis() {
        return startmillis;
    }

    public void setStartmillis(Long startmillis) {
        this.startmillis = startmillis;
    }

    public Long getEndmillis() {
        return endmillis;
    }

    public void setEndmillis(Long endmillis) {
        this.endmillis = endmillis;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Integer getShare() {
        return share;
    }

    public void setShare(Integer share) {
        this.share = share;
    }

    public Long getAlarm() {
        return alarm;
    }

    public void setAlarm(Long alarm) {
        this.alarm = alarm;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

}