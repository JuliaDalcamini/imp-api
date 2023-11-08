package com.imp.appUser.registration;

import com.imp.appUser.AppUser;
import com.imp.appUser.AppUserService;
import com.imp.project.Project;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@AllArgsConstructor
public class UserRegisterService {

    private final AppUserService appUserService;
    private final EmailValidator emailValidator;

    public void register(UserRegisterDTO request, ArrayList<Project> listProjects) {
        boolean isValidEmail = emailValidator.isValidEmail(request.email());
        if (!isValidEmail) {
            throw new IllegalStateException("O email não é válido.");
        }
        appUserService.register(
                new AppUser(
                        request.firstName(),
                        request.lastName(),
                        request.email(),
                        request.password(),
                        listProjects
                )
        );

    }
}
