package org.example.documind.Services;



import lombok.RequiredArgsConstructor;
import org.example.documind.Entity.Document;
import org.example.documind.Repository.DocumentRepository;
import org.example.documind.platform.modules.ai.service.IngestionService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final IngestionService ingestionService; // Inject the AI Service
    private final Path fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();

    // Init folder
    {
        try { Files.createDirectories(this.fileStorageLocation); } catch (Exception e) {}
    }

    public Document uploadDocument(MultipartFile file) throws IOException {
        // 1. Save File to Disk
        String filename = file.getOriginalFilename();
        Path targetLocation = this.fileStorageLocation.resolve(filename);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        // 2. Save Metadata to DB (Initial state: processed = false)
        Document doc = Document.builder()
                .filename(filename)
                .contentType(file.getContentType())
                .size(file.getSize())
                .processed(false)
                .build();
        Document savedDoc = documentRepository.save(doc);

        // 3. TRIGGER AI INGESTION (This was missing!)
        try {
            System.out.println("🚀 Triggering AI Ingestion for: " + filename); // Added explicit print
            ingestionService.ingestFile(targetLocation);

            // If successful, update status
            savedDoc.setProcessed(true);
            documentRepository.save(savedDoc);
            System.out.println("✅ AI Ingestion Success!");

        } catch (Exception e) {
            System.err.println("❌ AI Ingestion Failed: " + e.getMessage());
            e.printStackTrace();
        }

        return savedDoc;
    }
}