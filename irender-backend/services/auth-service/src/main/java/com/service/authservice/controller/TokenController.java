package com.service.authservice.controller;

import com.service.authservice.dto.*;
import com.service.authservice.model.JwtToken;
import com.service.authservice.service.UserService;
import com.service.authservice.service.impl.JwtTokenFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/auth")
@RestController
@Slf4j
public class TokenController {

    private final JwtTokenFactory tokenFactory;

    private final UserService userService;

    @Value("${app.time-zone.default:Asia/Ha_Noi}")
    private String countryCode;

    @Value("${app.token.header:token}")
    private String tokenHeader;

    @Value("${app.security.jwt.tokenSigningKey}")
    private String tokenSigningKey;

    @Autowired
    public TokenController(JwtTokenFactory tokenFactory, UserService userService) {
        this.tokenFactory = tokenFactory;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserToken> login(@RequestBody LoginForm loginForm) {
        UserDto userDto = userService.login(loginForm);
        if (userDto != null) {
            UserToken userToken = this.buildUserToken(userDto);
            return ResponseEntity.ok(userToken);
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<UserToken> refresh(@RequestBody RefreshForm refreshForm) {
        JwtToken refreshToken = tokenFactory.createRefreshToken(refreshForm.getRefreshToken());
        String subject = refreshToken.getClaims().getSubject();
        UserDto userDto = userService.findTopByUserName(subject);
        UserToken userToken = this.buildUserToken(userDto);
        return ResponseEntity.ok(userToken);
    }

    @GetMapping("/detail")
    public ResponseEntity<UserDto> detailInfo(@RequestHeader HttpHeaders headers) {
        log.info(headers.get("Authorization") + "" + headers.get("Authentication") + "" + headers.get("authentication") + "" + headers.get("authorization"));
        List<String> auth = headers.get("Authentication");
        if (auth == null || auth.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        String username = tokenFactory.getUsernameFromToken(auth.get(0).replaceAll("Bearer\\s", ""));
        UserDto userDto = userService.findTopByUserName(username);
        if (userDto != null) {
            return ResponseEntity.ok(userDto);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/time")
    public ResponseEntity<TimeDto> time(@RequestHeader HttpHeaders headers) {
        return ResponseEntity.ok(TimeDto.builder().currentTime(System.currentTimeMillis()).build());
    }

    private UserToken buildUserToken(UserDto userDto) {
        UserToken userToken = new UserToken(userDto);
        JwtToken accessToken = tokenFactory.createAccessJwtToken(userDto);
        JwtToken newRefreshToken = tokenFactory.createRefreshToken(userDto);
        userToken.setAccessToken(accessToken.getToken());
        userToken.setRefreshToken(newRefreshToken.getToken());
        userToken.setExpiredTime(accessToken.getExpirationTime());
        return userToken;
    }
}
