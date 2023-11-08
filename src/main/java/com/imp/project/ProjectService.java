package com.imp.project;

import com.imp.appUser.AppUser;
import com.imp.appUser.AppUserDTO;
import com.imp.appUser.AppUserService;
import com.imp.artifact.Artifact;
import com.imp.checklist.Checklist;
import com.imp.checklist.EmptyChecklist;
import com.imp.checklist.StandardChecklist;
import com.imp.prioritizationMethod.MoscowMethod;
import com.imp.prioritizationMethod.PrioritizationMethod;
import com.imp.prioritizationMethod.WiegersMethod;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private AppUserService appUserService;

    public void createProject(ProjectDTO request) {
        ArrayList<Artifact> artifactsList = new ArrayList<>();

        Project newProject = new Project(
                request.nameProject(),
                LocalDateTime.now(),
                appUserService.getUserLogged().getId(),
                getPMethod(request.prioritizationMethod()),
                getChecklist(request.checklist()),
                artifactsList,
                createTeam(appUserService.getUserLogged())

        );

        projectRepository.save(newProject);
    }

    public Project updateProject(ProjectDTO requestWithNewInfo, String id) {
        Project updatedProject = getProjectById(id);

        if (isEqualsCreator(updatedProject)) {
            updatedProject.setNameProject(requestWithNewInfo.nameProject());
            return projectRepository.save(updatedProject);
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Apenas o criador do projeto pode atualizá-lo");
    }

    public void deleteProject(String id) {
        Project project = getProjectById(id);

        if (!isEqualsCreator(project)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Apenas o criador do projeto pode excluí-lo");
        }

        projectRepository.deleteById(id);
    }

    public PrioritizationMethod getPMethod(String pMethod) {
        return switch (pMethod) {
            case "wiegers" -> new WiegersMethod();
            case "moscow" -> new MoscowMethod();
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Método de priorização inválido");
        };
    }

    public Checklist getChecklist(String checklistType) {
        return switch (checklistType) {
            case "standard" -> new StandardChecklist();
            case "empty" -> new EmptyChecklist();
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Checklist inválida");
        };
    }

    public List<Project> getProjectsByUser() {
        return projectRepository.findProjectsByCreatorId(appUserService.getUserLogged().getId());
    }

    public List<Project> getProjectsWithUser() {
        return projectRepository.findProjectsByMemberId(appUserService.getUserLogged().getId());
    }

    public ArrayList<AppUserDTO> createTeam(AppUser user) {
        ArrayList<AppUserDTO> team = new ArrayList<>();

        AppUserDTO userDTO =
                new AppUserDTO(
                        user.getId(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail()
                );

        team.add(userDTO);

        return team;
    }

    private boolean isEqualsCreator(Project updatedProject) {
        return Objects.equals(updatedProject.getCreatorId(), appUserService.getUserLogged().getId());
    }

    private boolean isProjectExists(String id) {
        return projectRepository
                .findById(id)
                .isPresent();
    }

    public Project getProjectById(String id) {
        if (isProjectExists(id)) {
            return projectRepository.findById(id).get();
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Projeto não encontrado");
    }
}
