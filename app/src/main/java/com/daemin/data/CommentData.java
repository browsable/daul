package com.daemin.data;

/**
 * Created by Jun-yeong on 2015-07-06.
 */
public class CommentData {
    private int article_id;
    private String body;
    private String date;
    private String userId;

    public CommentData(int article_id, String body, String date, String userId) {
        this.article_id = article_id;
        this.body = body;
        this.date = date;
        this.userId = userId;
    }

    public int getArticle_id() {
        return article_id;
    }
    public String getBody() {
        return body;
    }
    public String getDate() {
        return date;
    }
    public String getUserId() {
        return userId;
    }
    public void setArticle_id(int article_id) {
        this.article_id = article_id;
    }
    public void setBody(String body) {
        this.body = body;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
}