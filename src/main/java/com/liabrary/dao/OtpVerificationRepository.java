package com.liabrary.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.liabrary.helper.OtpVerification;

public interface OtpVerificationRepository extends JpaRepository<OtpVerification, Integer>{
	@Query("select O from OtpVerification O where O.receiver_user_id= :receiver_user_id")
	public List<OtpVerification> findOtpByReceiver(@Param("receiver_user_id") int receiver_user_id);
	
	@Query("select O.otp from OtpVerification O where O.receiver_user_id= :receiver_user_id AND O.sender_user_id= :sender_user_id AND O.book_id= :book_id")
	public String findOtpBySenderAndReceiverAndBook(@Param("receiver_user_id")int receiver_user_id, @Param("sender_user_id")int sender_user_id, @Param("book_id")int book_id);
	
	@Query("select O from OtpVerification O where O.otp= :otp")
	public OtpVerification findOtpVerificationByOtp(@Param("otp") String otp);
}
