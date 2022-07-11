package hr.tvz.ntovernic.studoglasnik.service;

import hr.tvz.ntovernic.studoglasnik.dto.ChangePasswordDto;
import hr.tvz.ntovernic.studoglasnik.dto.RegisterDto;
import hr.tvz.ntovernic.studoglasnik.dto.UserDto;
import hr.tvz.ntovernic.studoglasnik.dto.UserUpdateDto;
import hr.tvz.ntovernic.studoglasnik.exception.WrongPasswordException;
import hr.tvz.ntovernic.studoglasnik.model.Role;
import hr.tvz.ntovernic.studoglasnik.model.User;
import hr.tvz.ntovernic.studoglasnik.properties.FolderProperties;
import hr.tvz.ntovernic.studoglasnik.repository.UserRepository;
import hr.tvz.ntovernic.studoglasnik.util.FilenameGenerator;
import hr.tvz.ntovernic.studoglasnik.util.MapperUtils;
import hr.tvz.ntovernic.studoglasnik.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final CurrentUserService currentUserService;
    private final FolderProperties folderProperties;
    private final PasswordEncoder passwordEncoder;

    public List<UserDto> getUsers() {
        final List<User> userList = userRepository.findAll();

        return MapperUtils.mapAll(userList, UserDto.class);
    }

    public UserDto getUserById(final Long userId) {
        final User user = getUser(userId);

        return MapperUtils.map(user, UserDto.class);
    }

    public void saveUser(final RegisterDto registerDto) {
        userValidator.checkUsernameUniqueness(registerDto.getUsername());
        userValidator.checkEmailUniqueness(registerDto.getEmail());

        final User user = MapperUtils.map(registerDto, User.class);
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setJoinDate(LocalDateTime.now());

        userRepository.save(user);
    }

    public void updateUser(final Long userId, final UserUpdateDto userUpdateDto) {
        final User user = getUser(userId);

        userValidator.checkUsernameUniqueness(user, userUpdateDto.getUsername());
        userValidator.checkEmailUniqueness(user, userUpdateDto.getEmail());

        if(!currentUserService.isCurrentUserAdmin()) {
            userUpdateDto.setRole(Role.ROLE_USER);
        }

        BeanUtils.copyProperties(userUpdateDto, user);
        userRepository.save(user);
    }

    public void changeUserPassword(final Long userId, final ChangePasswordDto changePasswordDto) throws WrongPasswordException {
        final User user = getUser(userId);
        userValidator.checkPassword(changePasswordDto.getCurrentPassword(), user.getPassword());

        user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
        userRepository.save(user);
    }

    public void changeProfilePicture(final Long userId, final MultipartFile pictureFile) throws IOException {
        final User user = getUser(userId);

        final String newProfilePicture = FilenameGenerator.generateFilename(LocalDateTime.now(), pictureFile.getOriginalFilename());
        changeProfilePictureFile(user.getProfilePicture(), newProfilePicture, pictureFile);

        user.setProfilePicture(newProfilePicture);
        userRepository.save(user);
    }

    public void deleteUser(final Long userId) {
        final User user = getUser(userId);
        userRepository.delete(user);
    }

    private User getUser(final Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with id %d not found!", userId)));
    }

    private void changeProfilePictureFile(final String currentProfilePicture, final String newProfilePicture,
                                          final MultipartFile pictureFile) throws IOException {
        final Path userPictureFolder = Paths.get(folderProperties.getUserPictureFolder());

        if(currentProfilePicture != null) {
            Files.delete(userPictureFolder.resolve(currentProfilePicture));
        }

        Files.copy(pictureFile.getInputStream(), userPictureFolder.resolve(newProfilePicture));
    }
}
