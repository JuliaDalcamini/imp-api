package com.imp.artifact;

import com.imp.appUser.AppUser;
import com.imp.appUser.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("api/artifact")
public class ArtifactController {
    
    private ArtifactService artifactService;
    private AppUserService userService;

    @PostMapping("create")
    @ResponseStatus(HttpStatus.CREATED)
    public void createArtifact(@RequestBody ArtifactDTO request) {
        artifactService.createArtifact(request);
    }

    @PutMapping("update")
    public ArtifactResponse updateArtifact(@RequestBody ArtifactDTO requestWithNewInfo, @RequestParam(name = "id") String id) {
        Artifact artifact = artifactService.updateArtifact(requestWithNewInfo, id);
        AppUser creator = userService.getUserById(artifact.getCreatorId());

        return ArtifactResponse.of(artifact, creator);
    }

    @DeleteMapping("delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteArtifact(@RequestParam(name = "id") String id) {
        artifactService.deleteArtifact(id);
    }

    @GetMapping("search")
    public ArtifactResponse getArtifact(@RequestParam(name = "id") String id) {
        Artifact artifact = artifactService.getArtifactById(id);
        AppUser creator = userService.getUserById(artifact.getCreatorId());

        return ArtifactResponse.of(artifact, creator);
    }
    
}
