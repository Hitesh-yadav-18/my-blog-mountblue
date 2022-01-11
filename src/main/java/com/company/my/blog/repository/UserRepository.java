package com.company.my.blog.repository;

import java.util.List;

import com.company.my.blog.dto.UserDto;
import com.company.my.blog.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

  
    User findByEmail(String email);

    @Query("SELECT u FROM User u")
    List<User> findAllAuthors();

    @Query("SELECT new com.company.my.blog.dto.UserDto(u.id, u.name, u.email) FROM User u")
    List<UserDto> findAllUsers();
}
