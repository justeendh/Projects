package com.service.authservice.service.impl;

import com.service.authservice.config.JwtSettings;
import com.service.authservice.constant.TokenType;
import com.service.authservice.dto.UserDto;
import com.service.authservice.exception.InvalidJwtTokenException;
import com.service.authservice.exception.JwtExpiredTokenException;
import com.service.authservice.model.JwtToken;
import io.jsonwebtoken.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JwtTokenFactory {

  private static final String TYPE = "type";

  private final JwtSettings settings;

  @Autowired
  public JwtTokenFactory(JwtSettings settings) {
    this.settings = settings;
  }

  public JwtToken createAccessJwtToken(UserDto user) {
    String username = user.getUsername();
    if (StringUtils.isBlank(username)) {
      throw new IllegalArgumentException("Cannot create JWT Token without userName");
    }
    Claims claims = Jwts.claims().setSubject(username);
    LocalDateTime currentTime = LocalDateTime.now();
    claims.put(TYPE, TokenType.ACCESS_TOKEN.toString());
    String token = Jwts.builder()
        .setClaims(claims)
        .setIssuer(settings.getTokenIssuer())
        .setId(user.getUserId())
        .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
        .setExpiration(Date.from(currentTime
            .plusSeconds(settings.getTokenExpirationTime())
            .atZone(ZoneId.systemDefault()).toInstant()))
        .signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey())
        .compact();
    return new JwtToken(token, claims, settings.getTokenExpirationTime());
  }

  public JwtToken createAccessJwtToken(String token) {
    Claims claims = this.getAllClaimsFromToken(token);
    if (claims.get("type").equals(TokenType.ACCESS_TOKEN.toString())) {
      return new JwtToken(token, claims, settings.getTokenExpirationTime());
    } else {
      throw new InvalidJwtTokenException(InvalidJwtTokenException.MESSAGE);
    }
  }

  public JwtToken createRefreshToken(UserDto user) {
    String username = user.getUsername();
    if (StringUtils.isBlank(username)) {
      throw new IllegalArgumentException("Cannot create JWT Token without userName");
    }
    LocalDateTime currentTime = LocalDateTime.now();
    Claims claims = Jwts.claims().setSubject(username);
    claims.put(TYPE, TokenType.REFRESH_TOKEN.toString());
    String token = Jwts.builder()
        .setClaims(claims)
        .setIssuer(settings.getTokenIssuer())
        .setId(user.getUserId())
        .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
        .setExpiration(Date.from(currentTime
            .plusSeconds(settings.getRefreshTokenExpTime())
            .atZone(ZoneId.systemDefault()).toInstant()))
        .signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey())
        .compact();
    return new JwtToken(token, claims, settings.getRefreshTokenExpTime());
  }

  public JwtToken createRefreshToken(String token) {
    Claims claims = this.getAllClaimsFromToken(token);
    if (claims.get(TYPE).equals(TokenType.REFRESH_TOKEN.toString())) {
      return new JwtToken(token, claims, settings.getTokenExpirationTime());
    } else {
      throw new InvalidJwtTokenException(InvalidJwtTokenException.MESSAGE);
    }
  }

  public String getUsernameFromToken(String token) {
    return getAllClaimsFromToken(token).getSubject();
  }

  private Claims getAllClaimsFromToken(String token) {
    if (token != null) {
      try {
        return Jwts.parser()
            .setSigningKey(settings.getTokenSigningKey())
            .parseClaimsJws(token)
            .getBody();
      } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException ex) {
        throw new InvalidJwtTokenException(InvalidJwtTokenException.MESSAGE);
      } catch (ExpiredJwtException ex) {
        throw new JwtExpiredTokenException(JwtExpiredTokenException.MESSAGE);
      }
    } else {
      throw new InvalidJwtTokenException(InvalidJwtTokenException.MESSAGE);
    }
  }
}
