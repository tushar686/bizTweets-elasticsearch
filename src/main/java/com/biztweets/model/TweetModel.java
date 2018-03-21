package com.biztweets.model;

public class TweetModel {

    private final Tweet tweet;
    private final String howMuchAgo;
    private final String tooltipForAgo;
    private final String id;

    public TweetModel(final Tweet tweet, final String howMuchAgo, final String tooltipForAgo, final String id) {
        super();
        this.tweet = tweet;
        this.howMuchAgo = howMuchAgo;
        this.tooltipForAgo = tooltipForAgo;
        this.id = id;
    }

    public Tweet getTweet() {
        return tweet;
    }

    public String getHowMuchAgo() {
        return howMuchAgo;
    }

    public String getTooltipForAgo() {
        return tooltipForAgo;
    }

    public String getId() {
        return id;
    }

}
