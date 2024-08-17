package com.company.neuheathcaremanagement.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginHandlerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

       Object user = request.getSession().getAttribute("loginuser");

        if (user == null) {
            response.sendRedirect("/login");
            return false;
        }
        return true;
    }
}