package com.kappa.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kappa.constant.ChatDestinationName;
import com.kappa.constant.MessageCommand;
import com.kappa.model.entity.Attachment;
import com.kappa.service.DownloadService;
import com.kappa.service.MessageService;
import com.kappa.service.StorageService;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/resource")
@CrossOrigin(originPatterns = "*", allowCredentials = "true", allowedHeaders = "*",
    exposedHeaders = {HttpHeaders.SET_COOKIE})
public class ResourceController {

    private final DownloadService downloadService;

    private final StorageService storageService;

    private final MessageService messageService;

    private final SimpMessageSendingOperations template;

    private final ObjectMapper objectMapper;

    @Autowired
    public ResourceController(DownloadService downloadService,
        StorageService storageService, MessageService messageService,
        SimpMessageSendingOperations template,
        ObjectMapper objectMapper) {
        this.downloadService = downloadService;
        this.storageService = storageService;
        this.messageService = messageService;
        this.template = template;
        this.objectMapper = objectMapper;
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/download/{folder}/{fileName:.+}")
    public ResponseEntity<Resource> getAvatar(@PathVariable("fileName") String fileName,
        @PathVariable("folder") String folder,
        HttpServletRequest request) {
        return this.downloadService.generateUrl(fileName, folder, request);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/upload/attachment")
    public ResponseEntity<Attachment> uploadAttachment(
        @RequestParam("file") MultipartFile multipartFile,
        @RequestParam("attachment") String attachmentStr) throws IOException {
        Attachment attachment = this.objectMapper.readValue(attachmentStr, Attachment.class);
        String fileDownloadUri = this.storageService.upload(multipartFile, attachment);
        attachment.setUrl(fileDownloadUri);
        this.messageService.updateMessage(attachment);
        attachment.setCommand(MessageCommand.UPDATE);
        this.template.convertAndSendToUser(attachment.getFrom(), ChatDestinationName.PRIVATE_CHAT, attachment);
        this.template.convertAndSendToUser(attachment.getTo(), ChatDestinationName.PRIVATE_CHAT, attachment);
        return new ResponseEntity<>(attachment, HttpStatus.OK);
    }
}
