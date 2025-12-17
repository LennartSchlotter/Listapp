package com.example.listapp.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.listapp.dto.user.UserResponseDto;
import com.example.listapp.dto.user.UserUpdateDto;
import com.example.listapp.service.UserService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService _userService;

    public UserController(UserService userService){
        _userService = userService;
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<UserResponseDto> GetUser(){

        UserResponseDto response = _userService.getUser();
        return ResponseEntity.ok(response);
    }

    @PatchMapping
    @ResponseBody
    public ResponseEntity<UUID> UpdateUser(@Valid @RequestBody UserUpdateDto dto){
        UUID updatedId = _userService.updateUser(dto);
        return ResponseEntity.ok(updatedId);
    }

    @DeleteMapping
    @ResponseBody
    public ResponseEntity<Void> DeleteUser(){
        _userService.deleteUser();
        return ResponseEntity.noContent().build();
    }
}
