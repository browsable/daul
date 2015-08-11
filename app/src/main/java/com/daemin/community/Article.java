package com.daemin.community;

/**
 * Created by Jun-yeong on 2015-08-10.
 */
public class Article {
    private static String title;
    private static String date;
    private static String userId;
    private static String content;

    public static String getTitle() {
        return title;
    }

    public static String getDate() {
        return date;
    }

    public static String getUserId() {
        return userId;
    }

    public static String getContent() {
        return content;
    }

    public static void setTitle(String title) {
        Article.title = title;
    }

    public static void setDate(String date) {
        Article.date = date;
    }

    public static void setUserId(String userId) {
        Article.userId = userId;
    }

    public static void setContent(String content) {
        Article.content = content;
    }
}
