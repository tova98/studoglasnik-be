package hr.tvz.ntovernic.studoglasnik.validator;

import hr.tvz.ntovernic.studoglasnik.exception.WrongPasswordException;
import hr.tvz.ntovernic.studoglasnik.model.User;
import hr.tvz.ntovernic.studoglasnik.repository.UserRepository;
import hr.tvz.ntovernic.studoglasnik.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.persistence.EntityExistsException;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;
    private final PasswordEncoder passwordEncoder;

    public void checkUsernameUniqueness(final String username) {
        if(userRepository.existsByUsername(username)) {
            throw new EntityExistsException("User with this username already exists!");
        }
    }

    public void checkUsernameUniqueness(final User user, final String username) {
        if(!user.getUsername().equals(username) && userRepository.existsByUsername(username)) {
            throw new EntityExistsException("User with this username already exists!");
        }
    }

    public void checkEmailUniqueness(final String email) {
        if(userRepository.existsByEmail(email)) {
            throw new EntityExistsException("User with this email already exists!");
        }
    }

    public void checkEmailUniqueness(final User user, final String email) {
        if(!user.getEmail().equals(email) && userRepository.existsByEmail(email)) {
            throw new EntityExistsException("User with this email already exists!");
        }
    }

    public void checkPassword(final String currentPassword, final String userPassword) throws WrongPasswordException {
        if(isAdminAndWrongPassword(currentPassword, userPassword)
                || isNotAdminAndWrongPassword(currentPassword, userPassword)) {
            throw new WrongPasswordException("You have entered a wrong password!");
        }
    }

    private boolean isAdminAndWrongPassword(final String currentPassword, final String userPassword) {
        return currentUserService.isCurrentUserAdmin() && currentPassword != null
                && !passwordEncoder.matches(currentPassword, userPassword);
    }

    private boolean isNotAdminAndWrongPassword(final String currentPassword, final String userPassword) {
        return !currentUserService.isCurrentUserAdmin() && currentPassword != null
                && !passwordEncoder.matches(currentPassword, userPassword);
    }
}
