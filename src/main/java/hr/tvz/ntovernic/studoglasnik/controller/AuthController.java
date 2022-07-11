package hr.tvz.ntovernic.studoglasnik.controller;

import hr.tvz.ntovernic.studoglasnik.dto.RegisterDto;
import hr.tvz.ntovernic.studoglasnik.dto.LoginDto;
import hr.tvz.ntovernic.studoglasnik.dto.LoginResponse;
import hr.tvz.ntovernic.studoglasnik.dto.UserDto;
import hr.tvz.ntovernic.studoglasnik.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/current")
    public UserDto getCurrentUser() {
        return authService.getCurrentUser();
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody final LoginDto user) {
        return authService.login(user);
    }

    @PostMapping("/register")
    public void register(@RequestBody final RegisterDto user) {
        authService.register(user);
    }
}
