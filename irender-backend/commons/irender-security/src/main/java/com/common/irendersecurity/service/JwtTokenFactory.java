package com.common.irendersecurity.service;

import com.common.irendersecurity.config.JwtSettings;
import com.common.irendersecurity.constant.TokenType;
import com.common.irendersecurity.exception.InvalidJwtTokenException;
import com.common.irendersecurity.exception.JwtExpiredTokenException;
import com.common.irendersecurity.model.JwtToken;
import io.jsonwebtoken.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JwtTokenFactory {

  private static final String TYPE = "type";

  @Autowired
  private JwtSettings settings;

  public JwtToken createAccessJwtToken(String username, String userId) {
    if (StringUtils.isBlank(username)) {
      throw new IllegalArgumentException("Cannot create JWT Token without userName");
    }
    Claims claims = Jwts.claims().setSubject(username);
    LocalDateTime currentTime = LocalDateTime.now();
    claims.put(TYPE, TokenType.ACCESS_TOKEN.toString());
    String token = Jwts.builder()
        .setClaims(claims)
        .setIssuer(settings.getTokenIssuer())
        .setId(userId)
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

  public JwtToken createRefreshToken(String username, String userId) {
    if (StringUtils.isBlank(username)) {
      throw new IllegalArgumentException("Cannot create JWT Token without userName");
    }
    LocalDateTime currentTime = LocalDateTime.now();
    Claims claims = Jwts.claims().setSubject(username);
    claims.put(TYPE, TokenType.REFRESH_TOKEN.toString());
    String token = Jwts.builder()
        .setClaims(claims)
        .setIssuer(settings.getTokenIssuer())
        .setId(userId)
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
