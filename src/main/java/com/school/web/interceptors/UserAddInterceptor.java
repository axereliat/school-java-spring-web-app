package com.school.web.interceptors;

import com.school.domain.entities.User;
import com.school.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class UserAddInterceptor implements HandlerInterceptor {

    private final UserService userService;

    @Autowired
    public UserAddInterceptor(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView == null) {
            return;
        }
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (!principal.getClass().equals(String.class)) {
                UserDetails user = (UserDetails) principal;
                User userEntity = this.userService.findByUsername(user.getUsername());

                modelAndView.getModel().put("currentUser", userEntity);
            } else {
                User user = new User();
                modelAndView.getModel().put("currentUser", user);
            }
        }
    }
}
