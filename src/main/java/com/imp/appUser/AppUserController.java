package com.imp.appUser;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path = "imp/home")
public class AppUserController {

    private final AppUserService userService;
}
