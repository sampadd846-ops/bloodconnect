package com.bloodconnect.controller;

import com.bloodconnect.entity.User;
import com.bloodconnect.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }


    /* =========================
       LOGIN
    ========================== */

    @PostMapping("/login")
    public User login(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpSession session) {

        // Authenticate user
        User user = userService.login(
                username,
                password
        );

        // Clear any previous login information
        session.removeAttribute("username");
        session.removeAttribute("role");
        session.removeAttribute("bloodBankId");


        // Store current authenticated user
        session.setAttribute(
                "username",
                user.getUsername()
        );

        session.setAttribute(
                "role",
                user.getRole()
        );


        // Store assigned blood bank for manager
        if (user.getBloodBank() != null) {

            session.setAttribute(
                    "bloodBankId",
                    user.getBloodBank().getId()
            );
        }


        return user;
    }
}