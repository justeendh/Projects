package com.service.tranferservice.controller;

import com.common.commonuploadfile.dto.FileDto;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Api(value = "admin `File`")
public interface FileApi {


  @PostMapping("/uploadFile")
  @ApiOperation(
      value = "Upload File ", nickname = "uploadFile", notes = "Upload File",
      authorizations = {@Authorization(value = "JwtToken")}, tags = {})
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Upload file success!")
  })
  @ApiImplicitParam(
      name = "Accept-Language", value = "i18n", required = true,
      paramType = "header", dataTypeClass = String.class, example = "Locale")
  ResponseEntity<FileDto> uploadFile(@RequestParam("keyName") String keyName, @RequestParam("uploadFile") MultipartFile file);

  @GetMapping("/downloadFile")
  @ApiOperation(
      value = "Download File ", nickname = "downloadFile", notes = "Download File",
      authorizations = {@Authorization(value = "JwtToken")}, tags = {})
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Download file success!")
  })
  @ApiImplicitParam(
      name = "Accept-Language", value = "i18n", required = true,
      paramType = "header", dataTypeClass = String.class, example = "Locale")
  ResponseEntity<byte[]> downloadFile(@RequestParam("fileUrl") String fileUrl);

}
