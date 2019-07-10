package com.service.tranferservice.config;

import com.common.commonstorage.uploadfile.config.EnableCommonStorage;
import com.common.commonuploadfile.config.EnableCommonUploadFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCommonUploadFile
@EnableCommonStorage
@Slf4j
public class ApplicationConfig {
}
