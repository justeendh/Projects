package com.cloud.proxy.proxyservice.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class GatewayApi {
    @Autowired
    private ZuulProperties zuulProperties;

    @Value("${zuul.prefix}")
    private String zuulPrefix;

    @Primary
    @Bean
    public SwaggerResourcesProvider swaggerResourcesProvider() {
        return () -> {
            List<SwaggerResource> resources = new ArrayList<>();
            zuulProperties
                    .getRoutes()
                    .values()
                    .forEach(route -> {
                        if (route.getPath().endsWith("/**") && !route.getId().equals("legacy")) {
                            resources.add(createResource(route.getServiceId(), route.getId(), route.getPath()));
                        }
                    });
            return resources;
        };
    }

    private SwaggerResource createResource(String serviceId, String routeId, String routePath) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(serviceId + ": " + routeId);
        swaggerResource.setLocation(zuulPrefix + routePath.substring(0, routePath.length() - 3) + "/v2/api-docs");
        swaggerResource.setSwaggerVersion("2.0");
        return swaggerResource;
    }
}
