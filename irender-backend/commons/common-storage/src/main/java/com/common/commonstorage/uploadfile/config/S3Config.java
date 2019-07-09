package com.common.commonstorage.uploadfile.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {
  @Value("${irender_upfile.aws.access_key_id}")
  private String awsId;

  @Value("${irender_upfile.aws.secret_access_key}")
  private String awsKey;

  @Value("${irender_upfile.s3.region}")
  private String region;

  @Bean
  public AmazonS3 s3client() {

    BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(awsId, awsKey);
    return AmazonS3Client.builder()
        .withRegion(Regions.fromName(region))
        .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
        .build();

  }

  @Bean
  public AmazonS3Client s3clients() {

    BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(awsId, awsKey);
    return (AmazonS3Client) AmazonS3ClientBuilder.standard()
        .withRegion(Regions.fromName(region))
        .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
        .build();
  }


}
