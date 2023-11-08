package com.imp.appUser.registration;

import com.imp.appUser.AppUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRegisterRepository extends MongoRepository<AppUser, String> {
    UserDetails findByEmail(String email);
}
