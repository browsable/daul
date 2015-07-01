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
        @JsonProperty("name")
        private String name;
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
    }
    public List<Data> getData() {
        return data;
    }
    public void setData(List<Data> data) {
        this.data = data;
    }

}