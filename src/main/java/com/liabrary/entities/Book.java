package com.liabrary.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="BOOK")
@EnableWebMvc
public class Book {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="BOOK_SEQ")
    @SequenceGenerator(name="BOOK_SEQ", sequenceName="BOOK_SEQ", allocationSize=1)
	private int book_id;
	@NotBlank(message="this field can not be empty")
	private String book_name;
	@NotBlank(message="this field can not be empty")
	private String publisher;
	@NotBlank(message="this field can not be empty")
	private String author;
	@Column(length=1000)
	@NotBlank(message="this field can not be empty")
	private String description;
	@NotBlank(message="this field can not be empty")
	private String edition;
	@NotBlank(message="this field can not be empty")
	private String domain;
	private int book_count;
	private String image;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="book")
	@JsonIgnore
	private List<BookStock> bookstock=new ArrayList<>();

	public Book() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getBook_id() {
		return book_id;
	}
	public void setBook_id(int book_id) {
		this.book_id = book_id;
	}
	public String getBook_name() {
		return book_name;
	}
	public void setBook_name(String book_name) {
		this.book_name = book_name;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getEdition() {
		return edition;
	}
	public void setEdition(String edition) {
		this.edition = edition;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public int getBook_count() {
		return book_count;
	}
	public void setBook_count(int book_count) {
		this.book_count = book_count;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public List<BookStock> getBookstock() {
		return bookstock;
	}
	public void setBookstock(List<BookStock> bookstock) {
		this.bookstock = bookstock;
	}
	
	
	@Override
	public String toString() {
		return "Book [book_id=" + book_id + ", book_name=" + book_name + ", publisher=" + publisher + ", author="
				+ author + ", description=" + description + ", edition=" + edition + ", domain=" + domain + ", image="
				+ image + ", bookstock=" + bookstock + "]";
	}
	
	
	
	
	
}
