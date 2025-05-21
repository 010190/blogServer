package com.example.blogServer.controller;

import com.example.blogServer.dto.UserRegistrationDto;
import com.example.blogServer.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    // Wyświetlanie formularza logowania
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        return "login";
    }

    // Wyświetlanie formularza rejestracji
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("userDto", new UserRegistrationDto());
        return "register";
    }

    // Obsługa procesu rejestracji
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("userDto") UserRegistrationDto userDto,
                               BindingResult bindingResult,
                               Model model) {

        // Sprawdzenie błędów walidacji
        if (bindingResult.hasErrors()) {
            return "register";
        }

        // Weryfikacja zgodności haseł
        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            model.addAttribute("registrationError", "Hasła nie są zgodne");
            return "register";
        }

        try {
            // Rejestracja nowego użytkownika
            userService.registerNewUser(userDto);
            model.addAttribute("successMessage", "Rejestracja zakończona pomyślnie. Możesz się teraz zalogować.");
            return "login";
        } catch (Exception e) {
            model.addAttribute("registrationError", e.getMessage());
            return "register";
        }
    }
}

