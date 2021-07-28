package com.liabrary.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.liabrary.dao.OtpVerificationRepository;
import com.liabrary.helper.OtpVerification;

@Service
public class OtpDeletionService {
	
	@Autowired
	private OtpVerificationRepository otpVerificationRepository;
	
	@Async
	public void deleteotp(String otp) {
		
		try {
		    Thread.sleep(60000);
		    System.out.println("deleteing the otp");
		    OtpVerification otpVerification=this.otpVerificationRepository.findOtpVerificationByOtp(otp);
			otpVerificationRepository.deleteById(otpVerification.getOtp_id());
			
		} catch (InterruptedException ie) {
		    Thread.currentThread().interrupt();
		}
		
		
	}
	@Async
	public void deleteotpnow(String otp) {
		OtpVerification otpVerification=this.otpVerificationRepository.findOtpVerificationByOtp(otp);
		otpVerificationRepository.deleteById(otpVerification.getOtp_id());
	}

}
