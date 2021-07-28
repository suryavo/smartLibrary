package com.liabrary.dao;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.liabrary.entities.ChatMessage;
import com.liabrary.entities.User;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Integer>{
	
	@Query("select c from ChatMessage c where c.sender= :sender and c.receiver= :receiver or c.sender= :receiver and c.receiver= :sender")
	public List<ChatMessage> chatMessages(@Param("sender") User sender, @Param("receiver") User receiver);
	
	@Query("select c.sender from ChatMessage c where c.receiver= :user")
	public Set<User> senders(@Param("user") User user);
	
	@Query("select c.receiver from ChatMessage c where c.sender= :user")
	public Set<User> receivers(@Param("user") User user);

}
