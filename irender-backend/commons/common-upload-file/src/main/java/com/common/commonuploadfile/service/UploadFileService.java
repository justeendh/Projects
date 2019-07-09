package com.common.commonuploadfile.service;

import com.common.commonuploadfile.dto.FileDto;
import com.common.commonuploadfile.model.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;

public interface UploadFileService {

  /**
   * @param folderName the table
   * @param file multipartFile
   * @return
   */
  FileDto uploadFile(String folderName, MultipartFile file);

  /**
   *
   * @param url
   * @return
   */
  ByteArrayOutputStream downloadFile(String url);

  void uploadFileS3(FileInfo fileInfo);

  void deleteFileUrl(String url);

  boolean deleteDirectory(File directoryToBeDeleted);

}
