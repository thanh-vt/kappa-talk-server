package com.kappa.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.kappa.model.dto.Uploadable;
import com.kappa.service.StorageService;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import lombok.extern.log4j.Log4j2;
import org.cloudinary.json.JSONArray;
import org.cloudinary.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
@Service
@DependsOn("cloudinary")
@ConditionalOnProperty(prefix = "storage", name = "storage-type", havingValue = "cloudinary")
public class CloudinaryStorageServiceImpl extends StorageService {

    @Value("${storage.temp}")
    private String tempFolder;

    private final Cloudinary cloudinary;

    private final ServletContext servletContext;

    @Autowired
    public CloudinaryStorageServiceImpl(Cloudinary cloudinary, ServletContext servletContext) {
        this.cloudinary = cloudinary;
        this.servletContext = servletContext;
    }

    @Override
    public String upload(MultipartFile multipartFile, Uploadable uploadable) throws IOException {
        File tmpDir = new File(servletContext.getRealPath("/") + this.tempFolder);
        if (!tmpDir.exists()) {
            log.info("Created temp folder? {}", tmpDir.mkdir());
        }
        String ext = this.getExtension(multipartFile);
        String filename = uploadable.createFileName(ext);
        File tmpFile = new File(tmpDir, filename);
        if (!tmpFile.exists()) {
            log.info("Created temp file? {}", tmpFile.createNewFile());
        }
        log.info("Path: {}", tmpFile.getCanonicalPath());
        multipartFile.transferTo(tmpFile);
        JSONArray accessControl = new JSONArray();
        JSONObject accessType = new JSONObject();
        accessType.put("access_type", "anonymous");
        accessControl.put(accessType);
        Map<?, ?> params = ObjectUtils.asMap(
            "use_filename", true,
            "folder", uploadable.getFolder(),
            "unique_filename", false,
            "overwrite", true,
            "resource_type", "auto",
            "access_control", accessControl
        );
        Map<?, ?> uploadResult = this.cloudinary.uploader().upload(tmpFile, params);
        log.info("Delete temp file? {}", tmpFile.delete());
        String publicId = (String) uploadResult.get("public_id");
        uploadable.setBlobString(publicId);
        return (String) uploadResult.get("secure_url");
    }

    @Override
    public void delete(Uploadable uploadable) {
        Map<String, Object> deleteOption = new HashMap<>();
        deleteOption.put("invalidate", true);
        Map<?, ?> deleteResult;
        try {
            deleteResult = this.cloudinary.uploader()
                .destroy(uploadable.getBlobString(), deleteOption);
            if (deleteResult.get("result").equals("ok")) {
                log.info("Delete resource success {}", uploadable.getBlobString());
            } else {
                log.error("Delete resource failed {}", uploadable.getBlobString());
            }
        } catch (IOException ex) {
            log.error("Delete resource failed {} {}", uploadable.getBlobString(), ex);
        }
    }
}
