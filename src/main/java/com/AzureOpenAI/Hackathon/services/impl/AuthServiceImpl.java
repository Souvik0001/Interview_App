package com.AzureOpenAI.Hackathon.services.impl;

import com.AzureOpenAI.Hackathon.dtos.SignupDto;
import com.AzureOpenAI.Hackathon.dtos.UserDto;
import com.AzureOpenAI.Hackathon.entities.User;
import com.AzureOpenAI.Hackathon.entities.enums.Role;
import com.AzureOpenAI.Hackathon.exceptions.RuntimeConflictException;
import com.AzureOpenAI.Hackathon.repositories.UserRepository;
import com.AzureOpenAI.Hackathon.security.JWTService;
import com.AzureOpenAI.Hackathon.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
//    private final RiderService riderService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
//    private final WalletService walletService;

    @Override
    public String[] login(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        User user = (User) authentication.getPrincipal();

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return new String[]{accessToken, refreshToken};
    }

    @Override
    @Transactional
    public UserDto signup(SignupDto signupDto) {
        User user = userRepository.findByEmail(signupDto.getEmail()).orElse(null);
        if(user != null)
            throw new RuntimeConflictException("Cannot signup, User already exists with email "+signupDto.getEmail());

        User mappedUser = modelMapper.map(signupDto, User.class);
        mappedUser.setRoles(Set.of(Role.USER));
        mappedUser.setPassword(passwordEncoder.encode(mappedUser.getPassword()));
        User savedUser = userRepository.save(mappedUser);

//        create user related entities
//        riderService.createNewRider(savedUser);
//        walletService.createNewWallet(savedUser);

        return modelMapper.map(savedUser, UserDto.class);
    }
}
