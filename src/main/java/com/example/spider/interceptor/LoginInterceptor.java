package com.example.spider.interceptor;

import com.example.spider.controller.Util;
import com.example.spider.model.exceptions.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (request.getSession().getAttribute(Util.LOGGED_ID) == null) {
            String requestURI = request.getRequestURI();
            if (!(requestURI.endsWith("/users/login") || requestURI.endsWith("/users/register") || requestURI.endsWith("/users/logout")||requestURI.contains("/confirm"))) {
                throw new UnauthorizedException("You have to login first");
            }
        }
        request.setAttribute("startTime", System.currentTimeMillis());
        return true;
    }
}
