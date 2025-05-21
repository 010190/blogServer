package com.example.blogServer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto {
    private Long id;
    private String username;
    private String roles;
    // Możesz dodać token JWT, jeśli planujesz używać uwierzytelniania opartego na tokenach
}
