package com.jobs.matomesan;

public class ListItem {
    private String postTitle;
    private String postLink;
    private String postSiteTitle;
    private String postDate;

    public ListItem(String siteTitle, String link) {
        this.postSiteTitle = siteTitle;
        this.postLink = link;
    }

    public ListItem(String siteTitle, String title, String link, String date) {
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