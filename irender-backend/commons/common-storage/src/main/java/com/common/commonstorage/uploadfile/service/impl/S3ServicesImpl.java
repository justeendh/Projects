package com.common.commonstorage.uploadfile.service.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.common.commonstorage.uploadfile.service.S3Services;
import com.common.irenderqueue.dto.FileInfo;
import com.common.irenderqueue.service.QueueService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Service
@Slf4j
public class S3ServicesImpl implements S3Services {


  private Logger logger = LoggerFactory.getLogger(S3ServicesImpl.class);

  private final AmazonS3 s3client;

  private final AmazonS3Client s3clients;

  private final QueueService queueService;

  @Value("${queue.upload-file.retry.exchange}")
  private String retryExchange;

  @Value("${queue.upload-file.retry.routingKey}")
  private String retryRoutingKey;

  @Value("${file-storage.upload-folder}")
  private String uploadDir;

  @Value("${irender_upfile.s3.bucket}")
  private String bucketName;

  private static final int RETRY_COUNT_LIMIT = 5;

  private static final String IO_EXCEPTION = "IOException ";

  private static final String AMAZON_SERVICE_EXCEPTION = "AmazonServiceException ";

  private static final String SDK_CLIENT_EXCEPTION = "SdkClientException ";

  private static final String UPLOAD_FILE_S3 = "Upload file s3 ";

  private static final String DELETE_FILE_S3 = "Delete file s3 ";

  private static final String SUCCESS = " Success!";

  @Autowired
  public S3ServicesImpl(AmazonS3 s3client, AmazonS3Client s3clients, QueueService queueService) {
    this.s3client = s3client;
    this.s3clients = s3clients;
    this.queueService = queueService;
  }


  public ByteArrayOutputStream downloadFile(String keyName) {
    try {
      S3Object s3object = s3client.getObject(new GetObjectRequest(bucketName, keyName));
      InputStream is = s3object.getObjectContent();
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      int len;
      byte[] buffer = new byte[4096];
      while ((len = is.read(buffer, 0, buffer.length)) != -1) {
        byteArrayOutputStream.write(buffer, 0, len);
      }
      return byteArrayOutputStream;
    } catch (IOException ioe) {
      logger.error(IO_EXCEPTION, ioe.getMessage());
    } catch (AmazonServiceException ase) {
      logger.error(AMAZON_SERVICE_EXCEPTION, ase.getMessage());
    } catch (SdkClientException sce) {
      logger.error(SDK_CLIENT_EXCEPTION, sce.getMessage());
    }
    return null;
  }


  public String uploadFile(String keyName, MultipartFile file) {
    try {
      ObjectMetadata metadata = new ObjectMetadata();
      metadata.setContentLength(file.getSize());
      s3clients.putObject(new PutObjectRequest(bucketName, keyName, file.getInputStream(), metadata));
      return s3clients.getResourceUrl(bucketName, keyName);
    } catch (AmazonServiceException ase) {
      logger.error(AMAZON_SERVICE_EXCEPTION, ase.getMessage());
    } catch (SdkClientException sce) {
      logger.error(SDK_CLIENT_EXCEPTION, sce.getMessage());
    } catch (IOException e) {
      logger.error(IO_EXCEPTION, e.getMessage());
    }
    return null;
  }


  public void uploadFile(FileInfo fileInfo) {
    try {
      if (fileInfo.getUrl() != null && !fileInfo.getUrl().isEmpty()) {
        File file = new File(uploadDir + fileInfo.getUrl());
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.length());
        try (InputStream inputStream = new FileInputStream(file)) {
          s3clients.putObject(new PutObjectRequest(bucketName, fileInfo.getUrl(), inputStream, metadata));
          logger.info(UPLOAD_FILE_S3, SUCCESS);
        }
      }
    } catch (AmazonServiceException ase) {
      logger.error(SDK_CLIENT_EXCEPTION, ase.getMessage());
      this.retry(fileInfo);
    } catch (SdkClientException sce) {
      logger.error(SDK_CLIENT_EXCEPTION, sce.getMessage());
    } catch (IOException e) {
      logger.error(IO_EXCEPTION, e.getMessage());
    }
  }

  @Override
  public void deleteFile(String keyName) {
    try {
      s3client.deleteObject(bucketName, keyName);
      logger.info(DELETE_FILE_S3, SUCCESS);
    } catch (AmazonServiceException ase) {
      logger.error(AMAZON_SERVICE_EXCEPTION, ase.getMessage());
    } catch (SdkClientException sce) {
      logger.error(SDK_CLIENT_EXCEPTION, sce.getMessage());
    }
  }

  private void retry(FileInfo fileInfo) {
    if (fileInfo.getRetryCount() < RETRY_COUNT_LIMIT) {
      fileInfo.setRetryCount(fileInfo.getRetryCount() + 1);
      queueService.send(retryExchange, retryRoutingKey, fileInfo);
    }
  }
}
