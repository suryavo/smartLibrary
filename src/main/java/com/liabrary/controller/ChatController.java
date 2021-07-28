package com.liabrary.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.liabrary.dao.ChatMessageRepository;
import com.liabrary.dao.ChatNotificationRepository;
import com.liabrary.dao.UserRepository;
import com.liabrary.entities.Book;
import com.liabrary.entities.ChatMessage;
import com.liabrary.entities.User;
import com.liabrary.helper.ChatNotification;

@Controller
@RequestMapping("/user/chat")
public class ChatController {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ChatMessageRepository chatMessageRepository;
	@Autowired
	private ChatNotificationRepository chatNotificationRepository;
	
	@GetMapping("/{to_user}")
	public String dashboard(@PathVariable("to_user") int to_user, Principal principal, Model model) {
		
		
		String userName=principal.getName();
		User user=userRepository.findByUsername(userName);
		model.addAttribute("title", user.getUser_name()+"-Smart Library");
		model.addAttribute("user", user);
		
		
		User receiver=userRepository.getById(to_user);
		model.addAttribute("receiver", receiver);
		
		List<ChatMessage> chatMessages=chatMessageRepository.chatMessages(user, receiver);
		model.addAttribute("chatMessages", chatMessages);
		
		Set<User> sendersAndReceivers=chatMessageRepository.receivers(user);
		sendersAndReceivers.addAll(chatMessageRepository.senders(user));
		if(sendersAndReceivers.contains(user)) sendersAndReceivers.remove(user);
		List<User> sendersAndReceiversList=new ArrayList<>();
		for (User u: sendersAndReceivers) {
			sendersAndReceiversList.add(u);
        }
		
		ChatNotification cn=this.chatNotificationRepository.findChatNotificationBySenderAndReceiver(to_user, user.getUser_id());
		if(cn!=null) {
			int total_messages=cn.getTotal_messages();
			cn.setSeen_messages(total_messages);
			ChatNotification cnres=this.chatNotificationRepository.save(cn);
		}
		
		
		
		model.addAttribute("sendersAndReceiversList", sendersAndReceiversList);
		model.addAttribute("pageid", 3);
		if(to_user==0) {
			return "/user/blankchat";
		}
		
		
		return "/user/chat";
		
	}
	
	@PostMapping("/send/{to_user}")
	public void send(@ModelAttribute("chat") ChatMessage chatMessage, @PathVariable("to_user") int to_user, Principal principal, Model model) {
		
		String userName=principal.getName();
		User user=userRepository.findByUsername(userName);
		model.addAttribute("title", user.getUser_name()+"-Smart Library");
		model.addAttribute("user", user);
		
		User receiver=userRepository.getById(to_user);
		model.addAttribute("receiver", receiver);
		
		List<ChatMessage> chatMessages=chatMessageRepository.chatMessages(user, receiver);
		model.addAttribute("chatMessages", chatMessages);
		
		
		Set<User> sendersAndReceivers=chatMessageRepository.receivers(user);
		sendersAndReceivers.addAll(chatMessageRepository.senders(user));
		if(sendersAndReceivers.contains(user)) sendersAndReceivers.remove(user);
		List<User> sendersAndReceiversList=new ArrayList<>();
		for (User u: sendersAndReceivers) {
			sendersAndReceiversList.add(u);
			System.out.println(u);
        }
		model.addAttribute("sendersAndReceiversList", sendersAndReceiversList);
		
		
		chatMessage.setReceiver(receiver);
		chatMessage.setSender(user);
		ChatMessage res=this.chatMessageRepository.save(chatMessage);
		
		model.addAttribute("pageid", 3);
		
	    dashboard(to_user,principal, model);
	    
		
		
	}
	
	
	
	

}


