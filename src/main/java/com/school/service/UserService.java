package com.school.service;

import com.school.domain.entities.User;
import com.school.domain.models.binding.UserRegisterBindingModel;
import org.springframework.security.core.userdetails.UserDetailsService;

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

    User findById(Integer studentId);
}
