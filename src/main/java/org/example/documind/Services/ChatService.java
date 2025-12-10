package org.example.documind.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final VectorStore vectorStore;
    private final ChatClient.Builder chatClientBuilder;

    public String chatWithDocument(String userQuestion) {
        // 1. Search the Vector Store for relevant chunks (Similarity Search)
        List<Document> similarDocuments = vectorStore.similaritySearch(
                SearchRequest.query(userQuestion).withTopK(2) // Get top 2 most relevant chunks
        );

        // 2. Combine the chunks into a single string (Context)
        String context = similarDocuments.stream()
                .map(Document::getContent)
                .collect(Collectors.joining("\n"));

        // 3. Build the Prompt (RAG Pattern)
        String prompt = """
                You are a helpful assistant. Use the following context to answer the question.
                If the answer is not in the context, say "I don't know".
                
                CONTEXT:
                %s
                
                QUESTION:
                %s
                """.formatted(context, userQuestion);

        // 4. Call Ollama to generate the answer
        return chatClientBuilder.build()
                .prompt(prompt)
                .call()
                .content();
    }
}