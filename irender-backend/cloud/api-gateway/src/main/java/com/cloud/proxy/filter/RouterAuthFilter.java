//package com.cloud.proxy.filter;
//
//import com.ctc.wstx.util.ExceptionUtil;
//import com.netflix.zuul.ZuulFilter;
//import com.netflix.zuul.context.RequestContext;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.http.HttpStatus;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
//import org.springframework.http.HttpMethod;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.stream.Collectors;
//
//@Slf4j
//@Component
//public class RouterAuthFilter extends ZuulFilter {
//
//    private static final String API_DOC_URL = "/v2/api-docs";
//
//    @Value("${zuul.prefix}")
//    private String prefix;
//
//    @Value("${app.auth.enableAuth}")
//    private boolean enableAuth;
//
//    @Value("${app.auth.enableAuthor}")
//    private boolean enableAuthor;
//
//    private String authUrl = "/auth";
//
//    private JwtTokenFactory jwtTokenFactory;
//
//    private ApiService apiService;
//
//    private RoleService roleService;
//
//    @Autowired
//    RouterAuthFilter(
//            JwtTokenFactory jwtTokenFactory, ApiService apiService, RoleService roleService) {
//        this.jwtTokenFactory = jwtTokenFactory;
//        this.apiService = apiService;
//        this.roleService = roleService;
//    }
//
//    @Override
//    public String filterType() {
//        return FilterConstants.PRE_TYPE;
//    }
//
//    @Override
//    public int filterOrder() {
//        return FilterConstants.PRE_DECORATION_FILTER_ORDER;
//    }
//
//    @Override
//    public boolean shouldFilter() {
//        return true;
//    }
//
//    @Override
//    public Object run() {
//        RequestContext ctx = RequestContext.getCurrentContext();
//        HttpServletRequest request = ctx.getRequest();
//        String url = request.getRequestURI();
//        if (!this.isIgnoreUrl(url))
//            try {
//                String accessToken = getAccessToken(request.getHeader("Authentication"));
//                JwtToken jwtToken = jwtTokenFactory.createAccessJwtToken(accessToken);
//                String userId = jwtToken.getClaims().getId();
//                ctx.addZuulRequestHeader(MyZuulHeaders.USER_ID, userId);
//                String httpMethod = request.getMethod();
//                if (enableAuthor) {
//                    if (userId != null
//                            && apiService.userIsAuthor(userId, url, HttpMethod.resolve(httpMethod))) {
//                        List<RoleDto> roles = roleService.findAllByUserId(userId);
//                        ctx.addZuulRequestHeader(
//                                MyZuulHeaders.ROLE,
//                                roles.stream().map(RoleDto::getCode).collect(Collectors.joining(",")));
//                    } else {
//                        ctx.setSendZuulResponse(false);
//                        ctx.setResponseStatusCode(HttpStatus.SC_FORBIDDEN);
//                    }
//                }
//            } catch (InvalidJwtTokenException | JwtExpiredTokenException ex) {
//                log.info("(authFilter) exception: {}", ExceptionUtil.getFullStackTrace(ex, true));
//                if (enableAuth) {
//                    ctx.setSendZuulResponse(false);
//                    ctx.setResponseStatusCode(HttpStatus.SC_UNAUTHORIZED);
//                }
//            }
//        return null;
//    }
//
//    private String getAccessToken(String header) {
//        if (header != null && header.startsWith("Bearer ")) {
//            return header.replaceAll("Bearer\\s", "");
//        }
//        return null;
//    }
//
//    private boolean isIgnoreUrl(String url) {
//        if (url == null) {
//            return false;
//        } else {
//            return (url.contains(API_DOC_URL) || url.contains(prefix + authUrl));
//        }
//    }
//}