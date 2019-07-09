package com.cloud.proxy.filter;

import com.common.irendercore.constant.MyZuulHeaders;
import com.common.irendersecurity.exception.InvalidJwtTokenException;
import com.common.irendersecurity.exception.JwtExpiredTokenException;
import com.common.irendersecurity.model.JwtToken;
import com.common.irendersecurity.service.JwtTokenFactory;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
public class RouterAuthFilter extends ZuulFilter {

    private static final String API_DOC_URL = "/v2/api-docs";

    @Value("${zuul.prefix}")
    private String prefix;

    @Value("${app.auth.enableAuth}")
    private boolean enableAuth;

    private String authUrl = "/auth/auth/login";

    @Autowired
    private JwtTokenFactory jwtTokenFactory;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.PRE_DECORATION_FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String url = request.getRequestURI();
        if (!this.isIgnoreUrl(url)) {
            try {
                String accessToken = getAccessToken(request.getHeader("Authorization"));
                JwtToken jwtToken = jwtTokenFactory.createAccessJwtToken(accessToken);
                String userId = jwtToken.getClaims().getId();
                ctx.addZuulRequestHeader(MyZuulHeaders.USER_ID, userId);
            } catch (InvalidJwtTokenException | JwtExpiredTokenException ex) {
                log.info("(authFilter) exception: {}", ExceptionUtils.getFullStackTrace(ex));
                if (enableAuth) {
                    ctx.setSendZuulResponse(false);
                    ctx.setResponseBody(ex.getMessage());
                    ctx.setResponseStatusCode(HttpStatus.SC_UNAUTHORIZED);
                }
            }
        }
        return null;
    }

    private String getAccessToken(String header) {
        if (header != null && header.startsWith("Bearer ")) {
            return header.replaceAll("Bearer\\s", "");
        }
        return null;
    }

    private boolean isIgnoreUrl(String url) {
        if (url == null) return false;
        else return (url.contains(API_DOC_URL) || url.contains(prefix + authUrl));
    }

}