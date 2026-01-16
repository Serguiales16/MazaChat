package chatapp.repository;

import chatapp.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderAndRecipientOrRecipientAndSenderOrderByTimestampAsc(String sender1, String recipient1, String sender2, String recipient2);
}
