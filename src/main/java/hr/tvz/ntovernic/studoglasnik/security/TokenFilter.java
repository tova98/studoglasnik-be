package hr.tvz.ntovernic.studoglasnik.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import hr.tvz.ntovernic.studoglasnik.dto.ErrorResponse;
import hr.tvz.ntovernic.studoglasnik.exception.InvalidTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class TokenFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        final String token = parseToken(request);
        try {
            if(token != null && tokenProvider.validateToken(token)) {
                final Authentication auth = tokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (final InvalidTokenException e) {
            SecurityContextHolder.clearContext();
            final ErrorResponse body = new ErrorResponse(e.getMessage());

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            objectMapper.writeValue(response.getOutputStream(), body);
        }

        filterChain.doFilter(request, response);
    }

    public String parseToken(final HttpServletRequest request) {
        final String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
