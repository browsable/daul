package com.daemin.community;

/**
 * Created by Jun-yeong on 2015-08-10.
 */
public class Post {
    private static boolean isWritten;
    private static String title;
    private static String date;
    private static String time;
    private static String userId;
    private static String content;

    public Post() {
        isWritten = false;
    }

    public static boolean isWritten() { return isWritten; }

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

    public static String getTime() { return time; }

    public static void setTime(String time) { Post.time = time; }

    public static void setIsWritten(boolean isWritten) {
        Post.isWritten = isWritten;
    }

    public static void setTitle(String title) {
        Post.title = title;
    }

    public static void setDate(String date) {
        Post.date = date;
    }

    public static void setUserId(String userId) {
        Post.userId = userId;
    }

    public static void setContent(String content) {
        Post.content = content;
    }
}
