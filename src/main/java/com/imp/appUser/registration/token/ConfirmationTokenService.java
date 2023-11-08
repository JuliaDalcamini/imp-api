package com.imp.appUser.registration.token;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public void saveConfirmationToken(ConfirmationToken confirmationToken) {
        confirmationTokenRepository.save(confirmationToken);
    }

    public Optional<ConfirmationToken> getToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    public boolean setConfirmedAt(String token) {
        Optional<ConfirmationToken> optionalConfirmationToken = confirmationTokenRepository.findByToken(token);
        if (optionalConfirmationToken.isEmpty()) {
            throw new IllegalStateException("Invalid Token");
        }

        ConfirmationToken confirmationToken = optionalConfirmationToken.get();
        confirmationToken.setConfirmedAt(LocalDateTime.now());
        confirmationTokenRepository.save(confirmationToken);

        return true;
    }
}
