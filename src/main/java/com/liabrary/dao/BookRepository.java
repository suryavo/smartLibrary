package com.liabrary.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.liabrary.entities.Book;
import com.liabrary.entities.BookStock;
import com.liabrary.entities.User;


public interface BookRepository extends JpaRepository<Book, Integer>{
	@Query("select b from Book b where b.book_name like %:book_name%")
	public List<Book> findBookByName(@Param("book_name") String book_name);
	
	@Query("select b from Book b where b.book_id= :book_id")
	public Book findSingleBookById(@Param("book_id") int book_id);
	
	@Query("select b from Book b where b.book_name like %:book_name%")
	public Page<Book> findBookByNamePage(@Param("book_name") String book_name, Pageable pagedata);

	
}
