package org.example.documind.Controller;

import lombok.RequiredArgsConstructor;
import org.example.documind.Services.ChatService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ChatController {

    private final ChatService chatService;

    // Endpoint: POST /api/chat?question=...
    @PostMapping
    public Map<String, String> chat(@RequestParam String question) {
        String answer = chatService.chatWithDocument(question);
        return Map.of("answer", answer);
    }
}