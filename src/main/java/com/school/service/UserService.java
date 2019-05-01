package com.school.service;

import com.school.domain.models.binding.UserRegisterBindingModel;

public interface UserService {

    void seedAdminAndRoles();

    void sendRequestToAdmin();

    void register(UserRegisterBindingModel bindingModel);
}
