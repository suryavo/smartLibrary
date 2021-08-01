package com.liabrary.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.liabrary.entities.TransactionRecord;
import com.liabrary.entities.User;

public interface TransactionRecordRepository extends JpaRepository<TransactionRecord, Integer>{
	
	@Query("select tr from TransactionRecord tr where tr.record_id= :id")
	public TransactionRecord findByTransactionId(@Param("id") int id);
	
	@Query("select tr from TransactionRecord tr where tr.current_user= :user AND tr.unique_book_id= :unique_book_id AND tr.returned= :status")
	public TransactionRecord findTransactionRecordByCurrentUserAndBookAndStatus(@Param("user") User user, @Param("unique_book_id") int unique_book_id, @Param("status") boolean status);
	
	@Query("select tr from TransactionRecord tr where tr.current_user= :user AND tr.returned= :status")
	public List<TransactionRecord> findTransactionRecordByCurrentUserAndStatus(@Param("user") User user, @Param("status") boolean status);
	
	
	
	
	
	@Query("select tr from TransactionRecord tr where tr.current_user= :user OR tr.from_user= :user OR tr.to_user= :user")
	public List<TransactionRecord> findRecordByUser(@Param("user") User user);
	
	@Query("select tr from TransactionRecord tr where tr.unique_book_id= :unique_book_id")
	public List<TransactionRecord> findRecordByBookId(@Param("unique_book_id") int unique_book_id);
	
	@Query("select tr from TransactionRecord tr where tr.issue_date= :borrowDate")
	public List<TransactionRecord> findRecordByBorrowDate(@Param("borrowDate") Date borrowDate);

	@Query("select tr from TransactionRecord tr where tr.expected_return_date= :expectedReturnDate")
	public List<TransactionRecord> findRecordByExpectedReturnDate(@Param("expectedReturnDate") Date expectedReturnDate);
	
	@Query("select tr from TransactionRecord tr where tr.return_date= :returnDate")
	public List<TransactionRecord> findRecordByReturnDate(@Param("returnDate") Date returnDate);
	
	@Query("select tr from TransactionRecord tr where tr.returned= :status")
	public List<TransactionRecord> findRecordByStatus(@Param("status") boolean status);
}
