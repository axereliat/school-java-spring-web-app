package com.school.web.controllers;

import com.school.domain.entities.*;
import com.school.domain.exceptions.ResourceNotFoundException;
import com.school.domain.models.binding.MarkWriteBindingModel;
import com.school.service.SubjectService;
import com.school.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/student")
public class StudentController {

    private final UserService userService;

    private final SubjectService subjectService;

    @Autowired
    public StudentController(UserService userService, SubjectService subjectService) {
        this.userService = userService;
        this.subjectService = subjectService;
    }

    @GetMapping("/my-marks")
    public String myMarks(Principal principal, Model model) {
        Student student = (Student) this.userService.findByUsername(principal.getName());

        Map<String, List<Mark>> subjectMarkMap = new TreeMap<>();
        List<Subject> subjects = this.subjectService.findAll();

        for (Subject subject : subjects) {
            subjectMarkMap.put(subject.getName(), new ArrayList<>());
            for (Mark mark : student.getMarks().stream().filter(m -> m.getSubject().equals(subject)).collect(Collectors.toList())) {
                subjectMarkMap.get(subject.getName()).add(mark);
            }
        }

        model.addAttribute("student", student);
        model.addAttribute("subjectMarkMap", subjectMarkMap);
        model.addAttribute("subjects", subjects);

        return "student/my-marks";
    }
}
