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
        @JsonProperty("db_version")
        private String db_version;
        public String getKo() {
            return ko;
        }
        public String getTt_version() {
            return tt_version;
        }

        public String getDb_version() {
            return db_version;
        }
    }
    public List<Data> getData() {
        return data;
    }
    public void setData(List<Data> data) {
        this.data = data;
    }
}
