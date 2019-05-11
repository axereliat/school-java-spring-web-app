package com.school.service;

import com.school.common.constants.RoleConstants;
import com.school.common.enumerations.MarkType;
import com.school.domain.entities.*;
import com.school.domain.models.binding.MarkWriteBindingModel;
import com.school.domain.models.binding.UserRegisterBindingModel;
import com.school.repository.RoleRepository;
import com.school.repository.SubjectRepository;
import com.school.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final SubjectRepository subjectRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final ModelMapper modelMapper;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, SubjectRepository subjectRepository, BCryptPasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.subjectRepository = subjectRepository;
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
    public boolean register(UserRegisterBindingModel bindingModel) {
        if (bindingModel.getRole().equals(RoleConstants.TEACHER_ROLE)) {
            Teacher teacher = this.modelMapper.map(bindingModel, Teacher.class);
            teacher.getAuthorities().add(this.roleRepository.findByAuthority(RoleConstants.TEACHER_ROLE));
            teacher.setPassword(this.passwordEncoder.encode(bindingModel.getPassword()));
            Subject foundSubject = this.subjectRepository.findByName(bindingModel.getSubject());
            if (foundSubject == null || bindingModel.getSubject().equals("")) {
                return false;
            }
            teacher.setSubject(foundSubject);
            teacher.setEnabled(false);

            this.userRepository.saveAndFlush(teacher);
        }
        if (bindingModel.getRole().equals(RoleConstants.STUDENT_ROLE)) {
            if (bindingModel.getGrade() == 0 || bindingModel.getClassLetter() == '0') {
                return false;
            }
            Student student = this.modelMapper.map(bindingModel, Student.class);
            student.getAuthorities().add(this.roleRepository.findByAuthority(RoleConstants.STUDENT_ROLE));
            student.setPassword(this.passwordEncoder.encode(bindingModel.getPassword()));
            student.setEnabled(false);

            this.userRepository.saveAndFlush(student);
        }
        return true;
    }

    @Override
    public boolean existsUserByUsername(String username) {
        return this.userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsUserByEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }

    @Override
    public List<User> findAllDisabledAccounts() {
        return this.userRepository.findAllDisabledAccounts();
    }

    @Override
    public User findByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }

    @Override
    public boolean enableAccount(Integer userId) {
        User user = this.userRepository.findById(userId).orElse(null);
        if (user == null) {
            return false;
        }
        user.setEnabled(true);
        this.userRepository.saveAndFlush(user);
        return true;
    }

    @Override
    public List<User> findUsersFromClass(int number, char letter) {
        return this.userRepository.findAll().stream().filter(user -> {
            Student student = null;
            if (user.isStudent()) {
                student = (Student) user;
            }
            if (student != null && student.getClassLetter() == letter && student.getGrade() == number) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());
    }

    @Override
    public boolean writeMark(Integer studentId, MarkWriteBindingModel bindingModel, Principal principal) {
        User user = this.userRepository.findById(studentId).orElse(null);
        if (user == null || !user.isStudent()) {
            return false;
        }
        Student student = (Student) user;

        if (bindingModel.getMark() < 2 || bindingModel.getMark() > 6) {
            return false;
        }
        Teacher currentTeacher = (Teacher) this.userRepository.findByUsername(principal.getName());
        Mark mark = new Mark();
        mark.setSubject(currentTeacher.getSubject());
        mark.setType(MarkType.valueOf(bindingModel.getMarkType()));
        mark.setValue(bindingModel.getMark());

        student.addMark(mark);

        this.userRepository.saveAndFlush(student);

        return true;
    }

    @Override
    public User findById(Integer studentId) {
        return this.userRepository.findById(studentId).orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        if (!this.userRepository.existsByUsername(s)) {
            throw new UsernameNotFoundException("Username " + s + " not found.");
        }
        return this.userRepository.findByUsername(s);
    }
}
