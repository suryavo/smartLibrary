package com.liabrary.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.liabrary.dao.BookRepository;
import com.liabrary.entities.Book;

@RestController
public class SearchController {
	
	@Autowired
	private BookRepository bookRepository;
	
	@GetMapping("/admin/search/{query}")
	public ResponseEntity<?> adminsearch(@PathVariable("query") String query){
		List<Book> books=this.bookRepository.findBookByName(query);
		return ResponseEntity.ok(books);
	}
	
	@GetMapping("/user/search/{query}")
	public ResponseEntity<?> usersearch(@PathVariable("query") String query){
		List<Book> books=this.bookRepository.findBookByName(query);
		return ResponseEntity.ok(books);
	}

	
}
