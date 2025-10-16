package com.dev.job.repository.Communication;

import com.dev.job.entity.communication.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
    @Query("SELECT m from Message m where" +
            "(m.senderId = :user1 and m.receiverId = :user2) or" +
            "(m.senderId = :user2 and m.receiverId = :user1)" +
            "order by m.timestamp asc")
    List<Message> findChatBetweenUsers(@Param("user1") String user1, @Param("user2") String user2);

    @Query("SELECT m FROM Message m WHERE m.senderId = :userId OR m.receiverId = :userId ORDER BY m.timestamp DESC")
    List<Message> findAllMessageByUser(@Param("userId") String userId);
}
