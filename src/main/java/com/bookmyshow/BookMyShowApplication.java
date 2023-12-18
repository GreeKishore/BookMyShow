package com.bookmyshow;

import com.bookmyshow.controller.UserController;
import com.bookmyshow.dto.SignUpRequestDto;
import com.bookmyshow.dto.SignUpResponseDto;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class BookMyShowApplication implements CommandLineRunner {

    private final UserController userController;

    public BookMyShowApplication(UserController userController) {
        this.userController = userController;
    }

    public static void main(String[] args) {
        SpringApplication.run(BookMyShowApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto();
        signUpRequestDto.setEmailId("mani@gmail.com");
        signUpRequestDto.setPassword("mani");
        SignUpResponseDto signUpResponseDto = userController.signUp(signUpRequestDto);
        System.out.println(signUpResponseDto.getResponseStatus()+" "+signUpResponseDto.getId());
    }
}
