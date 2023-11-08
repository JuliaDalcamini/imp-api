package com.imp.appUser;

import com.imp.appUser.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@AllArgsConstructor
@Service
public class AppUserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG = "Usuário com o email %s não foi encontrado.";

    private final AppUserRepository appUserRepository;
    private PasswordEncoder passwordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(String
                                .format(USER_NOT_FOUND_MSG, email)));
    }

    public void register(AppUser appUser) {

        if (isUserExists(appUser)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email já cadastrado.");
        }

        String encondedPassword = passwordEncoder
                .encode(appUser.getPassword());
        appUser.setPassword(encondedPassword);

        appUserRepository.save(appUser);
    }

    private boolean isUserExists(AppUser appUser) {
        return appUserRepository
                .findByEmail(appUser.getEmail())
                .isPresent();
    }

    public AppUser updateUser(AppUser userWithNewInfo, String id) {
        AppUser updatedUser = getUserById(id);

        updatedUser.setFirstName(userWithNewInfo.getFirstName());
        updatedUser.setLastName(userWithNewInfo.getLastName());
        updatedUser.setEmail(userWithNewInfo.getEmail());

        String encodedPassword = passwordEncoder.encode(userWithNewInfo.getPassword());
        updatedUser.setPassword(encodedPassword);

        return updatedUser;
    }

    public void deleteUser(String id) {
        AppUser deletedUser = getUserById(id);

        appUserRepository.delete(deletedUser);
    }

    public AppUser getUserById(String id) {
        Optional<AppUser> foundUser = appUserRepository.findById(id);

        if (foundUser.isPresent()) {
            return foundUser.get();
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário não encontrado");
    }

    public AppUser getUserByEmail(String email) {
        Optional<AppUser> foundUser = appUserRepository.findByEmail(email);

        if (foundUser.isPresent()) {
            return foundUser.get();
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário não encontrado");
    }

    public AppUser getUserLogged() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ((AppUser)principal);
    }
}
