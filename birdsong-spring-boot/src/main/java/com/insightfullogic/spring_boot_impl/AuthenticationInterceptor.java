package com.insightfullogic.spring_boot_impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * .
 */
@Component
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {

    private final UserRepository repo;

    @Autowired
    public AuthenticationInterceptor(UserRepository repo) {
        this.repo = repo;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie userCookie = WebUtils.getCookie(request, UserController.USER);
        Cookie passCookie = WebUtils.getCookie(request, UserController.PASS);
        if (userCookie == null || passCookie == null) {
            throw new ForbiddenException();
        }
        String user = userCookie.getValue();
        repo.checkUsersPassword(user, passCookie.getValue());
        request.setAttribute("user", user);
        return true;
    }

}
