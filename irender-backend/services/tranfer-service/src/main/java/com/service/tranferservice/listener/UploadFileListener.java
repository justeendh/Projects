package com.service.tranferservice.listener;

import com.common.commonstorage.uploadfile.service.S3Services;
import com.common.irenderqueue.dto.FileInfo;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UploadFileListener {

  @Autowired
  S3Services s3Services;

  @RabbitListener(queues = "${queue.upload-file.queue}")
  public void receivedMessage(FileInfo fileInfo) {
    s3Services.uploadFile(fileInfo);
  }
}
