package com.daemin.community.github;

/**
 * Created by hernia on 2015-06-20.
 */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FreeBoard {

    @JsonProperty("status")
    private String status;
    @JsonProperty("data")
    private List<Data> data;
    @JsonProperty("message")
    private String message;

   public static class Data {
        @JsonProperty("no")
        private Integer no;
        @JsonProperty("account_no")
        private Integer account_no;
        @JsonProperty("good")
        private Integer good;
        @JsonProperty("bad")
        private Integer bad;
        @JsonProperty("read")
        private Integer read;
        @JsonProperty("passwd")
        private String passwd;
        @JsonProperty("title")
        private String title;
        @JsonProperty("body")
        private String body;
        @JsonProperty("img")
        private String img;
        @JsonProperty("when")
        private String when;
        @JsonProperty("ip")
        private String ip;
        public Integer getNo() {
            return no;
        }
        public void setNo(Integer no) {
            this.no = no;
        }
        public Integer getAccount_no() {
            return account_no;
        }
        public void setAccount_no(Integer account_no) {
            this.account_no = account_no;
        }
        public Integer getGood() {
            return good;
        }
        public void setGood(Integer good) {
            this.good = good;
        }
        public Integer getBad() {
            return bad;
        }
        public void setBad(Integer bad) {
            this.bad = bad;
        }
        public Integer getRead() {
            return read;
        }
        public void setRead(Integer read) {
            this.read = read;
        }
        public String getPasswd() {
            return passwd;
        }
        public void setPasswd(String passwd) {
            this.passwd = passwd;
        }
        public String getTitle() {
            return title;
        }
        public void setTitle(String title) {
            this.title = title;
        }
        public String getBody() {
            return body;
        }
        public void setBody(String body) {
            this.body = body;
        }
        public String getImg() {
            return img;
        }
        public void setImg(String img) {
            this.img = img;
        }
        public String getWhen() {
            return when;
        }
        public void setWhen(String when) {
            this.when = when;
        }
        public String getIp() {
            return ip;
        }
        public void setIp(String ip) {
            this.ip = ip;
        }

       @Override
       public String toString() {
           return "Data{" +
                   "no=" + no +
                   ", account_no=" + account_no +
                   ", good=" + good +
                   ", bad=" + bad +
                   ", read=" + read +
                   ", passwd='" + passwd + '\'' +
                   ", title='" + title + '\'' +
                   ", body='" + body + '\'' +
                   ", img='" + img + '\'' +
                   ", when='" + when + '\'' +
                   ", ip='" + ip + '\'' +
                   '}'+'\n';
       }
   }
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("status : " + getStatus() + "\n");
        //b.append("data : " + getData().toString()+ "\n");
        b.append("message : " + getMessage() + "\n");
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