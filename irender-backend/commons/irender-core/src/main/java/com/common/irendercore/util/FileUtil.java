package com.common.irendercore.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FileUtil {

  @Value("${irender_upfile.s3.url}")
  private String s3Url;

  private FileUtil() {

  }

  public String getFullUrlS3(String url) {
    if (url != null && !url.isEmpty()) {
      return s3Url + (s3Url.endsWith("/") ? "" : "/") + url;
    }
    return null;
  }
}
