package com.liabrary.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.liabrary.entities.Book;
import com.liabrary.entities.BookStock;
import com.liabrary.entities.User;

public interface BookStockRepository extends JpaRepository<BookStock, Integer>{
	
	@Query("select bs from BookStock bs where bs.user= :user")
	public List<BookStock> findMyBooks(@Param("user") User user);  
	
	@Query("select bs from BookStock bs where bs.book= :book")
	public List<BookStock> findBookStockHavingTheBook(@Param("book") Book book);
	
	@Query("select bs.book from BookStock bs where bs.unique_book_id= :unique_book_id")
	public Book findBookByUnique_Book_Id(@Param("unique_book_id") int unique_book_id);

}
