package com.liabrary.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.liabrary.dao.ChatMessageRepository;
import com.liabrary.dao.ChatNotificationRepository;
import com.liabrary.dao.UserRepository;
import com.liabrary.entities.ChatMessage;
import com.liabrary.entities.User;
import com.liabrary.helper.ChatNotification;

@RestController
public class ChatResponseEntity {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ChatMessageRepository chatMessageRepository;
	@Autowired
	private ChatNotificationRepository chatNotificationRepository;
	
	
	@GetMapping("/user/chat/refresh/{to_user}")
	public ResponseEntity<?> usersearch(@PathVariable("to_user") int to_user, Principal principal){
		
		String userName=principal.getName();
		User user=userRepository.findByUsername(userName);
		
		User receiver=userRepository.getById(to_user);
		
		List<ChatMessage> chatMessages=this.chatMessageRepository.chatMessages(user, receiver);
		
		ChatNotification cn=this.chatNotificationRepository.findChatNotificationBySenderAndReceiver(to_user, user.getUser_id());
		if(cn!=null) {
			int total_messages=cn.getTotal_messages();
			cn.setSeen_messages(total_messages);
			ChatNotification cnres=this.chatNotificationRepository.save(cn);
		}
		

		return ResponseEntity.ok(chatMessages);
	}
	
	
	
	@GetMapping("/user/chat/add/{to_user}/{message}")
	public ResponseEntity<?> addMessage(@PathVariable("to_user") int to_user, @PathVariable("message") String message, Principal principal){
		
		String userName=principal.getName();
		User user=userRepository.findByUsername(userName);
		
		User receiver=userRepository.getById(to_user);
		
		ChatMessage cm=new ChatMessage();
		cm.setMessage(message);
		cm.setReceiver(receiver);
		cm.setSender(user);
		cm.setReceiver_id(to_user);
		cm.setSender_id(user.getUser_id());
		
		Date today = new Date();
		Calendar cal=Calendar.getInstance();
		today=cal.getTime();
		cm.setDate(today);
		
		ChatMessage res=this.chatMessageRepository.save(cm);
		
		
		ChatNotification cn=this.chatNotificationRepository.findChatNotificationBySenderAndReceiver(user.getUser_id(), to_user);
		if(cn==null) {
			cn=new ChatNotification();
			cn.setTotal_messages(1);
			cn.setSeen_messages(0);
			cn.setSender_id(user.getUser_id());
			cn.setReceiver_id(to_user);
		}
		int total_messages=cn.getTotal_messages();
		total_messages+=1;
		cn.setTotal_messages(total_messages);
		
		ChatNotification cnres=this.chatNotificationRepository.save(cn);
		
		
		
		return ResponseEntity.ok(res);
	}
	
	
	@GetMapping("/user/chat/notification")
	public ResponseEntity<?> shoNotification(Principal principal){
		
		String userName=principal.getName();
		User user=userRepository.findByUsername(userName);
		
	
		List<ChatNotification> cn=this.chatNotificationRepository.findChatNotificationByReceiver(user.getUser_id());
		if(cn.size()==0) {
			cn.add(new ChatNotification(Integer.MAX_VALUE, Integer.MAX_VALUE, user.getUser_id(), 0, 0, 0));
		}

		return ResponseEntity.ok(cn);
	}
	
	
	@GetMapping("/user/chat/refresh/users")
	public ResponseEntity<?> chatusers(Principal principal){
		
		String userName=principal.getName();
		User user=userRepository.findByUsername(userName);
		

		
		Set<User> sendersAndReceivers=chatMessageRepository.receivers(user);
		sendersAndReceivers.addAll(chatMessageRepository.senders(user));
		if(sendersAndReceivers.contains(user)) sendersAndReceivers.remove(user);
		List<User> sendersAndReceiversList=new ArrayList<>();
		for (User u: sendersAndReceivers) {
			sendersAndReceiversList.add(u);
        }
		

		return ResponseEntity.ok(sendersAndReceiversList);
	}
	
	@GetMapping("/user/chat/refresh/users/count")
	public ResponseEntity<?> countchatusers(Principal principal){
		
		String userName=principal.getName();
		User user=userRepository.findByUsername(userName);
		
		int count=0;
		
		Set<User> sendersAndReceivers=chatMessageRepository.receivers(user);
		sendersAndReceivers.addAll(chatMessageRepository.senders(user));
		if(sendersAndReceivers.contains(user)) sendersAndReceivers.remove(user);
		for (User u: sendersAndReceivers) {
			count++;
        }
		

		return ResponseEntity.ok(count);
	}

}
