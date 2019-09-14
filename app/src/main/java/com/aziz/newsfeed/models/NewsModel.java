package com.aziz.newsfeed.models;

import android.util.Log;

public class NewsModel {
    private static final String TAG = "NewsModel MainActivity";

    private String section;
    private String title;
    private String thumbnail;
    private String trail;
    private String date;
    private String author;
    private String webUrl;

    public NewsModel(String section, String title, String thumbnail, String trail, String data, String author, String webUrl) {
        Log.d(TAG, "NewsModel() called with: section = [" + section + "], title = [" + title + "], thumbnail = [" + thumbnail + "], trail = [" + trail + "], data = [" + data + "], author = [" + author + "], webUrl = [" + webUrl + "]");

        this.section = section;
        this.title = title;
        this.thumbnail = thumbnail;
        this.trail = trail;
        this.date = data;
        this.author = author;
        this.webUrl = webUrl;
    }

    public String getSection() {
        Log.d(TAG, "getSection() called");
        return section;
    }

    public String getTitle() {
        Log.d(TAG, "getTitle() called");
        return title;
    }

    public String getThumbnail() {
        Log.d(TAG, "getThumbnail() called");
        return thumbnail;
    }

    public String getTrail() {
        Log.d(TAG, "getTrail() called");
        return trail;
    }

    public String getDate() {
        Log.d(TAG, "getDate() called");
        return date;
    }

    public String getAuthor() {
        Log.d(TAG, "getAuthor() called");
        return author;
    }

    public String getWebUrl() {
        Log.d(TAG, "getWebUrl() called");
        return webUrl;
    }
}
