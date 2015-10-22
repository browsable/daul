package com.daemin.data;

/**
 * Created by hernia on 2015-06-20.
 */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PostData {

    @JsonProperty("status")
    private String status;
    @JsonProperty("data")
    private List<Data> data;
    @JsonProperty("message")
    private String message;

    public static class Data {
        public Data(Integer no, Integer account_no, String nickname, String a_img,
                    String title, Integer good, Integer bad, Integer read, String date,
                    String time, Integer cmt, String body_t, String img_t) {
            this.no = no;
            this.account_no = account_no;
            this.nickname = nickname;
            this.a_img = a_img;
            this.title = title;
            this.good = good;
            this.bad = bad;
            this.read = read;
            this.date = date;
            this.time = time;
            this.cmt = cmt;
            this.body_t = body_t;
            this.img_t = img_t;
        }

        @JsonProperty("no")
        private Integer no;
        @JsonProperty("account_no")
        private Integer account_no;
        @JsonProperty("nickname")
        private String nickname;
        @JsonProperty("a_img")
        private String a_img;
        @JsonProperty("title")
        private String title;
        @JsonProperty("good")
        private Integer good;
        @JsonProperty("bad")
        private Integer bad;
        @JsonProperty("read")
        private Integer read;
        @JsonProperty("date")
        private String date;
        @JsonProperty("time")
        private String time;
        @JsonProperty("cmt")
        private Integer cmt;
        @JsonProperty("body_t")
        private String body_t;
        @JsonProperty("img_t")
        private String img_t;
        public void setNo(Integer no) {
            this.no = no;
        }
        public void setAccount_no(Integer account_no) {
            this.account_no = account_no;
        }
        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
        public void setA_img(String a_img) {
            this.a_img = a_img;
        }
        public void setTitle(String title) {
            this.title = title;
        }
        public void setGood(Integer good) {
            this.good = good;
        }
        public void setBad(Integer bad) {
            this.bad = bad;
        }
        public void setRead(Integer read) {
            this.read = read;
        }
        public void setDate(String date) {
            this.date = date;
        }
        public void setTime(String time) {
            this.time = time;
        }
        public void setCmt(Integer cmt) {
            this.cmt = cmt;
        }
        public void setBody_t(String body_t) {
            this.body_t = body_t;
        }
        public void setImg_t(String img_t) {
            this.img_t = img_t;
        }
        public Integer getNo() {
            return no;
        }
        public Integer getAccount_no() {
            return account_no;
        }
        public String getNickname() {
            return nickname;
        }
        public String getA_img() {
            return a_img;
        }
        public String getTitle() {
            return title;
        }
        public Integer getGood() {
            return good;
        }
        public Integer getBad() {
            return bad;
        }
        public Integer getRead() {
            return read;
        }
        public String getDate() {
            return date;
        }
        public String getTime() {
            return time;
        }
        public Integer getCmt() {
            return cmt;
        }
        public String getBody_t() {
            return body_t;
        }
        public String getImg_t() {
            return img_t;
        }
        @Override
        public String toString() {
            return "Data{" +
                    "no=" + no +
                    ", account_no=" + account_no +
                    ", nickname='" + nickname + '\'' +
                    ", a_img='" + a_img + '\'' +
                    ", title='" + title + '\'' +
                    ", good=" + good +
                    ", bad=" + bad +
                    ", read=" + read +
                    ", date='" + date + '\'' +
                    ", time='" + time + '\'' +
                    ", cmt=" + cmt +
                    ", body_t='" + body_t + '\'' +
                    ", img_t='" + img_t + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("status : " + getStatus() + "\n");
        b.append("message : " + getMessage() + "\n");
        b.append("data : " + getData().toString()+ "\n");
        return b.toString();
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public List<Data> getData() {
        return data;
    }
    public void setData(List<Data> data) {
        this.data = data;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}