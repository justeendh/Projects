package com.service.tranferservice.config;

import com.common.commonstorage.uploadfile.config.EnableCommonStorage;
import com.common.commonuploadfile.config.EnableCommonUploadFile;
import com.common.irenderqueue.config.EnableIrenderQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableIrenderQueue
@EnableCommonUploadFile
@EnableCommonStorage
@Slf4j
public class ApplicationConfig {
}
