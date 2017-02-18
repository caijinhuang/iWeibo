package com.iweibo.pojo;

import android.graphics.drawable.Drawable;

public class User {
	private long _id = 0;
	private String user_id;//用户uid
	private String user_name;//用户名
	private String taken = "";
	private String access_taken = "";
	private String description;//个人描述
	private Drawable user_head = null;//用户头像
	public User(){}
	
	public User(String user_id,String user_name,String taken,
			String access_taken,String description) {
		this.user_id = user_id;
		this.user_name = user_name;
		this.taken = taken;
		this.access_taken = access_taken;
		this.description = description;
	} 	
	
	public User(String user_id,String user_name,String taken,
			String access_taken,String description,Drawable user_head) {
		this( user_id, user_name, taken, access_taken, description);
		this.user_head = user_head;
	}
	
	@Override
	public String toString() {
		return "User [_id=" + _id + ", user_id=" + user_id + ", user_name=" + user_name + ", taken=" + taken
				+ ", access_taken=" + access_taken + ", description=" + description + ", user_head=" + "]";
	}
	
	public long get_id() {
		return _id;
	}
	public void set_id(long _id) {
		this._id = _id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getTaken() {
		return taken;
	}
	public void setTaken(String taken) {
		this.taken = taken;
	}
	public String getAccess_taken() {
		return access_taken;
	}
	public void setAccess_taken(String access_taken) {
		this.access_taken = access_taken;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Drawable getUser_head() {
		return user_head;
	}
	public void setUser_head(Drawable user_head) {
		this.user_head = user_head;
	}
}
