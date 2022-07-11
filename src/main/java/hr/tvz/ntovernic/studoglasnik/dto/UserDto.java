package hr.tvz.ntovernic.studoglasnik.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import hr.tvz.ntovernic.studoglasnik.model.Role;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDto {

    private Long id;
    private String username;
    private String email;
    private String phoneNumber;
    private Role role;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    private LocalDateTime joinDate;
    private String profilePicture;
}
