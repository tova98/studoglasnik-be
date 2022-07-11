package hr.tvz.ntovernic.studoglasnik.security;

import hr.tvz.ntovernic.studoglasnik.exception.InvalidTokenException;
import hr.tvz.ntovernic.studoglasnik.model.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenProvider {

    private static final String SECRET_KEY = "studoglasnikSecretKey";
    public static final long JWT_TOKEN_VALIDITY_DURATION = 24L * 60 * 60 * 1000;

    private final UserDetailsService userDetailsService;

    public String generateToken(final String username,final Role role) {
        final Claims claims = Jwts.claims().setSubject(username);
        claims.put("auth", new SimpleGrantedAuthority(role.getAuthority()));

        final Date now = new Date();
        final Date validity = new Date(now.getTime() + JWT_TOKEN_VALIDITY_DURATION);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public boolean validateToken(final String token) throws InvalidTokenException {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (final JwtException | IllegalArgumentException e) {
            throw new InvalidTokenException("Expired or invalid JWT token!");
        }
    }

    public Authentication getAuthentication(final String token) {
        final String username = getUsername(token);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
    }
}
