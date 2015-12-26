package com.jobs.matomesan;

public class listItem {
    private String postTitle;
    private String postLink;
    private String postSiteTitle;
    private String postDate;

    public listItem(String siteTitle, String title, String link, String date) {
        this.postTitle = title;
        this.postLink = link;
        this.postDate = date;
        this.postSiteTitle = siteTitle;
    }

    public String getTitle() {
        return postTitle;
    }

    public String getLink() {
        return postLink;
    }

    public String getDate() {
        return postDate;
    }

    public String getSiteName() {
        return postSiteTitle;
    }
}