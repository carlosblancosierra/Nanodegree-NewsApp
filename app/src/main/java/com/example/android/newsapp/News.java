package com.example.android.newsapp;

/**
 * Created by carlosblanco on 12/19/16.
 */

public class News {

    // Fields
    /* New's title */
    private String mTitle;
    /* New's Date */
    private String mDate;
    /* New's Section */
    private String mSection;
    /* New's WebUrl */
    private String mWebUrl;

    /**
     * Constructs a new {@link News} object.
     * @param title is the title of the news
     * @param date is the date the news was published
     * @param section is the section of the news
     * @param url is the url of the news in the guardian webpage
     */
    public News(String title, /* String author,*/ String date, String section, String url) {
        mTitle = title;
        mDate = date;
        mSection = section;
        mWebUrl = url;
    }

    /**
     * @return the title of the news
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * @return the date of the news
     */
    public String getDate() {
        return mDate;
    }

    /**
     * @return the section of the news
     */
    public String getSection() {
        return mSection;
    }

    /**
     * @return the url of the news
     */
    public String getWebUrl() {
        return mWebUrl;
    }


}
