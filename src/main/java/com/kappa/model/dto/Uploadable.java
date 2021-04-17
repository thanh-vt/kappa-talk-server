package com.kappa.model.dto;

public interface Uploadable {

    String getUrl();

    String createFileName(String ext);

    String getFolder();

    String getBlobString();

    void setBlobString(String blobString);
}
