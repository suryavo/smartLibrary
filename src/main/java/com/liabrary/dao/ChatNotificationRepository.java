package com.liabrary.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.liabrary.helper.ChatNotification;

public interface ChatNotificationRepository extends JpaRepository<ChatNotification, Integer>{
	
	@Query("select CN from ChatNotification CN where CN.receiver_id= :receiver_id")
	public List<ChatNotification> findChatNotificationByReceiver(@Param("receiver_id") int receiver_id);
	
	@Query("select CN from ChatNotification CN where CN.sender_id= :sender_id and CN.receiver_id= :receiver_id")
	public ChatNotification findChatNotificationBySenderAndReceiver(@Param("sender_id") int sender_id, @Param("receiver_id") int receiver_id);

}
