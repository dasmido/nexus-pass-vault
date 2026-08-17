package dev.dasmido.nexus.rest;

import javax.sql.DataSource;
import java.sql.SQLException;

import org.springframework.web.bind.annotation.GetMapping;
import java.net.URI;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/settings")
public class SettingsController {

    private final DataSource dataSource;

    public SettingsController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping("/storage-status")
    public StorageStatus getStorageStatus() {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metadata = connection.getMetaData();
            URI databaseUri = URI.create(metadata.getURL().replaceFirst("^jdbc:", ""));
            
            return new StorageStatus(
                metadata.getDatabaseProductName(),
                connection.isValid(2),
                databaseUri.getPath().replaceFirst("^/", ""),
                databaseUri.getHost(),
                databaseUri.getPort()
            );
        } catch (SQLException | IllegalArgumentException e) {
            return new StorageStatus("PostgreSQL", false, null, null, null);
        }
    }

    public record StorageStatus(
        String provider,
        boolean connected,
        String database,
        String host,
        Integer port
    ){}

}
