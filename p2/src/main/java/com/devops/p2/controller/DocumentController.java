/*package com.devops.p2.controller;

import com.devops.p2.service.Concrete.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class DocumentController {
    private final DocumentService documentService;

    @PostMapping
    public void saveImage(@RequestParam("file") MultipartFile file) {
        documentService.upload(file);
    }
}*/
