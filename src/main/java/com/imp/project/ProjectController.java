package com.imp.project;

import com.imp.appUser.AppUser;
import com.imp.appUser.AppUserDTO;
import com.imp.appUser.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path = "api/projects")
public class ProjectController {

    private ProjectService projectService;
    private AppUserService userService;

    @PostMapping("create")
    @ResponseStatus(HttpStatus.CREATED)
    public void createProject(@RequestBody ProjectDTO request) {
        projectService.createProject(request);
    }

    @PutMapping("update")
    public ProjectResponse updateProject(@RequestBody ProjectDTO requestWithNewInfo, @RequestParam(name = "id") String id) {
        Project project = projectService.updateProject(requestWithNewInfo, id);
        AppUser creator = userService.getUserById(project.getCreatorId());

        return ProjectResponse.of(project, creator);
    }

    @DeleteMapping("delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteProject(@RequestParam(name = "id") String id) {
        projectService.deleteProject(id);
    }

    @GetMapping("search")
    public ProjectResponse getProject(@RequestParam(name = "id") String id) {
        Project project = projectService.getProjectById(id);
        AppUser creator = userService.getUserById(project.getCreatorId());

        return ProjectResponse.of(project, creator);
    }
    @GetMapping
    public List<Project> getProjects(@RequestParam(name = "filter") String filter) {

        return switch (filter) {
            case "createdBy" -> projectService.getProjectsByUser();
            case "member" -> projectService.getProjectsWithUser();
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Filtro inválido");
        };
    }

    @PostMapping("team/add")
    @ResponseStatus(HttpStatus.OK)
    public void addUserToTeam(@RequestBody String email, @RequestParam(name = "id") String id) {
        Project project = projectService.getProjectById(id);
        AppUser user = userService.getUserByEmail(email);

        AppUserDTO userDTO =
                new AppUserDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        );

        project.getTeam().add(userDTO);
    }
}
