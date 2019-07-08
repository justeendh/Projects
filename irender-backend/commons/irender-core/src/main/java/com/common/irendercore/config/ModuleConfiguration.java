package com.common.irendercore.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@ComponentScan({
    "com.common.irendercore.config",
    "com.common.irendercore.controller",
    "com.common.irendercore.util"
})
@Configuration
@EnableSwagger2
@Slf4j
public class ModuleConfiguration {

}
