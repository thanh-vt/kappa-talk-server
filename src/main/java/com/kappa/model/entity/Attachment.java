package com.kappa.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kappa.model.dto.Uploadable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Attachment extends Message implements Uploadable {

    private String uploadId;

    private String fileName;

    private String url;

    private String blobString;

    private String uploadStatus;

    @Override
    public String getUrl() {
        return this.url;
    }

    @Override
    public String createFileName(String ext) {
        return this.fileName.concat(".").concat(ext);
    }

    @Override
    @JsonIgnore
    public String getFolder() {
        return "attachment";
    }

    @Override
    public String getBlobString() {
        return this.blobString;
    }

    @Override
    public void setBlobString(String blobString) {
        this.blobString = blobString;
    }
}
