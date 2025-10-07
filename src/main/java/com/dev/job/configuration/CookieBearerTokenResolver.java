package com.dev.job.configuration;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.stereotype.Component;

@Component
public class CookieBearerTokenResolver implements BearerTokenResolver {
    @Override
    public String resolve(HttpServletRequest request) {
        if(request.getCookies() == null) return null;
        for(var cookie: request.getCookies()){
            if("token".equals(cookie.getName())){
                return cookie.getValue();
            }
        }
        return null;
    }
}
