package com.AzureOpenAI.Hackathon.dtos;

import com.AzureOpenAI.Hackathon.entities.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private long id;
    private String name;
    private String email;
    private Set<Role> roles;
}
