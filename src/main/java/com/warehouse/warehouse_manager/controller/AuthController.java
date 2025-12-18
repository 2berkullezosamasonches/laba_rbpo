package com.warehouse.warehouse_manager.controller;

import com.warehouse.warehouse_manager.dto.RegisterRequest;
import com.warehouse.warehouse_manager.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        // Здесь мы берем данные из JSON и отправляем в твой AuthService
        return authService.register(
                request.getUsername(),
                request.getPassword(),
                request.getRole()
        );
    }
}