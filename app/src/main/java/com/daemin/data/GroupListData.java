package com.daemin.data;

/**
 * Created by hernia on 2015-06-20.
 */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GroupListData {
    @JsonProperty("data")
    private List<Data> data;

    public static class Data {
        @JsonProperty("korname")
        private String korname;
        @JsonProperty("engname")
        private String engname;
        @JsonProperty("when")
        private String when;

        public String getWhen() {
            return when;
        }

        public void setWhen(String when) {
            this.when = when;
        }

        public String getEngname() {
            return engname;
        }

        public void setEngname(String engname) {
            this.engname = engname;
        }

        public String getKorname() {
            return korname;
        }

        public void setKorname(String korname) {
            this.korname = korname;
        }
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

}