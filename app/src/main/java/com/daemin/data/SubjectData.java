package com.daemin.data;

/**
 * Created by hernia on 2015-07-02.
 */
public class SubjectData {
    private String _id;
    private String subnum;
    private String subtitle;
    private String prof;
    private String credit;
    private String classnum; //분반
    private String limitnum; //제한수
    private String dep_detail; //이수형태
    private String dep_grade; //대상학년

    public SubjectData(){
    }
    public SubjectData(int _id,String subnum, String subtitle, String prof, int credit,int classnum, int limitnum ,String dep_detail, String dep_grade) {
        this._id = String.valueOf(_id);
        this.subnum = subnum;
        this.subtitle = subtitle;

        this.prof = prof;
        this.dep_grade = dep_grade+"학년";
        this.dep_detail = dep_detail;
        this.limitnum = String.valueOf(limitnum);
        this.classnum = String.valueOf(classnum);
        this.credit = String.valueOf(credit);
    }
    public String get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = String.valueOf(_id);
    }
    public String getSubnum() {
        return subnum;
    }

    public void setSubnum(String subnum) {
        this.subnum = subnum;
    }
    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getProf() {
        return prof;
    }

    public void setProf(String prof) {
        this.prof = prof;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit =  String.valueOf(credit);
    }

    public String getClassnum() {
        return classnum;
    }

    public void setClassnum(int classnum) {
        this.classnum =  String.valueOf(classnum);
    }

    public String getLimitnum() {
        return limitnum;
    }

    public void setLimitnum(int limitnum) {
        this.limitnum =  String.valueOf(limitnum);
    }

    public String getDep_detail() {
        return dep_detail;
    }

    public void setDep_detail(String dep_detail) {
        this.dep_detail = dep_detail;
    }

    public String getDep_grade() {
        return dep_grade;
    }

    public void setDep_grade(String dep_grade) {
        this.dep_grade = dep_grade;
    }
}