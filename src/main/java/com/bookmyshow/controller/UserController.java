package com.bookmyshow.controller;

import com.bookmyshow.dto.*;
import com.bookmyshow.models.User;
import com.bookmyshow.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


@Controller
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    public SignUpResponseDto signUp(SignUpRequestDto requestDto){
        SignUpResponseDto signUpResponseDto = new SignUpResponseDto();
        try{
            User user = userService.signUp(requestDto.getEmailId(),requestDto.getPassword());
            signUpResponseDto.setResponseStatus(ResponseStatus.SUCCESS);
            signUpResponseDto.setId(user.getId());
        }catch(Exception e){
            signUpResponseDto.setResponseStatus(ResponseStatus.FAILURE);
        }
        return signUpResponseDto;
    }
    public LoginResponseDto login(LoginRequestDto loginRequestDto){
        LoginResponseDto loginResponseDto = new LoginResponseDto();
        try {
            User loggedInUser = userService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());
            loginResponseDto.setLoginStatus(ResponseStatus.SUCCESS);
            loginResponseDto.setUserId(loggedInUser.getId());
        } catch (Exception e) {
            loginResponseDto.setLoginStatus(ResponseStatus.FAILURE);
        }
        return loginResponseDto;
    }
}
