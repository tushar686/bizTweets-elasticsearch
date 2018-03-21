package com.biztweets.model;

public class Follow {

    private String user;
    private String followQuery;

    public Follow() {
    }

    public Follow(final String user, final String followQuery) {
        super();
        this.user = user;
        this.followQuery = followQuery;
    }

    public String getUser() {
        return user;
    }

    public String getFollowQuery() {
        return followQuery;
    }

    @Override
    public String toString() {
        return "Follow [user=" + user + ", followQuery=" + followQuery + "]";
    }

}
