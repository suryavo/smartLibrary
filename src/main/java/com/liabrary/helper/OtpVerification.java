package com.liabrary.helper;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@EnableWebMvc
@Entity
@Table(name="OTPVERIFICATION")
public class OtpVerification {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="OTP_SEQ")
    @SequenceGenerator(name="OTP_SEQ", sequenceName="OTP_SEQ", allocationSize=1)
	private int otp_id;
	private int book_id;
	private int sender_user_id;
	private int receiver_user_id;
	private String otp;
	public OtpVerification() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public int getOtp_id() {
		return otp_id;
	}

	public void setOtp_id(int otp_id) {
		this.otp_id = otp_id;
	}

	public int getBook_id() {
		return book_id;
	}
	public void setBook_id(int book_id) {
		this.book_id = book_id;
	}
	public int getSender_user_id() {
		return sender_user_id;
	}
	public void setSender_user_id(int sender_user_id) {
		this.sender_user_id = sender_user_id;
	}
	public int getReceiver_user_id() {
		return receiver_user_id;
	}
	public void setReceiver_user_id(int receiver_user_id) {
		this.receiver_user_id = receiver_user_id;
	}
	public String getOtp() {
		return otp;
	}
	public void setOtp(String otp) {
		this.otp = otp;
	}
	
}
