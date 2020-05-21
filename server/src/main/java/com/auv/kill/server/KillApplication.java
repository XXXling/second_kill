package com.auv.kill.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @description:
 * @EnableScheduling 注解是开启定时任务
 * @author: huining
 * @time: 2020/5/10 18:13
 */
@SpringBootApplication
@ImportResource(value = {"classpath:spring/spring-jdbc.xml"})
@MapperScan(basePackages = "com.auv.kill.model.mapper")
@EnableScheduling
public class KillApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(KillApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(KillApplication.class,args);
    }
}
