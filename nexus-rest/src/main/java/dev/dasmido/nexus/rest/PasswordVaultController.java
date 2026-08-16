package dev.dasmido.nexus.rest;

import dev.dasmido.nexus.jpa.PasswordEntry;
import dev.dasmido.nexus.jpa.PasswordEntryRepository;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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
                .map(this::toView)
                .toList();
    }

    @GetMapping("/{id}")
    public PasswordView findById(@PathVariable Long id) {
        PasswordEntry entry = passwordEntryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Password entry not found"));
        return toView(entry);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PasswordView create(@Valid @RequestBody CreatePasswordRequest request) {
        PasswordEntry saved = passwordEntryRepository.save(
                new PasswordEntry(request.site(), request.username(), request.secret())
        );
        return toView(saved);
    }

    @PutMapping("/{id}")
    public PasswordView update(@PathVariable Long id, @Valid @RequestBody CreatePasswordRequest request) {
        PasswordEntry existing = passwordEntryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Password entry not found"));

        existing.setSite(request.site());
        existing.setUsername(request.username());
        existing.setSecret(request.secret());

        PasswordEntry saved = passwordEntryRepository.save(existing);
        return toView(saved);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!passwordEntryRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Password entry not found");
        }
        passwordEntryRepository.deleteById(id);
    }

    private PasswordView toView(PasswordEntry entry) {
        return new PasswordView(entry.getId(), entry.getSite(), entry.getUsername(), entry.getSecret());
    }
}

