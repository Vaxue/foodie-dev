package com.imooc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    public CorsConfig() {
    }
    @Bean
    public CorsFilter corsFilter(){
        // 1 添加cors配置信息
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:8080"); //配置前端请求调用端地址
        config.setAllowCredentials(true); // 设置是否允许携带cookie信息
        config.addAllowedMethod("*"); // 设置放行的请求方法 GET POST
        config.addAllowedHeader("*"); //设置允许的header

        // 2 为url添加映射路径
        UrlBasedCorsConfigurationSource corsSource = new UrlBasedCorsConfigurationSource();
        corsSource.registerCorsConfiguration("/**",config);

        //3 返回重新定义好的cors
        return new CorsFilter(corsSource);
    }
}
