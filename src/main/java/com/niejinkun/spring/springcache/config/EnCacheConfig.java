package com.niejinkun.spring.springcache.config;

import java.net.URL;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.sf.ehcache.CacheManager;

/*@Configuration
@EnableCaching
// @ImportResource("XXX.XML") 
public class EnCacheConfig 
{
	  @Bean
	  public CacheManager cacheManager(){
		 URL url = getClass().getResource("classpath:conf/ehcache.xml");  
		 CacheManager manager = CacheManager.create(url);  
		 for (String cacheName : manager.getCacheNames()) {
			    System.out.println(cacheName);
			}
		 return manager;
	  }
  
	  @Bean
	  public Cache userCache(){
		  return cacheManager().getCache("userCache");
	  }
}*/
