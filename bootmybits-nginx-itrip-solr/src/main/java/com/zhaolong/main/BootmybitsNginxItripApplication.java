package com.zhaolong.main;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan(basePackages = {"com.zhaolong.mapper"})
@ComponentScan(basePackages = {"com.zhaolong.po","com.zhaolong.service","com.zhaolong.controller"})
public class BootmybitsNginxItripApplication {

    public static void main(String[] args) {

        SpringApplication.run(BootmybitsNginxItripApplication.class, args);
    }

}