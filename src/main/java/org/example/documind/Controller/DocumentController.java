package org.example.documind.Controller;


import lombok.RequiredArgsConstructor;
import org.example.documind.Entity.Document;
import org.example.documind.Services.DocumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Document> upload(@RequestParam("file") MultipartFile file) throws IOException {
        Document savedDoc = documentService.uploadDocument(file);
        return ResponseEntity.ok(savedDoc);
    }
}
