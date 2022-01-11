package com.company.my.blog.service;

import java.util.List;

import com.company.my.blog.dto.UserDto;
import com.company.my.blog.model.User;
import com.company.my.blog.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    public User getUserById(int id) {
        return userRepository.findById(id).get();
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAllUsers();
    }

    public void registerUser(User user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole("Author");

        userRepository.save(user);
    }

    public User getUserByEmail(String selectedAuthor) {
        return userRepository.findByEmail(selectedAuthor);
    }

    
}
