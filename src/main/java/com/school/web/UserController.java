package com.school.web;

import com.school.service.SubjectService;
import com.school.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
}
