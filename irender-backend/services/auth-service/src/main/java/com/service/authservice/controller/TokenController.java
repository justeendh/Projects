package com.service.authservice.controller;

import com.service.authservice.dto.*;
import com.common.irendersecurity.model.JwtToken;
import com.service.authservice.service.UserService;
import com.common.irendersecurity.service.JwtTokenFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(value="Token Managerment", description="Cung cấp các thông tin đăng nhập, và thông tin người dùng")
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

    @ApiOperation(value = "Đăng nhập hệ thống và lấy token")
    @PostMapping("/login")
    public ResponseEntity<UserToken> login(@RequestBody LoginForm loginForm) {
        UserDto userDto = userService.login(loginForm);
        if (userDto != null) {
            UserToken userToken = this.buildUserToken(userDto);
            return ResponseEntity.ok(userToken);
        }
        return ResponseEntity.badRequest().build();
    }

    @ApiOperation(value = "Làm mới thông tin đăng nhập, lấy refresh token")
    @PostMapping("/refresh")
    public ResponseEntity<UserToken> refresh(@RequestBody RefreshForm refreshForm) {
        JwtToken refreshToken = tokenFactory.createRefreshToken(refreshForm.getRefreshToken());
        String subject = refreshToken.getClaims().getSubject();
        UserDto userDto = userService.findTopByUserName(subject);
        UserToken userToken = this.buildUserToken(userDto);
        return ResponseEntity.ok(userToken);
    }

    @ApiOperation(value = "Lấy thông tin chi tiết user")
    @GetMapping("/detail")
    public ResponseEntity<UserDto> detailInfo(@RequestHeader HttpHeaders headers) {
        log.info(headers.get("Authorization") + "" + headers.get("Authentication") + "" + headers.get("authentication") + "" + headers.get("authorization"));
        List<String> auth = headers.get("Authorization");
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

    @ApiOperation(value = "Lấy thông tin thời gian hiện tại của hệ thống")
    @GetMapping("/time")
    public ResponseEntity<TimeDto> time(@RequestHeader HttpHeaders headers) {
        return ResponseEntity.ok(TimeDto.builder().currentTime(System.currentTimeMillis()).build());
    }

    private UserToken buildUserToken(UserDto userDto) {
        UserToken userToken = new UserToken(userDto);
        JwtToken accessToken = tokenFactory.createAccessJwtToken(userDto.getUsername(), userDto.getUserId());
        JwtToken newRefreshToken = tokenFactory.createRefreshToken(userDto.getUsername(), userDto.getUserId());
        userToken.setAccessToken(accessToken.getToken());
        userToken.setRefreshToken(newRefreshToken.getToken());
        userToken.setExpiredTime(accessToken.getExpirationTime());
        return userToken;
    }
}
