package com.example.karthik.listview;


import android.graphics.Bitmap;

public class NewsFeed {

    private Bitmap uAvatar;
    private String uTitle;
    private String uDescription;

    public NewsFeed(Bitmap avatar, String title, String description) {
        uAvatar = avatar;
        uTitle = title;
        uDescription = description;

    }

    public void setAvatar(Bitmap avatar) {
        uAvatar = avatar;
    }
    public Bitmap getAvatar() {
        return uAvatar;
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