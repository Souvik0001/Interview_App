package com.AzureOpenAI.Hackathon.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
public class FileStorageService {

    private static final String STORAGE_DIRECTORY = "C:\\Users\\Souvik\\Desktop\\OpenAi\\AzureAI\\Hackathon\\src\\main\\resources\\File-Upload";

    public void saveFile(MultipartFile fileToSave) throws IOException {
        if (fileToSave == null || fileToSave.isEmpty()) {
            throw new IOException("File is empty or null.");
        }

        // Sanitize the filename
        String originalFilename = fileToSave.getOriginalFilename();
        if (originalFilename == null || originalFilename.contains("..")) {
            throw new SecurityException("Unsupported filename: " + originalFilename);
        }

        String sanitizedFilename = originalFilename.replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");

        // Define the target file path (normalized to avoid path traversal)
        Path targetLocation = Paths.get(STORAGE_DIRECTORY).toAbsolutePath().normalize().resolve(sanitizedFilename);

        // Ensure the file is stored in the correct directory
        if (!targetLocation.startsWith(Paths.get(STORAGE_DIRECTORY).toAbsolutePath().normalize())) {
            throw new SecurityException("Invalid file path: " + targetLocation);
        }

        // Create directories if they do not exist
        Files.createDirectories(targetLocation.getParent());

        // Save the file and throw IOException on failure
        Files.copy(fileToSave.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
    }
}
