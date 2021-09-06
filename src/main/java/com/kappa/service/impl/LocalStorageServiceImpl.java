package com.kappa.service.impl;

import com.kappa.config.properties.LocalStorageProperty;
import com.kappa.constant.CommonConstant;
import com.kappa.error.FileNotFoundException;
import com.kappa.error.FileStorageException;
import com.kappa.model.dto.Uploadable;
import com.kappa.service.StorageService;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Log4j2
@Service
@ConditionalOnProperty(prefix = "storage", name = "storage-type", havingValue = "local")
public class LocalStorageServiceImpl extends StorageService {

    private final Path storageLocation;

    @Autowired
    public LocalStorageServiceImpl(LocalStorageProperty localStorageProperty) {
        this.storageLocation = Paths.get(localStorageProperty.getUploadDir())
                .toAbsolutePath().normalize();
        Path audioPath = storageLocation.resolve("attachment");
        try {
            Files.createDirectories(this.storageLocation);
            Files.createDirectories(audioPath);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @Override
    public String upload(MultipartFile multipartFile, Uploadable uploadable) {
        String ext = getExtension(multipartFile);
        String folder = uploadable.getFolder();
        String fileName = uploadable.createFileName(ext);
        String rootUri = "/api/resource/download";
        this.normalizeFileName(fileName);
        try {
            // Check if the file's title contains invalid characters
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            String blobString = folder + CommonConstant.SLASH + fileName;
            // Copy file to the target location (Replacing existing file with the same title)
            Path targetLocation = this.storageLocation.resolve(blobString);
            uploadable.setBlobString(blobString);
            Files.copy(multipartFile.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(rootUri)
                    .path(CommonConstant.SLASH)
                    .path(folder)
                    .path(CommonConstant.SLASH)
                    .path(fileName).toUriString();
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    @Override
    public void delete(Uploadable uploadable) {
        Path filePath = storageLocation.resolve(uploadable.getBlobString()).normalize();
        File file = filePath.toFile();
        log.info("Delete file {} success? {}", uploadable.getBlobString(), file.delete());
    }

    @Override
    public Resource loadFileAsResource(String fileName, String folder) {
        try {
            Path filePath = storageLocation.resolve(folder + CommonConstant.SLASH + fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found " + folder + CommonConstant.SLASH + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("File not found " + folder + CommonConstant.SLASH + fileName, ex);
        }
    }
}
