package com.example.listapp.controller;

import java.util.UUID;

import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.listapp.dto.user.UserResponseDto;
import com.example.listapp.dto.user.UserUpdateDto;
import com.example.listapp.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/user")
@Tag(name = "User Controller", description = "APIs for managing authenticated users")
public class UserController {

    private final UserService _userService;

    public UserController(UserService userService){
        _userService = userService;
    }

    @Operation(summary = "Get current user", description = "Retrieve details of the authenticated user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User retrieved successfully",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserResponseDto.class)))
    })
    @GetMapping
    @ResponseBody
    public ResponseEntity<UserResponseDto> GetUser(){

        UserResponseDto response = _userService.getUser();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update current user", description = "Update details of the authenticated user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User updated successfully",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UUID.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request data",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PatchMapping
    @ResponseBody
    public ResponseEntity<UUID> UpdateUser(@Valid @RequestBody UserUpdateDto dto){
        UUID updatedId = _userService.updateUser(dto);
        return ResponseEntity.ok(updatedId);
    }

    @Operation(summary = "Delete current user", description = "Deletes the authenticated user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User account deleted successfully")
    })
    @DeleteMapping
    @ResponseBody
    public ResponseEntity<Void> DeleteUser(HttpServletRequest request, HttpServletResponse response){
        _userService.deleteUser();
        new SecurityContextLogoutHandler().logout(request, response, null);
        return ResponseEntity.noContent().build();
    }
}
