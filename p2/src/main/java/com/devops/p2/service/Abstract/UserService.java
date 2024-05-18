package com.devops.p2.service.Abstract;

import com.devops.p2.dto.RegisterDto;
import com.devops.p2.dto.UpdateDto;
import com.devops.p2.dto.UserResponse;

import java.util.List;

public interface UserService {
    void save(RegisterDto registerDto);


    void update(int id, UpdateDto updateDto);

    void delete(int id);

    List<UserResponse> getUsers();

    UserResponse getUserById(int id);
}
