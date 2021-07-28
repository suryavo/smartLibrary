package com.liabrary.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Entity
@Table(name="TRANSACTIONRECORD")
@EnableWebMvc
public class TransactionRecord {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="RECORD_SEQ")
    @SequenceGenerator(name="RECORD_SEQ", sequenceName="RECORD_SEQ", allocationSize=1)
	private int record_id;
	private int unique_book_id;
	@ManyToOne
	private User current_user;
	@ManyToOne
	private User from_user;
	@ManyToOne
	private User to_user;
	private Date issue_date;
	private Date expected_return_date;
	private Date return_date;
	private int fine;
	private boolean returned;
	
	public TransactionRecord() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getRecord_id() {
		return record_id;
	}

	public void setRecord_id(int record_id) {
		this.record_id = record_id;
	}

	public int getUnique_book_id() {
		return unique_book_id;
	}

	public void setUnique_book_id(int unique_book_id) {
		this.unique_book_id = unique_book_id;
	}

	public User getCurrent_user() {
		return current_user;
	}

	public void setCurrent_user(User current_user) {
		this.current_user = current_user;
	}

	public User getFrom_user() {
		return from_user;
	}

	public void setFrom_user(User from_user) {
		this.from_user = from_user;
	}

	public User getTo_user() {
		return to_user;
	}

	public void setTo_user(User to_user) {
		this.to_user = to_user;
	}

	public Date getIssue_date() {
		return issue_date;
	}

	public void setIssue_date(Date issue_date) {
		this.issue_date = issue_date;
	}

	public Date getExpected_return_date() {
		return expected_return_date;
	}

	public void setExpected_return_date(Date expected_return_date) {
		this.expected_return_date = expected_return_date;
	}

	public Date getReturn_date() {
		return return_date;
	}

	public void setReturn_date(Date return_date) {
		this.return_date = return_date;
	}

	public int getFine() {
		return fine;
	}

	public void setFine(int fine) {
		this.fine = fine;
	}
	

	public boolean isReturned() {
		return returned;
	}

	public void setReturned(boolean returned) {
		this.returned = returned;
	}

	@Override
	public String toString() {
		return "TransactionRecord [record_id=" + record_id + ", unique_book_id=" + unique_book_id + ", current_user="
				+ current_user + ", from_user=" + from_user + ", to_user=" + to_user + ", issue_date=" + issue_date
				+ ", expected_return_date=" + expected_return_date + ", return_date=" + return_date + ", fine=" + fine
				+ ", returned=" + returned + "]";
	}


	


	
	
	
}
