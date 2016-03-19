package com.niejinkun.spring.springcache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.niejinkun.spring.springcache.config.CacheConfig;
import com.niejinkun.spring.springcache.config.MybatisConfig;
import com.niejinkun.spring.springcache.controller.UserController;

@SpringBootApplication
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
@Import({UserController.class,CacheConfig.class,MybatisConfig.class})
//@Import({UserController.class,EnCacheConfig.class,MybatisConfig.class})
public class App 
{
  public static void main( String[] args )
  {
  	 SpringApplication.run(App.class, args);
  }
}
