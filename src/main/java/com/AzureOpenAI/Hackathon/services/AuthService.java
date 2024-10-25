package com.AzureOpenAI.Hackathon.services;

import com.AzureOpenAI.Hackathon.dtos.SignupDto;
import com.AzureOpenAI.Hackathon.dtos.UserDto;

public interface AuthService {

    String[] login(String email, String password);

    UserDto signup(SignupDto signupDto);
}
