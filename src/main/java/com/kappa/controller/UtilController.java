package com.kappa.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/util")
@CrossOrigin(originPatterns = "*", allowCredentials = "true", allowedHeaders = "*", exposedHeaders = {HttpHeaders.SET_COOKIE})
public class UtilController {

    @RequestMapping(path = "/ping", method = {RequestMethod.HEAD})
    public ResponseEntity<Void> ping() {
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
