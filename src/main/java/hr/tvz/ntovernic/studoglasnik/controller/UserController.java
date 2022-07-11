package hr.tvz.ntovernic.studoglasnik.controller;

import hr.tvz.ntovernic.studoglasnik.dto.ChangePasswordDto;
import hr.tvz.ntovernic.studoglasnik.dto.RegisterDto;
import hr.tvz.ntovernic.studoglasnik.dto.UserDto;
import hr.tvz.ntovernic.studoglasnik.dto.UserUpdateDto;
import hr.tvz.ntovernic.studoglasnik.exception.WrongPasswordException;
import hr.tvz.ntovernic.studoglasnik.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<UserDto> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_USER')")
    public UserDto getUserById(@PathVariable final Long userId) {
        return userService.getUserById(userId);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void saveUser(@RequestBody final RegisterDto registerDto) {
        userService.saveUser(registerDto);
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updateUser(@PathVariable final Long userId, @RequestBody final UserUpdateDto userUpdateDto) {
        userService.updateUser(userId, userUpdateDto);
    }

    @PatchMapping("/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || @currentUserService.isCurrentUserMakingRequest(#userId)")
    public void changeUserPassword(@PathVariable final Long userId,
                                   @RequestBody final ChangePasswordDto changePasswordDto) throws WrongPasswordException {
        userService.changeUserPassword(userId, changePasswordDto);
    }

    @PostMapping("/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || @currentUserService.isCurrentUserMakingRequest(#userId)")
    public void changeProfilePicture(@PathVariable final Long userId,
                                     @RequestPart("picture") final MultipartFile pictureFile) throws IOException {
        userService.changeProfilePicture(userId, pictureFile);
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteUser(@PathVariable final Long userId) {
        userService.deleteUser(userId);
    }
}
