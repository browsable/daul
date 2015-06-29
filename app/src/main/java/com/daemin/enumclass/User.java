package com.daemin.enumclass;

import java.util.List;

/**
 * Created by hernia on 2015-06-27.
 */
public enum User {
    USER;
    private int groupindex;
    private int groupdepindex;
    private int userKey; // php에서 넘겨받는 userid
    private String group;
    private String groupDep;
    private String phoneNum; //Hash 값으로 변환후 저장
    private String email; // Hash 값으로 변환후 저장
    private String passwd;
    private List<TimePos> timePosList;

    User() {}

    public List<TimePos> getTimePosList() {
        return timePosList;
    }

    public void setTimePosList(List<TimePos> timePosList) {
        this.timePosList = timePosList;
    }

    public int getGroupindex() {
        return groupindex;
    }

    public void setGroupindex(int groupindex) {
        this.groupindex = groupindex;
    }

    public int getGroupdepindex() {
        return groupdepindex;
    }

    public void setGroupdepindex(int groupdepindex) {
        this.groupdepindex = groupdepindex;
    }

    public int getUserKey() {
        return userKey;
    }

    public void setUserKey(int userKey) {
        this.userKey = userKey;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getGroupDep() {
        return groupDep;
    }

    public void setGroupDep(String groupDep) {
        this.groupDep = groupDep;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

}
