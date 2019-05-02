package com.school.service;

import com.school.domain.models.binding.UserRegisterBindingModel;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    void seedAdminAndRoles();

    void sendRequestToAdmin();

    boolean register(UserRegisterBindingModel bindingModel);

    boolean existsUserByUsername(String username);

    boolean existsUserByEmail(String email);
}
