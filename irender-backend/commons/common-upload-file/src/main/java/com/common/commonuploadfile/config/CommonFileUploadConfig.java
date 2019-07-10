package com.common.commonuploadfile.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class CommonFileUploadConfig {

  @Value("${file-storage.upload-folder}")
  private String uploadDir;

  @Value("${irender_upfile.s3.url}")
  private String s3Url;

}

