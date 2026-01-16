package chatapp.service;

import chatapp.model.Message;
import chatapp.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }

    public List<Message> getGroupChatHistory() {
        // Busca todos los mensajes y los ordena por fecha de envío
        return messageRepository.findAll(Sort.by(Sort.Direction.ASC, "timestamp"));
    }
}
