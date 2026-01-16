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

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        
        Message savedMessage = messageService.saveMessage(chatMessage);

        // Construye un mapa simple para enviar como JSON
        Map<String, String> messageMap = Map.of(
                "sender", savedMessage.getSender(),
                "content", savedMessage.getContent(),
                "type", savedMessage.getType().toString(),
                "timestamp", savedMessage.getTimestamp().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        );

        messagingTemplate.convertAndSend("/topic/group", messageMap);
    }

    @GetMapping("/api/messages/group")
    public ResponseEntity<List<Map<String, String>>> getGroupChatHistory() {
        List<Message> messages = messageService.getGroupChatHistory();
        
        // Convierte la lista de mensajes a una lista de mapas simples
        List<Map<String, String>> messageMaps = messages.stream().map(msg -> Map.of(
                "sender", msg.getSender(),
                "content", msg.getContent(),
                "type", msg.getType().toString(),
                "timestamp", msg.getTimestamp().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        )).collect(Collectors.toList());

        return ResponseEntity.ok(messageMaps);
    }
}
