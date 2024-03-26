package com.imp.artifact;

import com.imp.appUser.AppUserService;
import com.imp.priority.moscow.MoscowPriority;
import com.imp.priority.moscow.MoscowPriorityLevel;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@AllArgsConstructor
public class ArtifactService {

    private ArtifactRepository artifactRepository;
    private AppUserService appUserService;
    public void createArtifact(ArtifactDTO request) {
        Artifact newArtifact = new Artifact(
                request.name(),
                request.artifactType(),
                appUserService.getUserLogged().getId(),
                false,
                LocalDateTime.now(),
                LocalDateTime.of(0, 1, 1, 0, 0, 0),
                new MoscowPriority(MoscowPriorityLevel.COULD_HAVE)
        );

        artifactRepository.save(newArtifact);
    }
    public Artifact updateArtifact(ArtifactDTO requestWithNewInfo, String id) {
        Artifact updatedArtifact = getArtifactById(id);

        if (isEqualsCreator(updatedArtifact)) {
            updatedArtifact.setName(requestWithNewInfo.name());
            return artifactRepository.save(updatedArtifact);
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Apenas o criador do projeto pode atualizá-lo");
    }

    public void deleteArtifact(String id) {
        Artifact artifact = getArtifactById(id);

        if (!isEqualsCreator(artifact)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Apenas o criador do projeto pode excluí-lo");
        }

        artifactRepository.deleteById(id);
    }

    private boolean isEqualsCreator(Artifact updatedArtifact) {
        return Objects.equals(updatedArtifact.getCreatorId(), appUserService.getUserLogged().getId());
    }

    private boolean isArtifactExists(String id) {
        return artifactRepository
                .findById(id)
                .isPresent();
    }

    public Artifact getArtifactById(String id) {
        if (isArtifactExists(id)) {
            return artifactRepository.findById(id).get();
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Artefato não encontrado");
    }
}
