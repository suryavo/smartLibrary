package com.liabrary.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.liabrary.entities.TransactionRecord;
import com.liabrary.entities.User;

public interface TransactionRecordRepository extends JpaRepository<TransactionRecord, Integer>{
	@Query("select tr from TransactionRecord tr where tr.current_user= :user AND tr.unique_book_id= :unique_book_id AND tr.returned= :status")
	public TransactionRecord findTransactionRecordByCurrentUserAndBookAndStatus(@Param("user") User user, @Param("unique_book_id") int unique_book_id, @Param("status") boolean status);
	
	@Query("select tr from TransactionRecord tr where tr.current_user= :user AND tr.returned= :status")
	public List<TransactionRecord> findTransactionRecordByCurrentUserAndStatus(@Param("user") User user, @Param("status") boolean status);
}
