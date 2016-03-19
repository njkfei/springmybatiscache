package com.niejinkun.spring.springcache.config;

import java.util.Arrays;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableCaching
public class CacheConfig 
{

  
  @Bean
  public SimpleCacheManager cacheManager(){
	  SimpleCacheManager cacheManager = new SimpleCacheManager();
	  cacheManager.setCaches(Arrays.asList(userCache(),defaultCache()));
	  return cacheManager;
  }
  
  @Bean(name="userCache")
  public ConcurrentMapCache userCache(){
	  ConcurrentMapCache userCache = new ConcurrentMapCache("userCache");
	  return  userCache;
  }
  
  @Bean
  public ConcurrentMapCache defaultCache(){
	  ConcurrentMapCache defaultCache = new ConcurrentMapCache("defaultCache");
	  return  defaultCache;
  }
}
