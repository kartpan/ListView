package com.example.karthik.listview;


public class NewsArticles {

    private String uTitle;
    private String uSubTitle;
    private String uURL;
    private String uImageURL;

    public NewsArticles(String title, String subtitle, String url, String imageurl) {
        uTitle = title;
        uSubTitle = subtitle;
        uURL = url;
        uImageURL = imageurl;

    }

    public void setTitle(String title) {
        uTitle = title;
    }

    public String getTitle() {
        return uTitle;
    }

    public void setSubTitle(String subTitle) {
        uSubTitle = subTitle;
    }

    public String getSubTitle() {
        return uSubTitle;
    }

    public void setURL(String url) {
        uURL = url;
    }

    public String getURL() {
        return uURL;
    }

    public void setImageURL(String imageurl) {
        uImageURL = imageurl;
    }

    public String getImageURL() {
        return uImageURL;
    }

}