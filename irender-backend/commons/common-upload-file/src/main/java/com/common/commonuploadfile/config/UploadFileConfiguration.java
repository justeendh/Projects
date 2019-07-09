package com.common.commonuploadfile.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan({
    "com.common.commonuploadfile.service.impl",
    "com.common.commonuploadfile.config",
    "com.common.commonuploadfile.service"
})
@Configuration
@Slf4j
public class UploadFileConfiguration {

}
