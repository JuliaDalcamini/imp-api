package com.imp.appUser.registration;

import com.imp.appUser.AppUser;
import com.imp.auth.AuthenticationDTO;
import com.imp.auth.LoginResponseDTO;
import com.imp.project.Project;
import com.imp.config.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping(path = "api")
public class UserRegisterController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRegisterService registerService;

    @Autowired
    private TokenService tokenService;

    @PostMapping("register")
    public ResponseEntity register(@RequestBody @Valid UserRegisterDTO request) {
        ArrayList<Project> listProjects = new ArrayList<>();
        registerService.register(request, listProjects);
        return ResponseEntity.ok().build();
    }

    @PostMapping("login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO request){
        var usernamePassword = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((AppUser) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }
}
