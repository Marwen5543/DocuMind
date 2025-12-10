package org.example.documind.platform.modules.ai.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.ai.document.Document;

import java.nio.file.Path;
import java.util.List;

@Service
public class IngestionService {

    private static final Logger log = LoggerFactory.getLogger(IngestionService.class);
    private final VectorStore vectorStore;

    public IngestionService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public void ingestFile(Path filePath) {
        log.info("Starting ingestion for file: {}", filePath);

        // 1. Read PDF (Apache Tika)
        TikaDocumentReader reader = new TikaDocumentReader(new FileSystemResource(filePath));
        List<Document> documents = reader.get();

        // 2. Split Text (Chunks)
        TokenTextSplitter splitter = new TokenTextSplitter();
        List<Document> splitDocuments = splitter.apply(documents);

        // 3. Save Vectors to Postgres (AI Magic)
        vectorStore.accept(splitDocuments);

        log.info("Ingestion complete! Stored {} chunks.", splitDocuments.size());
    }
}