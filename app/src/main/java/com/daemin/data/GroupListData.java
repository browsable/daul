package com.daemin.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by user on 2015-12-19.
 */
public class GroupListData {
    @JsonProperty("status")
    private String status;
    @JsonProperty("data")
    private List<Data> data;
    @JsonProperty("message")
    private String message;
    public static class Data {
        @JsonProperty("no")
        private Integer no;
        @JsonProperty("en")
        private String en;
        @JsonProperty("ko")
        private String ko;
        @JsonProperty("tt_version")
        private String tt_version;
        public Integer getNo() {
            return no;
        }
        public void setNo(Integer no) {
            this.no = no;
        }
        public String getEn() {
            return en;
        }
        public void setEn(String en) {
            this.en = en;
        }
        public String getKo() {
            return ko;
        }
        public void setKo(String ko) {
            this.ko = ko;
        }
        public String getTt_version() {
            return tt_version;
        }
        public void setTt_version(String tt_version) {
            this.tt_version = tt_version;
        }
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public List<Data> getData() {
        return data;
    }
    public void setData(List<Data> data) {
        this.data = data;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
