package com.liabrary.helper;

import javax.validation.constraints.NotBlank;

import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
public class AssignBook {
	
	int user_id;
	int unique_book_id;
	
	public AssignBook() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public int getUnique_book_id() {
		return unique_book_id;
	}
	public void setUnique_book_id(int unique_book_id) {
		this.unique_book_id = unique_book_id;
	}
}
