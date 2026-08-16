package dev.dasmido.nexus.rest;

import dev.dasmido.nexus.jpa.PasswordEntry;
import dev.dasmido.nexus.jpa.PasswordEntryRepository;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/passwords")
public class PasswordVaultController {

    private final PasswordEntryRepository passwordEntryRepository;

    public PasswordVaultController(PasswordEntryRepository passwordEntryRepository) {
        this.passwordEntryRepository = passwordEntryRepository;
    }

    @GetMapping
    public List<PasswordView> findAll() {
        return passwordEntryRepository.findAll()
                .stream()
                .map(entry -> new PasswordView(entry.getId(), entry.getSite(), entry.getUsername(), entry.getSecret()))
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PasswordView create(@Valid @RequestBody CreatePasswordRequest request) {
        PasswordEntry saved = passwordEntryRepository.save(
                new PasswordEntry(request.site(), request.username(), request.secret())
        );
        return new PasswordView(saved.getId(), saved.getSite(), saved.getUsername(), saved.getSecret());
    }
}

