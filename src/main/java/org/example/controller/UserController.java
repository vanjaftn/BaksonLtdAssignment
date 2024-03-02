package org.example.controller;

import org.example.model.AuthRequest;
import org.example.model.User;
import org.example.service.UserService;
import org.example.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    private final UserService service;

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/authenticate")
    public String generateToken(@RequestBody AuthRequest authRequest) throws Exception {
        return service.generateToken(authRequest);
    }

    @GetMapping("/getAllUsers")
    @ResponseBody
    public List<User> getAll() {
        return service.getAll();
    }

}
