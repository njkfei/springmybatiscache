package com.niejinkun.spring.springcache.model;

import java.math.BigDecimal;

/**
 * 用户表
 * @author sanhao
 *
 */
public class User {
	private int user_id;
	private int user_type;

	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public int getUser_type() {
		return user_type;
	}
	public void setUser_type(int user_type) {
		this.user_type = user_type;
	}
	
}
