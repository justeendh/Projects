package com.common.commonstorage.uploadfile.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan({
    "com.common.commonstorage.uploadfile.service",
    "com.common.commonstorage.uploadfile.config"
})
@Configuration
@Slf4j
public class CommonStorageConfiguration {

}
