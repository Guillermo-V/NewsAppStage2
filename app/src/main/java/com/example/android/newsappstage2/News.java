package com.example.android.newsappstage2;

public class News {

    private String msection;

    private String mTitle;

    private String mWebPublicationDate;

    private String mUrl;

    String mAuthor;


    public News(String title, String section, String WebPublicationDate, String url, String author) {
        msection = section;
        mTitle = title;
        mWebPublicationDate = WebPublicationDate;
        mUrl = url;
        mAuthor = author;
    }

    public String getmsection() {
        return msection;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmWebPublicationDate() {
        return mWebPublicationDate;
    }

    public String getmUrl() {
        return mUrl;
    }

    public String getmAuthor() {
        return mAuthor;
    }


}