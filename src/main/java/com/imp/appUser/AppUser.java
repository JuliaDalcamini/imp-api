package com.imp.appUser;

import com.imp.project.Project;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Document("users")
public class AppUser implements UserDetails {
    @Id
    private String id;
    @NotBlank(message = "O nome não pode ser um campo vazio")
    private String firstName;
    @NotBlank(message = "O sobrenome não pode ser um campo vazio")
    private String lastName;
    @NotBlank(message = "O email não pode ser um campo vazio")
    private String email;
    @NotBlank(message = "A senha não pode ser um campo vazio")
    private String password;
    private ArrayList<Project> listProjects;

    public AppUser(String firstName, String lastName, String email, String password, ArrayList<Project> listProjects) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.listProjects = listProjects;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("USER");
        return Collections.singletonList(authority);
    }

    @Override
    public String getUsername() {
        return null;
    }

    public String getFullName() {
        return "%s %s".formatted(firstName, lastName);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
