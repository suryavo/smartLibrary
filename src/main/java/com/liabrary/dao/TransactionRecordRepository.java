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
	
	@Query("select tr from TransactionRecord tr where tr.current_user= :user OR tr.from_user= :user  OR tr.to_user= :user  AND ISNULL(DATE_FORMAT(tr.issue_date , '%Y-%m-%d'),'') like %:borrowDate% AND ISNULL(DATE_FORMAT(tr.expected_return_date , '%Y-%m-%d'),'') like %:expectedReturnDate% AND ISNULL(DATE_FORMAT(tr.return_date , '%Y-%m-%d'),'') like %:returnDate% AND tr.unique_book_id= :book_id")
	public List<TransactionRecord> findbydate(@Param("user") User user, @Param("borrowDate") String borrowDate, @Param("expectedReturnDate") String expectedReturnDate, @Param("returnDate") String returnDate, @Param("book_id") int book_id);
}
