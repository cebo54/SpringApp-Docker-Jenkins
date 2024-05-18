package com.devops.p2.service.Concrete;

import com.devops.p2.dao.UserRepository;
import com.devops.p2.dto.RegisterDto;
import com.devops.p2.dto.UpdateDto;
import com.devops.p2.dto.UserResponse;
import com.devops.p2.entity.User;
import com.devops.p2.service.Abstract.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.devops.p2.dto.UserResponse.convert;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    @Override
    public void save(RegisterDto registerDto) {
        User user = new User();
        user.setName(registerDto.getName());
        user.setAddress(registerDto.getAddress());

        userRepository.save(user);
    }

    @Override
    public void update(int id, UpdateDto updateDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setName(updateDto.getName());
        user.setAddress(updateDto.getAddress());
        userRepository.save(user);
    }

    @Override
    public void delete(int id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User Not found"));
        userRepository.delete(user);
    }

    @Override
    public List<UserResponse> getUsers() {
        List<UserResponse> users = new ArrayList<>();
        List<User> userList = userRepository.findAll();
        for (User user : userList) {
            users.add(convert(user));
        }
        return users;
    }

    @Override
    public UserResponse getUserById(int id) {
        User user = userRepository.findById(id).orElseThrow();
        return convert(user);
    }
}
