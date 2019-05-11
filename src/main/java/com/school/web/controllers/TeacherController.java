package com.school.web.controllers;

import com.school.domain.entities.*;
import com.school.domain.exceptions.ResourceNotFoundException;
import com.school.domain.models.binding.MarkWriteBindingModel;
import com.school.service.SubjectService;
import com.school.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

    private final UserService userService;

    private final SubjectService subjectService;

    @Autowired
    public TeacherController(UserService userService, SubjectService subjectService) {
        this.userService = userService;
        this.subjectService = subjectService;
    }

    @GetMapping("/classes")
    public String classes() {
        return "teacher/classes";
    }

    @GetMapping("/classes/{classId}")
    public String classDetails(@PathVariable String classId, Model model) {
        int number = Integer.parseInt(classId.substring(0, classId.length() - 1));
        char[] charArr = classId.toLowerCase().toCharArray();
        char letter = charArr[charArr.length - 1];
        List<User> sortedStudents = this.userService.findUsersFromClass(number, letter).stream()
                .sorted(Comparator.comparing(User::getUsername))
                .collect(Collectors.toList());

        model.addAttribute("classId", classId);
        model.addAttribute("students", sortedStudents);
        return "teacher/classDetails";
    }

    @GetMapping("/students/{studentId}")
    public String studentDetails(@PathVariable Integer studentId, Model model, Principal principal) {
        User user = this.userService.findById(studentId);
        if (!user.isStudent()) {
            throw new ResourceNotFoundException();
        }
        Student student = (Student) user;

        Map<String, List<Mark>> subjectMarkMap = new TreeMap<>();
        List<Subject> subjects = this.subjectService.findAll();

        for (Subject subject : subjects) {
            subjectMarkMap.put(subject.getName(), new ArrayList<>());
            for (Mark mark : student.getMarks().stream().filter(m -> m.getSubject().equals(subject)).collect(Collectors.toList())) {
                subjectMarkMap.get(subject.getName()).add(mark);
            }
        }

        Teacher currentTeacher = (Teacher) this.userService.findByUsername(principal.getName());

        model.addAttribute("currentTeacher", currentTeacher);
        model.addAttribute("student", student);
        model.addAttribute("subjectMarkMap", subjectMarkMap);
        model.addAttribute("subjects", subjects);

        return "teacher/studentDetails";
    }

    @PostMapping("/students/{studentId}/write-mark")
    public String writeMark(@PathVariable Integer studentId, RedirectAttributes redirectAttributes, MarkWriteBindingModel bindingModel, Principal principal, HttpServletResponse response) {
        User user = this.userService.findById(studentId);
        if (!user.isStudent()) {
            throw new ResourceNotFoundException();
        }

        if (!this.userService.writeMark(studentId, bindingModel, principal)) {
            redirectAttributes.addFlashAttribute("error", "An error occurred.");
        }

        return "redirect:/teacher/students/" + studentId;
    }
}
