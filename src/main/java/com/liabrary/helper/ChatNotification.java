package com.liabrary.helper;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class ChatNotification {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="NOTIFICATION_SEQ")
    @SequenceGenerator(name="NOTIFICATION_SEQ", sequenceName="NOTIFICATION_SEQ", allocationSize=1)
	private int notification_id;
	private int sender_id;
	private int receiver_id;
	private int seen_messages;
	private int total_messages;
	private int new_messages;
	public ChatNotification() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getNotification_id() {
		return notification_id;
	}
	public void setNotification_id(int notification_id) {
		this.notification_id = notification_id;
	}
	public int getSender_id() {
		return sender_id;
	}
	public void setSender_id(int sender_id) {
		this.sender_id = sender_id;
	}
	public int getSeen_messages() {
		return seen_messages;
	}
	public void setSeen_messages(int seen_messages) {
		this.seen_messages = seen_messages;
	}
	public int getTotal_messages() {
		return total_messages;
	}
	public void setTotal_messages(int total_messages) {
		this.total_messages = total_messages;
	}
	public int getNew_messages() {
		return new_messages;
	}
	public void setNew_messages(int new_messages) {
		this.new_messages = new_messages;
	}
	public int getReceiver_id() {
		return receiver_id;
	}
	public void setReceiver_id(int receiver_id) {
		this.receiver_id = receiver_id;
	}
	public ChatNotification(int notification_id, int sender_id, int receiver_id, int seen_messages, int total_messages, int new_messages) {
		super();
		this.notification_id = notification_id;
		this.sender_id = sender_id;
		this.receiver_id = receiver_id;
		this.seen_messages = seen_messages;
		this.total_messages = total_messages;
		this.new_messages = new_messages;
	}
	
	
	

}
