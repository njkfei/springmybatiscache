package com.niejinkun.spring.springcache.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.niejinkun.spring.springcache.dao.UserDAO;
import com.niejinkun.spring.springcache.model.Account;
import com.niejinkun.spring.springcache.model.User;

@RestController
@EnableAutoConfiguration
@Cacheable
public class UserController {
	@Autowired
	private UserDAO userDAO;
	
	@Cacheable(value = "userCache") // 使用了一个缓存名叫 userCache
	@RequestMapping("/user/{userName}")
	@ResponseBody
	public Account getAccountByName(@PathVariable  String userName) {
		// 方法内部实现不考虑缓存逻辑，直接实现业务
		System.out.println("real query account." + userName);
		return getFromDB(userName);
	}

	private Account getFromDB(String acctName) {
		System.out.println("real querying db..." + acctName);
		return new Account(acctName);
	}
	
	@Cacheable(value = "userCache") // 使用了一个缓存名叫 userCache
	@RequestMapping("/user2/{user_id}")
	@ResponseBody
	public User getUser(@PathVariable  int user_id) {
		// 方法内部实现不考虑缓存逻辑，直接实现业务
		System.out.println("real query user." + user_id);
		return userDAO.getUser(user_id);
	}
	
	@CachePut(value = "userCache") // 使用了一个缓存名叫 userCache
	@RequestMapping("/user3/{user_id}")
	@ResponseBody
	public User getUser3(@PathVariable  int user_id) {
		// 方法内部实现不考虑缓存逻辑，直接实现业务
		System.out.println("real query user." + user_id);
		return userDAO.getUser(user_id);
	}
	
	// 删除全部缓存
	@CacheEvict(value="userCache",allEntries=true,beforeInvocation=true)
	@RequestMapping("/clear")
	public String clear() {
		// 方法内部实现不考虑缓存逻辑，直接实现业务
		System.out.println("clearn cache.");
		return "clear ok";
	}
	
	//　删除部分缓存
	@CacheEvict(value="userCache",beforeInvocation=true)
	@RequestMapping("/clearuser/{user_id}")
	public String clearUser(@PathVariable  int user_id) {
		// 方法内部实现不考虑缓存逻辑，直接实现业务
		System.out.println("clearn cache.　user_id : " + user_id);
		return "clear user_id " + user_id + " ok";
	}
	
	// @CachePut 主要针对方法配置，能够根据方法的请求参数对其结果进行缓存，和 @Cacheable 不同的是，它每次都会触发真实方法的调用
	//　删除部分缓存
	@CacheEvict(value="userCache",key="#user_id",beforeInvocation=true)
	@RequestMapping("/update/{user_id}/{user_type}")
	@ResponseBody
	public User updateUser(@PathVariable  int user_id,@PathVariable  int user_type) {
		// 方法内部实现不考虑缓存逻辑，直接实现业务
		System.out.println("UPDATE cache.　user_id : " + user_id);
		userDAO.updateUser(user_id,user_type);
		return userDAO.getUser(user_id);
	}
	
}