package com.school.web.controllers;

import com.school.domain.models.binding.UserRegisterBindingModel;
import com.school.service.SubjectService;
import com.school.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {

    private final UserService userService;

    private final SubjectService subjectService;

    @Autowired
    public UserController(UserService userService, SubjectService subjectService) {
        this.userService = userService;
        this.subjectService = subjectService;
    }

    @GetMapping("/authenticate")
    public String authenticate(Model model) {
        this.userService.seedAdminAndRoles();
        model.addAttribute("subjects", this.subjectService.findAll());

        return "users/authenticate";
    }

    @PostMapping(value = "/register", produces = "application/json")
    public @ResponseBody
    Map<String, Object> register(@Valid UserRegisterBindingModel bindingModel, BindingResult bindingResult) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", "success");

        if (bindingResult.getAllErrors().size() > 0) {
            map.put("message", bindingResult.getAllErrors().get(0).getDefaultMessage());
            return map;
        }
        if (this.userService.existsUserByUsername(bindingModel.getUsername())) {
            map.put("message", "Username exists.");
            return map;
        }
        if (this.userService.existsUserByEmail(bindingModel.getEmail())) {
            map.put("message", "Email exists.");
            return map;
        }
        if (!bindingModel.getPassword().equals(bindingModel.getConfirmPassword())) {
            map.put("message", "Passwords do not match.");
            return map;
        }
        if (!this.userService.register(bindingModel)) {
            map.put("message", "You were not registered successfully.");
            return map;
        }
        return map;
    }

    @GetMapping("/logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        return "redirect:/";
    }
}
