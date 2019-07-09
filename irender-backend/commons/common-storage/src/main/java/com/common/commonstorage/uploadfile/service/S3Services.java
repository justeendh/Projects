package com.common.commonstorage.uploadfile.service;

import com.common.irenderqueue.dto.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;

public interface S3Services {

  ByteArrayOutputStream downloadFile(String keyName);

  String uploadFile(String keyName, MultipartFile file);

  void uploadFile(FileInfo fileInfo);

  void deleteFile(String keyName);

}
