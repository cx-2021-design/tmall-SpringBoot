package com.example.tmall;

import com.example.tmall.util.PortUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 作者：cjy
 * 类名：TmallApplication
 * 全路径类名：com.example.tmall.TmallApplication
 * 父类或接口：
 * 描述：商城主类
 */
@SpringBootApplication
@EnableTransactionManagement
@EnableCaching
@EnableElasticsearchRepositories(basePackages = "com.example.tmall.es")
@EnableJpaRepositories(basePackages = {"com.example.tmall.dao", "com.example.tmall.pojo"})
public class TmallApplication {

	public static void main(String[] args) {
		SpringApplication.run(TmallApplication.class, args);
	}

	//端口检查
	static {
		PortUtil.checkPort(3306,"mysql 服务端",true);
		PortUtil.checkPort(6379,"Redis 服务端",true);
		PortUtil.checkPort(9300,"ElasticSearch 服务端",true);
	}

}
