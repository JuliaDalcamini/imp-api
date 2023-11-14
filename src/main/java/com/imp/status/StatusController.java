package com.imp.status;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(path = "api")
public class StatusController {

    @GetMapping("status")
    @ResponseStatus(HttpStatus.OK)
    public String createProject() {
        return "Running.";
    }
}
