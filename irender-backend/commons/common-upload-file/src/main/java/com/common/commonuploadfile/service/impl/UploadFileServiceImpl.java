package com.common.commonuploadfile.service.impl;

import com.common.commonuploadfile.config.CommonFileUploadConfig;
import com.common.commonuploadfile.dto.FileDto;
import com.common.commonuploadfile.model.FileInfo;
import com.common.commonuploadfile.service.UploadFileService;
import com.common.irenderqueue.service.QueueService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
public class UploadFileServiceImpl implements UploadFileService {

  private static final Logger LOGGER = LoggerFactory.getLogger(UploadFileServiceImpl.class);

  private static final String IO_EXCEPTION = "IOException ";

  private static final String DIRECTORY_NOT_EMPTY_EXCEPTION = "DirectoryNotEmptyException";

  private static final String NO_SUCH_FILE_EXCEPTION = "NoSuchFileException";

  private static final String DELETE_FILE = "Delete file ";

  private static final String SUCCESS = " Success!";

  private static final String DEFAULT_FOLDER = "Topkid";

  @Value("${queue.upload-file.exchange}")
  private String exchange;

  @Value("${queue.upload-file.routingKey}")
  private String routingKey;

  private final QueueService queueService;

  private final CommonFileUploadConfig commonFileUploadConfig;

  @Autowired
  public UploadFileServiceImpl(CommonFileUploadConfig commonFileUploadConfig, QueueService queueService) {
    this.commonFileUploadConfig = commonFileUploadConfig;
    this.queueService = queueService;
  }

  private Path getOrCreateNewPath(String folderName) {
    String format = "yyyy-MM-dd";
    DateFormat dateFormatter = new SimpleDateFormat(format);
    String date = dateFormatter.format(new Date());
    Path currentDatePath = Paths.get(commonFileUploadConfig.getUploadDir() + folderName + date);
    if (!currentDatePath.toFile().exists()) {
      try {
        return Files.createDirectories(currentDatePath);
      } catch (IOException e) {
        LOGGER.error(IO_EXCEPTION, e.getMessage());
      }
    }
    return currentDatePath;
  }

  @Override
  public FileDto uploadFile(String folderName, MultipartFile file) {
    if (folderName != null && !folderName.isEmpty()) {
      folderName += "/";
    } else {
      folderName = DEFAULT_FOLDER + "/";
    }
    Path pathOnToDay = getOrCreateNewPath(folderName);
    String fullFileName = StringUtils.cleanPath(UUID.randomUUID().toString())
        + "."
        + FilenameUtils.getExtension(file.getOriginalFilename());
    try {
      Path targetLocation = pathOnToDay.resolve(fullFileName);
      Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
      Path absolutePath = targetLocation.toAbsolutePath();
      String fullPathStr = absolutePath.toString();
      if (File.separatorChar != '/') {
        fullPathStr = fullPathStr.replace(File.separatorChar, '/');
      }
      String pathStr = fullPathStr.substring(fullPathStr.indexOf(folderName));
      return FileDto.builder().url(pathStr)
          .fullS3Url(commonFileUploadConfig.getS3Url() + pathStr)
          .fullServerUrl(fullPathStr)
          .build();
    } catch (IOException ex) {
      LOGGER.error(IO_EXCEPTION, ex.getMessage());
    }
    return null;
  }

  @Override
  public ByteArrayOutputStream downloadFile(String url) {
    File file = new File(commonFileUploadConfig.getUploadDir() + url);
    try (InputStream is = new FileInputStream(file)) {
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      int len;
      byte[] buffer = new byte[4096];
      while ((len = is.read(buffer, 0, buffer.length)) != -1) {
        byteArrayOutputStream.write(buffer, 0, len);
      }
      return byteArrayOutputStream;
    } catch (IOException ioe) {
      LOGGER.error(IO_EXCEPTION, ioe.getMessage());
    }
    return null;
  }


  public void uploadFileS3(FileInfo fileInfo) {
    queueService.send(exchange, routingKey, fileInfo);
  }


  public void deleteFileUrl(String url) {
    try {
      Files.deleteIfExists(Paths.get(commonFileUploadConfig.getUploadDir() + url));
      LOGGER.info(DELETE_FILE, SUCCESS);
    } catch (NoSuchFileException e) {
      LOGGER.error(NO_SUCH_FILE_EXCEPTION, e.getMessage());
    } catch (DirectoryNotEmptyException e) {
      LOGGER.error(DIRECTORY_NOT_EMPTY_EXCEPTION, e.getMessage());
    } catch (IOException e) {
      LOGGER.error(IO_EXCEPTION, e.getMessage());
    }
  }

  @Override
  public boolean deleteDirectory(File directoryToBeDeleted) {
    File[] allContents = directoryToBeDeleted.listFiles();
    if (allContents != null) {
      for (File file : allContents) {
        deleteDirectory(file);
      }
    }
    boolean result = false;
    try {
      result = Files.deleteIfExists(directoryToBeDeleted.toPath());
    } catch (IOException e) {
      LOGGER.debug(IO_EXCEPTION, e.getMessage());
    }
    return result;
  }
}
