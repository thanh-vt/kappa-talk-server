package com.kappa.service;

import com.kappa.error.FileStorageException;
import com.kappa.model.dto.Uploadable;
import java.io.IOException;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public abstract class StorageService {

    protected void normalizeFileName(String fileName) {
        if (fileName.contains("..")) {
            throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
        }
    }

    protected String getExtension(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        return originalFileName != null ?
                originalFileName.substring(originalFileName.lastIndexOf(".") + 1) : "";
    }

    public abstract String upload(MultipartFile multipartFile, Uploadable uploadable) throws IOException;

    public abstract void delete(Uploadable uploadable);

    public Resource loadFileAsResource(String fileName, String folder) {
        return null;
    }
}
