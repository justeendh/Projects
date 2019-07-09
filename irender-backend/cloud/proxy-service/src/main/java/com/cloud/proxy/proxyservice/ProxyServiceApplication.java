package com.cloud.proxy.proxyservice;

import com.cloud.proxy.proxyservice.filter.RouteLoggingFilter;
import com.cloud.proxy.proxyservice.filter.RouterAuthFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
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
    public RouteLoggingFilter routeLoggingFilter() {
        return new RouteLoggingFilter();
    }

    @Bean
    public RouterAuthFilter routerAuthFilter() {
        return new RouterAuthFilter();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
