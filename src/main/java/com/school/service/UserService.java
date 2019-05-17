package com.school.service;

import com.school.domain.entities.Student;
import com.school.domain.entities.Teacher;
import com.school.domain.entities.User;
import com.school.domain.models.binding.MarkWriteBindingModel;
import com.school.domain.models.binding.UserRegisterBindingModel;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.security.Principal;
import java.util.List;

public interface UserService extends UserDetailsService {

    User findByUsername(String username);

    void seedAdminAndRoles();

    List<User> findAllDisabledAccounts();

    boolean register(UserRegisterBindingModel bindingModel);

    boolean existsUserByUsername(String username);

    boolean existsUserByEmail(String email);

    boolean enableAccount(Integer userId);

    List<User> findUsersFromClass(int number, char letter);

    boolean writeMark(Integer studentId, MarkWriteBindingModel bindingModel, Principal principal);

    User findById(Integer studentId);

    List<Teacher> findAllTeachers();

    List<Student> findAllStudents();
}
