package com.common.irendersecurity.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class JwtSettings {
  @Value("${app.security.jwt.tokenExpirationTime}")
  private Integer tokenExpirationTime;

  @Value("${app.security.jwt.tokenIssuer}")
  private String tokenIssuer;

  @Value("${app.security.jwt.tokenSigningKey}")
  private String tokenSigningKey;

  @Value("${app.security.jwt.refreshTokenExpTime}")
  private Integer refreshTokenExpTime;

  public Integer getRefreshTokenExpTime() {
    return refreshTokenExpTime;
  }

  public Integer getTokenExpirationTime() {
    return tokenExpirationTime;
  }

  public String getTokenIssuer() {
    return tokenIssuer;
  }

  public String getTokenSigningKey() {
    return tokenSigningKey;
  }

}
