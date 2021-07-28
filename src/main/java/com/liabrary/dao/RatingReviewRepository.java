package com.liabrary.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.liabrary.entities.Book;
import com.liabrary.entities.RatingReview;

public interface RatingReviewRepository extends JpaRepository<RatingReview, Integer>{
	
	@Query("select rr from RatingReview rr where rr.book= :book")
	public List<RatingReview> findRatingReviewByBook(@Param("book") Book book);

}
