package com.kappa.service.impl;

import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import com.kappa.error.FileStorageException;
import com.kappa.model.dto.Uploadable;
import com.kappa.service.StorageService;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@DependsOn("firebaseStorage")
@ConditionalOnProperty(prefix = "storage", name = "storage-type", havingValue = "firebase")
public class FirebaseStorageServiceImpl extends StorageService {

    private final StorageClient storageClient;

    @Autowired
    public FirebaseStorageServiceImpl(StorageClient storageClient) {
        this.storageClient = storageClient;
    }

    @Override
    public String upload(MultipartFile multipartFile, Uploadable uploadable) {
        String ext = this.getExtension(multipartFile);
        String fileName = uploadable.createFileName(ext);
        this.normalizeFileName(fileName);
        Bucket bucket = storageClient.bucket();
        try {
            InputStream fileInputStream = multipartFile.getInputStream();
            String blobString = uploadable.getFolder() + "/" + fileName;
            Blob blob = bucket.create(blobString, fileInputStream, Bucket.BlobWriteOption.userProject("climax-sound"));
            bucket.getStorage().updateAcl(blob.getBlobId(), Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));
            String blobName = blob.getName();
            uploadable.setBlobString(blobName);
            return blob.getMediaLink();
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    @Override
    public void delete(Uploadable uploadable) {
        String blobString = uploadable.getBlobString();
        BlobId blobId = BlobId.of(storageClient.bucket().getName(), blobString);
        storageClient.bucket().getStorage().delete(blobId);
    }
}
