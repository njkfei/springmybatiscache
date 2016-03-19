package com.niejinkun.spring.springcache.dao;


import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.niejinkun.spring.springcache.model.User;



/**
 * 用户表相关操作
 * @author sanhao
 *
 */
public interface UserDAO {
	@Select("select * from ysyy_user where user_id=#{user_id} ")
	@Options(useCache = true,timeout = 100000,flushCache = false)
	User getUser(@Param("user_id")int user_id);
	
	@Update("update ysyy_user set `user_type`=#{user_type} where user_id=#{user_id}")
	int updateUser(@Param("user_id")int user_id,@Param("user_type")int user_type);
}
