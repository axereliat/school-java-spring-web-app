package com.school.service;

import com.school.common.constants.RoleConstants;
import com.school.domain.entities.Role;
import com.school.domain.entities.Teacher;
import com.school.domain.entities.User;
import com.school.domain.models.binding.UserRegisterBindingModel;
import com.school.repository.RoleRepository;
import com.school.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public void seedAdminAndRoles() {
        if (this.roleRepository.findAll().size() == 0) {
            Role adminRole = new Role();
            adminRole.setAuthority(RoleConstants.ADMIN_ROLE);
            Role teacherRole = new Role();
            teacherRole.setAuthority(RoleConstants.TEACHER_ROLE);
            Role studentRole = new Role();
            studentRole.setAuthority(RoleConstants.STUDENT_ROLE);

            this.roleRepository.saveAndFlush(adminRole);
            this.roleRepository.saveAndFlush(teacherRole);
            this.roleRepository.saveAndFlush(studentRole);
        }
        if (this.userRepository.findAll().size() == 0) {
            User user = new User();
            user.setUsername("mario");
            user.setPassword(this.passwordEncoder.encode("123"));
            user.getAuthorities().add(this.roleRepository.findByAuthority(RoleConstants.ADMIN_ROLE));
            user.getAuthorities().add(this.roleRepository.findByAuthority(RoleConstants.TEACHER_ROLE));
            user.setPhoneNumber("0898452104");
            this.userRepository.saveAndFlush(user);
        }
    }

    @Override
    public void sendRequestToAdmin() {

    }

    @Override
    public void register(UserRegisterBindingModel bindingModel) {
        if (bindingModel.getRole().equals(RoleConstants.TEACHER_ROLE)) {
            Teacher teacher = this.modelMapper.map(bindingModel, Teacher.class);
            //teacher.setSubject();
        }
    }
}
