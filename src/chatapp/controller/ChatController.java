package chatapp.controller;

import chatapp.model.Message;
import chatapp.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MessageService messageService;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload Message chatMessage, Principal principal) {
        chatMessage.setSender(principal.getName());
        chatMessage.setTimestamp(LocalDateTime.now());
        messageService.saveMessage(chatMessage);
        messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipient(), "/topic/messages", chatMessage);
    }

    @GetMapping("/api/messages/{recipient}")
    public ResponseEntity<List<Message>> getChatHistory(@PathVariable String recipient, Principal principal) {
        return ResponseEntity.ok(messageService.getChatHistory(principal.getName(), recipient));
    }
}
