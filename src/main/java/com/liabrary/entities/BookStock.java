package com.liabrary.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="BOOKSTOCK")
public class BookStock {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="BOOKSTOCK_SEQ")
    @SequenceGenerator(name="BOOKSTOCK_SEQ", sequenceName="BOOKSTOCK_SEQ", allocationSize=1)
	private int unique_book_id;
	@ManyToOne
	private Book book;
	@ManyToOne
	private User user;
	
	public BookStock() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getUnique_book_id() {
		return unique_book_id;
	}
	public void setUnique_book_id(int unique_book_id) {
		this.unique_book_id = unique_book_id;
	}
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
}
