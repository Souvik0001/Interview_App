package com.AzureOpenAI.Hackathon.Controller;

import com.AzureOpenAI.Hackathon.dtos.LoginRequestDto;
import com.AzureOpenAI.Hackathon.dtos.LoginResponseDto;
import com.AzureOpenAI.Hackathon.dtos.SignupDto;
import com.AzureOpenAI.Hackathon.dtos.UserDto;
import com.AzureOpenAI.Hackathon.services.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    ResponseEntity<UserDto> signUp(@RequestBody SignupDto signupDto) {
        return new ResponseEntity<>(authService.signup(signupDto), HttpStatus.CREATED);
    }


    @PostMapping("/login")
    ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto,
                                           HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        String tokens[] = authService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());

//        Cookie cookie = new Cookie("token", tokens[1]);
//        cookie.setHttpOnly(true);          // javascript cannot access it so XSS attack is prevented

//        httpServletResponse.addCookie(cookie);

        return ResponseEntity.ok(new LoginResponseDto(tokens[0]));
    }
}
