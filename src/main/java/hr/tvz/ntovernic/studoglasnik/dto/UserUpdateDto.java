package hr.tvz.ntovernic.studoglasnik.dto;

import hr.tvz.ntovernic.studoglasnik.model.Role;
import lombok.Data;

@Data
public class UserUpdateDto {

    private String username;
    private String email;
    private String phoneNumber;
    private Role role;
}
