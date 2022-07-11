package hr.tvz.ntovernic.studoglasnik.service;

import hr.tvz.ntovernic.studoglasnik.dto.RegisterDto;
import hr.tvz.ntovernic.studoglasnik.dto.LoginDto;
import hr.tvz.ntovernic.studoglasnik.dto.UserDto;
import hr.tvz.ntovernic.studoglasnik.model.Role;
import hr.tvz.ntovernic.studoglasnik.model.User;
import hr.tvz.ntovernic.studoglasnik.repository.UserRepository;
import hr.tvz.ntovernic.studoglasnik.dto.LoginResponse;
import hr.tvz.ntovernic.studoglasnik.security.TokenProvider;
import hr.tvz.ntovernic.studoglasnik.util.MapperUtils;
import hr.tvz.ntovernic.studoglasnik.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public UserDto getCurrentUser() {
        final User user = getUserFromToken();

        return MapperUtils.map(user, UserDto.class);
    }

    public LoginResponse login(final LoginDto loginDto) {
        final Authentication auth = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
        authenticationManager.authenticate(auth);

        final User user = userRepository.findByUsername(loginDto.getUsername());
        final String token = tokenProvider.generateToken(user.getUsername(), user.getRole());
        final UserDto userDto = MapperUtils.map(user, UserDto.class);
        return new LoginResponse(userDto, token);
    }

    public void register(final RegisterDto registerDto) {
        userValidator.checkUsernameUniqueness(registerDto.getUsername());
        userValidator.checkEmailUniqueness(registerDto.getEmail());

        final User user = MapperUtils.map(registerDto, User.class);
        user.setRole(Role.ROLE_USER);
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setJoinDate(LocalDateTime.now());

        userRepository.save(user);
    }

    private User getUserFromToken() {
        final String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username);
    }
}
