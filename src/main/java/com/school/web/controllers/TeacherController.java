package com.school.web.controllers;

import com.school.domain.entities.Mark;
import com.school.domain.entities.Student;
import com.school.domain.entities.Subject;
import com.school.domain.entities.User;
import com.school.domain.exceptions.ResourceNotFoundException;
import com.school.service.SubjectService;
import com.school.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
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
    public String studentDetails(@PathVariable Integer studentId, Model model) {
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

        model.addAttribute("student", student);
        model.addAttribute("subjectMarkMap", subjectMarkMap);
        model.addAttribute("subjects", subjects);

        return "teacher/studentDetails";
    }
}
