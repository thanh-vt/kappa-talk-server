package com.kappa.service.impl;

import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import com.kappa.constant.CommonConstant;
import com.kappa.error.FileStorageException;
import com.kappa.model.dto.Uploadable;
import com.kappa.service.StorageService;
import java.io.IOException;
import java.io.InputStream;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
  * @created 25/04/2021 - 12:34:56 SA
  * @project vengeance
  * @author thanhvt
  * @description upload service using Firebase Storage
  * @since 1.0
**/
@Log4j2
@Service
@DependsOn("firebaseStorage")
@ConditionalOnProperty(prefix = "storage", name = "storage-type", havingValue = "firebase")
public class FirebaseStorageServiceImpl extends StorageService {

    private final StorageClient storageClient;

    @Autowired
    public FirebaseStorageServiceImpl(StorageClient storageClient) {
        this.storageClient = storageClient;
    }

    /**
     * @created 25/04/2021 - 12:28:37 SA
     * @author thanhvt
     * @description
     * @param multipartFile file to be uploaded
     * @param uploadable upload file info
     * @return a string represent url to download file
     */
    @Override
    public String upload(MultipartFile multipartFile, Uploadable uploadable) {
        String ext = this.getExtension(multipartFile);
        String fileName = uploadable.createFileName(ext);
        this.normalizeFileName(fileName);
        Bucket bucket = storageClient.bucket();
        try {
            InputStream fileInputStream = multipartFile.getInputStream();
            String blobString = uploadable.getFolder() + CommonConstant.SLASH + fileName;
            Blob blob = bucket.create(blobString, fileInputStream, Bucket.BlobWriteOption.userProject("climax-sound"));
            bucket.getStorage().updateAcl(blob.getBlobId(), Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));
            String blobName = blob.getName();
            uploadable.setBlobString(blobName);
            return blob.getMediaLink();
        } catch (IOException ex) {
            log.error(ex);
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    /**
     * @created 25/04/2021 - 12:35:45 SA
     * @author thanhvt
     * @description delete file on Firebase Storage
     * @param uploadable upload file info
     */
    @Override
    public void delete(Uploadable uploadable) {
        String blobString = uploadable.getBlobString();
        BlobId blobId = BlobId.of(storageClient.bucket().getName(), blobString);
        boolean deleteSuccess = storageClient.bucket().getStorage().delete(blobId);
        log.info("Delete file {} {}", blobString, deleteSuccess ? "successfully" : "failed");
    }
}
