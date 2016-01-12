package com.daemin.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by user on 2015-12-19.
 */
public class UpdateListData {
    @JsonProperty("status")
    private String status;
    @JsonProperty("data")
    private List<Data> data;
    @JsonProperty("message")
    private String message;
    public static class Data {
        @JsonProperty("current_version")
        private String current_version;
        @JsonProperty("update_history")
        private String update_history;
        public String getCurrent_version() {
            return current_version;
        }
        public String getUpdate_history() {
            return update_history;
        }
    }
    public String getStatus() {
        return status;
    }
    public String getMessage() {
        return message;
    }
    public List<Data> getData() {
        return data;
    }
    public void setData(List<Data> data) {
        this.data = data;
    }
}
