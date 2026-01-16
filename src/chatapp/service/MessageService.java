package chatapp.service;

import chatapp.model.Message;
import chatapp.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }

    public List<Message> getChatHistory(String sender, String recipient) {
        return messageRepository.findBySenderAndRecipientOrRecipientAndSenderOrderByTimestampAsc(sender, recipient, sender, recipient);
    }
}
