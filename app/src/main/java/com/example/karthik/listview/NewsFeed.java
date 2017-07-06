package com.example.karthik.listview;


public class NewsFeed {

    private String uUrl;
    private String uTitle;
    private String uDescription;

    public NewsFeed(String url, String title, String description) {
        uUrl = url;
        uTitle = title;
        uDescription = description;

    }

    public void setUrl(String url) {
        uUrl = url;
    }

    public String getUrl() {
        return uUrl;
    }

    public void setTitle(String title) {
        uTitle = title;
    }
    public String getuTitle() {
        return uTitle;
    }

    public void setDescription(String description) {
        uDescription = description;
    }
    public String getDescription() {
        return uDescription;
    }

}