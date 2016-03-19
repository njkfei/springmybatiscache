# springmybatiscache
## DEMO说明
　本示例通过REST控制器接收外界数据请求，rest控制器通过mybatis向数据库请求数据并缓存中cache当中。
　通过测试表示，如果没有cache，通过ab请求时，将导致mysql连接不可用。使用缓存后，上游读请求将不会把mysql压跨。
　技术要素如下：
* mybatis 　：提供ORM框架
* cache　：缓存查询数据
* restcontroller　：　提供web服务
* springboot : 内嵌tomcat web服务支持
 本DEMO使用了二级缓存。第一级在REST请求处。
 第二级在MYBATIS查询处。
 
## maven依赖
```
		<!-- spring boot -->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-tomcat</artifactId>
			<scope>provided</scope>
		</dependency>
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-webmvc</artifactId>
		</dependency>

		<dependency>
			<groupId>org.mybatis</groupId>
			<artifactId>mybatis-spring</artifactId>
			<version>1.2.3</version>
			<type>jar</type>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-jdbc</artifactId>
		</dependency>

		<!-- mysql驱动 -->
		<dependency>
			<groupId>mysql</groupId>
			<artifactId>mysql-connector-java</artifactId>
		</dependency>
		<dependency>
			<groupId>org.mybatis</groupId>
			<artifactId>mybatis</artifactId>
			<version>3.3.0</version>
		</dependency>
```

##　CACHE配置
配置cache,使用spring自带的缓存框架ConcurrentMapCache即可。
```
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
```

## REST控制器缓存配置
### 加入依赖
```
@RestController
@EnableAutoConfiguration
@Cacheable // 这条注解不能少
public class UserController {}
```

### 理新一次缓存@Cacheable
@Cacheable使用名为"userCache"的缓存，如果缓存中没有数据，则读取数据库后加入缓存。
```
	@Cacheable(value = "userCache") // 使用了一个缓存名叫 userCache
	@RequestMapping("/user2/{user_id}")
	@ResponseBody
	public User getUser(@PathVariable  int user_id) {
		// 方法内部实现不考虑缓存逻辑，直接实现业务
		System.out.println("real query user." + user_id);
		return userDAO.getUser(user_id);
	}
```
### 每次更新缓存@CachePut
@CachePut每次访问时，都会先读数据库，再更新缓存。因此，这条注解适合update的场景。
下面的测试代码，实际上相当于缓存失效了。
```
	@CachePut(value = "userCache") // 使用了一个缓存名叫 userCache
	@RequestMapping("/user3/{user_id}")
	@ResponseBody
	public User getUser3(@PathVariable  int user_id) {
		// 方法内部实现不考虑缓存逻辑，直接实现业务
		System.out.println("real query user." + user_id);
		return userDAO.getUser(user_id);
	}
```

### 删除单个缓存@CacheEvict
最好加上key,否则可能没有清除缓存

```
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
```
	
### 删除整个缓存
```
	// 删除全部缓存
	@CacheEvict(value="userCache",allEntries=true,beforeInvocation=true)
	@RequestMapping("/clear")
	public String clear() {
		// 方法内部实现不考虑缓存逻辑，直接实现业务
		System.out.println("clearn cache.");
		return "clear ok";
	}
```
## MYBATIS数据库缓存
加上注解__@Options(useCache = true,timeout = 100000,flushCache = false)__即可。

```
	@Select("select * from ysyy_user where user_id=#{user_id} ")
	@Options(useCache = true,timeout = 100000,flushCache = false)
	User getUser(@Param("user_id")int user_id);
```

## 总结
通过性能测试工具ab测试表示，如果不使用缓存，在QPS为40时，MYSQL就扛不住了。
加上缓存后，服务运行稳定。


ab 测试命令行为：
```
ab -c 100 -n 1000000 http://192.168.1.236:8080/user3/972
```

测试结果为：

```
This is ApacheBench, Version 2.3 <$Revision: 655654 $>
Copyright 1996 Adam Twiss, Zeus Technology Ltd, http://www.zeustech.net/
Licensed to The Apache Software Foundation, http://www.apache.org/

Benchmarking 192.168.1.236 (be patient)
apr_socket_recv: Connection reset by peer (104)
Total of 17953 requests completed
[niejinping@dev-office-dev-192.168.1.19.centos65.sanhao.com ~]$ab -c 100 -n 1000000 http://192.168.1.236:8080/user3/972
This is ApacheBench, Version 2.3 <$Revision: 655654 $>
Copyright 1996 Adam Twiss, Zeus Technology Ltd, http://www.zeustech.net/
Licensed to The Apache Software Foundation, http://www.apache.org/

Benchmarking 192.168.1.236 (be patient)
^C

Server Software:        Apache-Coyote/1.1
Server Hostname:        192.168.1.236
Server Port:            8080

Document Path:          /user3/972
Document Length:        29 bytes

Concurrency Level:      100
Time taken for tests:   15.923 seconds
Complete requests:      9466
Failed requests:        0
Write errors:           0
Total transferred:      1675482 bytes
HTML transferred:       274514 bytes
Requests per second:    594.47 [#/sec] (mean)
Time per request:       168.218 [ms] (mean)
Time per request:       1.682 [ms] (mean, across all concurrent requests)
Transfer rate:          102.75 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    1   0.7      1      16
Processing:     4  162 245.8     30    1825
Waiting:        4  162 245.6     30    1824
Total:          5  163 245.9     31    1825

Percentage of the requests served within a certain time (ms)
  50%     31
  66%    122
  75%    246
  80%    318
  90%    493
  95%    689
  98%    971
  99%   1129
 100%   1825 (longest request)
```
