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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    private final SubjectService subjectService;

    @Autowired
    public AdminController(UserService userService, SubjectService subjectService) {
        this.userService = userService;
        this.subjectService = subjectService;
    }

    @GetMapping("/users")
    public String usersList(Model model) {
        model.addAttribute("students", this.userService.findAllStudents());
        model.addAttribute("teachers", this.userService.findAllTeachers());

        return "admin/users-list";
    }

    @GetMapping("/disabled-accounts")
    public String disabledAccounts(Model model) {
        model.addAttribute("users", this.userService.findAllDisabledAccounts());

        return "admin/disabled-users";
    }

    @GetMapping("/enable-account/{id}")
    public @ResponseBody Map<String, Object> enableAccount(@PathVariable Integer id, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", "User account is successfully enabled.");

        if (!this.userService.enableAccount(id)) {
            response.setStatus(407);
            map.put("message", "User with id " + id + " does not exist.");
            return map;
        }

        return map;
    }
}
