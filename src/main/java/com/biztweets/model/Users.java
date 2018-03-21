package com.biztweets.model;

public class Users {
	private String user;
	private Follow follow;
	
	public Users() {}
	
	public Users(String user, Follow follow) {
		super();
		this.user = user;
		this.follow = follow;
	}

	public String getUser() {
		return user;
	}

	public Follow getFollow() {
		return follow;
	}
	
}
