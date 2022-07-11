package hr.tvz.ntovernic.studoglasnik.security;

import hr.tvz.ntovernic.studoglasnik.model.User;
import hr.tvz.ntovernic.studoglasnik.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthProvider implements AuthenticationManager {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(final Authentication authentication) {
        final String username = authentication.getPrincipal().toString();
        final String password = authentication.getCredentials().toString();
        final User user = userRepository.findByUsername(username);

        if(user == null || !StringUtils.hasText(password) || !passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Wrong username or password!");
        }

        final List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRole().name()));
        return new UsernamePasswordAuthenticationToken(username, password, authorities);
    }
}
