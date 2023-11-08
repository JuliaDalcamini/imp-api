package com.imp.appUser.registration.token;

import com.imp.appUser.AppUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Document("token")
public class ConfirmationToken {

    @Id
    private String id;
    @NotBlank
    private String token;
    @NotBlank
    private LocalDateTime createdAt;
    @NotBlank
    private LocalDateTime expiresAt;
    private LocalDateTime confirmedAt;
    @NotBlank
    private AppUser appUser;

    public ConfirmationToken(String token, LocalDateTime createdAt, LocalDateTime expiresAt, AppUser appUser) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.appUser = appUser;
    }
}
