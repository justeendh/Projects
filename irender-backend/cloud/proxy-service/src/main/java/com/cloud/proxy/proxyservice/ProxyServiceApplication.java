package com.cloud.proxy.proxyservice;

import com.cloud.proxy.filter.RouteLoggingFilter;
import com.cloud.proxy.filter.RouterAuthFilter;
import com.common.irendersecurity.config.JwtSettings;
import com.common.irendersecurity.service.JwtTokenFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableZuulProxy
@EnableSwagger2
public class ProxyServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProxyServiceApplication.class, args);
    }

    @Bean
    @Primary
    public RouteLoggingFilter routeLoggingFilter() {
        return new RouteLoggingFilter();
    }

    @Bean
    @Primary
    public RouterAuthFilter routerAuthFilter() {
        return new RouterAuthFilter();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtTokenFactory jwtTokenFactory() {
        return new JwtTokenFactory();
    }

    @Bean
    public JwtSettings jwtSettings() {
        return new JwtSettings();
    }

}
