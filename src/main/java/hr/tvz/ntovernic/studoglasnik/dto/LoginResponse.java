package hr.tvz.ntovernic.studoglasnik.dto;

import hr.tvz.ntovernic.studoglasnik.model.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoginResponse {
    private final UserDto user;
    private final String token;
}
