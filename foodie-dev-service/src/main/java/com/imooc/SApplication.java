package com.imooc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.imooc.mapper")
public class SApplication {
    public static void main(String[] args) {
        SpringApplication.run(SApplication.class,args);
    }
}
