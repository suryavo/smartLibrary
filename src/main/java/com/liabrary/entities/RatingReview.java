package com.liabrary.entities;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Entity
@Table(name="RATINGREVIEW")
@EnableWebMvc
public class RatingReview {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="RATING_SEQ")
    @SequenceGenerator(name="RATING_SEQ", sequenceName="RATING_SEQ", allocationSize=1)
	private int rating_id;
	@ManyToOne
	private Book book;
	private int rating;
	private String review;
	@ManyToOne
	private User user;
	public RatingReview() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getRating_id() {
		return rating_id;
	}
	public void setRating_id(int rating_id) {
		this.rating_id = rating_id;
	}
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	
	
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	public String getReview() {
		return review;
	}
	public void setReview(String review) {
		this.review = review;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	

}
