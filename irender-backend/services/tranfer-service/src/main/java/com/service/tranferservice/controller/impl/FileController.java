package com.service.tranferservice.controller.impl;

import com.common.commonuploadfile.dto.FileDto;
import com.common.commonuploadfile.service.UploadFileService;
import com.service.tranferservice.controller.FileApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;

@Transactional
@RestController
@RequestMapping("/file")
public class FileController implements FileApi {

  private final UploadFileService uploadFileService;

  @Autowired
  public FileController(UploadFileService uploadFileService) {
    this.uploadFileService = uploadFileService;
  }

  @Override
  public ResponseEntity<FileDto> uploadFile(String keyName, MultipartFile file) {
    FileDto fileDto = uploadFileService.uploadFile(keyName, file);
    if (fileDto != null) {
      return ResponseEntity.ok().body(fileDto);
    } else {
      return ResponseEntity.badRequest().build();
    }
  }

  @Override
  public ResponseEntity<byte[]> downloadFile(String fileUrl) {
    ByteArrayOutputStream downloadInputStream = uploadFileService.downloadFile(fileUrl);
    int index = fileUrl.lastIndexOf('/');
    String fileName = fileUrl;
    if (index >= 0) {
      fileName = fileUrl.substring(index);
    }
    if (downloadInputStream == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok()
        .contentType(contentType(fileName))
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
        .body(downloadInputStream.toByteArray());
  }

  private MediaType contentType(String keyName) {
    String[] arr = keyName.split("\\.");
    String type = arr[arr.length - 1];
    switch (type) {
      case "txt":
        return MediaType.TEXT_PLAIN;
      case "png":
        return MediaType.IMAGE_PNG;
      case "jpg":
        return MediaType.IMAGE_JPEG;
      case "pdf":
        return MediaType.APPLICATION_PDF;
      default:
        return MediaType.APPLICATION_OCTET_STREAM;
    }
  }
}
