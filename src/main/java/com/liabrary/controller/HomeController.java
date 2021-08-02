package com.liabrary.controller;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.liabrary.dao.UserRepository;
import com.liabrary.entities.User;
import com.liabrary.helper.Message;


@Controller
public class HomeController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	
	@RequestMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title","Signup-Smart Liabrary");
		model.addAttribute("user", new User());
		return "signup";
	}
	
	@RequestMapping("/signin")
	public String signin(@ModelAttribute("user") User user, Model model) {
		model.addAttribute("title","Login-Smart Liabrary");
		model.addAttribute("user", user);
		return "signin";
	}
	
	
	
	//handler for sign up user
	@PostMapping("/do_signup")
	public String signupUser(@Valid @ModelAttribute("user") User user, BindingResult res1, Model model, HttpSession session) {
		
		try {
					
			
			if(res1.hasErrors()) {
				System.out.println(res1);
				model.addAttribute("user", user);
				return "signup";
			}
			
			Date today = new Date();
			user.setRole("ROLE_USER");
			user.setProfile_activity(true);
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			
			if(user.getYear().equals("1st")) {
				System.out.println(user.getYear());
				Calendar cal=Calendar.getInstance();
				cal.add(Calendar.YEAR,3);
				today=cal.getTime();
				user.setExpiry_date(today);
			}
			
			else if(user.getYear().equals("2nd")) {
				System.out.println(user.getYear());
				Calendar cal=Calendar.getInstance();
				cal.add(Calendar.YEAR,2);
				today=cal.getTime();
				user.setExpiry_date(today);
			}
			
			else if(user.getYear().equals("3rd")) {
				System.out.println(user.getYear());
				Calendar cal=Calendar.getInstance();
				cal.add(Calendar.YEAR,1);
				today=cal.getTime();
				user.setExpiry_date(today);
			}
			
			else if(user.getYear().equals("4th")) {
				System.out.println(user.getYear());
				Calendar cal=Calendar.getInstance();
				cal.add(Calendar.MONTH,6);
				today=cal.getTime();
				user.setExpiry_date(today);
			}

			
			User result=this.userRepository.save(user);
			
			model.addAttribute("user", new User());
			session.setAttribute("message", new Message("Successfully Registered ", "alert-success"));
			return "signup";
			
		} catch (Exception e) {

			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("Sorry! Something went wrong !! ", "alert-danger"));
			return "signup";
		}
		
		
		
	}
}
