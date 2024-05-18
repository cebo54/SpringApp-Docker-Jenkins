package com.devops.p2.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.devops.p2.dto.RegisterDto;
import com.devops.p2.dto.UpdateDto;
import com.devops.p2.dto.UserResponse;
import com.devops.p2.service.Abstract.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.URL;
import java.util.List;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final AmazonS3 amazonS3;



    @GetMapping("")
    public String getUsers(Model model){
        List<UserResponse> users = userService.getUsers();
        model.addAttribute("userList", users);
        return "index";
    }

    @GetMapping("/create")
    public String getCreateForm(Model model) {
        model.addAttribute("registerDto", new RegisterDto());
        return "create"; // Return the view name
    }

    @PostMapping("/addUser")
    public String save(@ModelAttribute RegisterDto registerDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "create";
        }

        userService.save(registerDto);
        return "redirect:/";
    }


    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") int id, Model model) {
        UserResponse user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "update";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") int id, @ModelAttribute UpdateDto updateDto) {
        userService.update(id, updateDto);
        return "redirect:/";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        userService.delete(id);
        return "redirect:/";
    }

}
