package com.common.irendercore.util;

import com.common.irendercore.constant.MyZuulHeaders;
import com.common.irendercore.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Component
public class ContextUtil {

  @Autowired
  private HttpServletRequest request;


  public String getCurrentUser() {
    return request.getHeader(MyZuulHeaders.USER_ID);
  }

  public UserInfo getUserInfo() {
    UserInfo userInfo = new UserInfo();
    userInfo.setUserId(getCurrentUser());
    userInfo.setUsername(request.getHeader(MyZuulHeaders.USER_NAME));
    String roles = request.getHeader(MyZuulHeaders.ROLE);
    userInfo.setRoles(Arrays.asList(roles.split(",")));
    return userInfo;
  }
}
