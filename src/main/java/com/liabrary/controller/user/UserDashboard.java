package com.liabrary.controller.user;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.PriorityQueue;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.liabrary.dao.BookRepository;
import com.liabrary.dao.BookStockRepository;
import com.liabrary.dao.OtpVerificationRepository;
import com.liabrary.dao.RatingReviewRepository;
import com.liabrary.dao.TransactionRecordRepository;
import com.liabrary.dao.UserRepository;
import com.liabrary.entities.Book;
import com.liabrary.entities.BookStock;
import com.liabrary.entities.RatingReview;
import com.liabrary.entities.TransactionRecord;
import com.liabrary.entities.User;
import com.liabrary.helper.Message;
import com.liabrary.helper.OtpVerification;
import com.liabrary.services.OtpDeletionService;

@Controller
@RequestMapping("/user")
public class UserDashboard {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private BookStockRepository bookStockRepository;
	@Autowired
	private OtpVerificationRepository otpVerificationRepository;
	@Autowired
	private TransactionRecordRepository transactionRecordRepository;
	@Autowired
	private RatingReviewRepository ratingReviewRepository;
	@Autowired
	private OtpDeletionService otpDeletionService;
	
	
	
	@RequestMapping("/dashboard")
	public String dashboard(Model model, Principal principal) {
		String userName=principal.getName();
		User user=userRepository.findByUsername(userName);
		model.addAttribute("title", user.getUser_name()+"-Smart Library");
		model.addAttribute("user", user);
		
		
		List<Book> booksByDomain=this.bookRepository.findBookByDomain(user.getDepartment());
		PriorityQueue<BookSuggestion> bs=new PriorityQueue<BookSuggestion>(5, (b,a) -> a.getRating() - b.getRating());
		for(Book b : booksByDomain) {
			List<RatingReview> ratingReviewList=this.ratingReviewRepository.findRatingReviewByBook(b);
			int count=0;
			int totalrating=0;
			for(RatingReview rr:ratingReviewList) {
				count++;
				totalrating+=rr.getRating();
				
				int val=rr.getRating();
				val=val*100/5;
				val=Math.round(val/10);
				val=val*10;
				
				rr.setRating(val);

			}
			int avgrating=0;
			if(count>0) avgrating=totalrating/count;
			
			
			
			double averageRating=(double)avgrating;
			Formatter formatter = new Formatter();
	        formatter.format("%.1f", averageRating);
	        String average=formatter.toString();
	        
			int percentageRating=Math.round(((int)averageRating*100/5)/10)*10;
			
			
			
			bs.add(new BookSuggestion(b, avgrating, percentageRating, average, count));
		}
		
		int min=4;
		min=Math.min(min, bs.size());
		
		List<BookSuggestion> topfive=new ArrayList<>();
		for(int i=0;i<min;i++) {
			topfive.add(bs.poll());
		}
		

		if(topfive.size()==0) {
			model.addAttribute("topfive", "0");
		}
		else {
			model.addAttribute("topfive", topfive);
		}
		
		System.out.println(topfive);
		
		model.addAttribute("pageid", 1);
		
		return "/user/home";
	}
	
	//------------------------------------Update Profile------------------------------------------
	//---------------------------------------------------------------------------------------
	
	String existing_profile_image;
	@GetMapping("/update-profile")
	public String updateProfile(Model model, Principal principal) {
		
		String userName=principal.getName();
		User user=userRepository.findByUsername(userName);
		model.addAttribute("title", user.getUser_name()+"-Admin Library");
		model.addAttribute("user", user);
		
		existing_profile_image=user.getImage();
		model.addAttribute("pageid", 7);

		return "/user/updateprofile";
	}
	
	@PostMapping("/update-profile/do_update")
	public String doUpdateProfile(@Valid @ModelAttribute("newuser") User newuser,  BindingResult res, Model model, @RequestParam("profile-image") MultipartFile file, Principal principal, HttpSession session) {
		
		String userName=principal.getName();
		User user=userRepository.findByUsername(userName);
		model.addAttribute("title", user.getUser_name()+"-Admin Library");
		model.addAttribute("user", user);
		model.addAttribute("pageid", 7);
		//---------------------------------
		int userId=user.getUser_id();
		String roll_no=user.getRoll_no();
		String role=user.getRole();
		String password=user.getPassword();
		String department=user.getDepartment();
		String year=user.getYear();
		Date expiry=user.getExpiry_date();
		List<BookStock> user_borrows=user.getUser_borrows();

		
		try {
				
			
			if(res.hasErrors()) {
				System.out.println(res);
				model.addAttribute("newuser", newuser);
				return "/user/updateprofile";
			}
			
			if(file.isEmpty()) {
				//if the file is empty do nothing
				newuser.setImage(existing_profile_image);
				System.out.println(existing_profile_image);
			}
			else {
				//if the file is not empty
				String original_file_name=file.getOriginalFilename();
				Date date= new Date();
				long time = date.getTime(); 
				Timestamp ts = new Timestamp(time);
				DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy-HH-mm");
			    String t = dateFormat.format(ts);
				String tt=t.toString();
				String new_file_name=tt+original_file_name;
				
				System.out.println(new_file_name);
				
				newuser.setImage(new_file_name);
				
				File savefile=new ClassPathResource("static/images").getFile();
			
				Path path=Paths.get(savefile.getAbsolutePath()+File.separator+new_file_name);
				
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

				
			}
			newuser.setUser_id(userId);
			newuser.setRoll_no(roll_no);
			newuser.setRole(role);
			newuser.setDepartment(department);
			newuser.setExpiry_date(expiry);
			newuser.setPassword(password);
			newuser.setProfile_activity(true);
			newuser.setYear(year);
			newuser.setUser_borrows(user_borrows);
			
			User updateresult=this.userRepository.save(newuser);
			


			
			model.addAttribute("newuser", newuser);
			session.setAttribute("message", new Message("Successfully Updated", "alert-success"));
			return "/user/updateprofile";
			
		} catch (Exception e) {

			e.printStackTrace();
			model.addAttribute("newuser", newuser);
			session.setAttribute("message", new Message("Sorry! Something went wrong !! ", "alert-error"));
			return "/user/updateprofile";
		}
		
		
		
	}
	
	
	
	//------------------------------------library------------------------------------------
	//---------------------------------------------------------------------------------------
	@GetMapping("/userlibrary/{page}")
	public String showLibrary(Model model, @PathVariable("page") Integer page, Principal principal) {
		
		String userName=principal.getName();
		User user=userRepository.findByUsername(userName);
		model.addAttribute("title", user.getUser_name()+"-Library");
		model.addAttribute("user", user);
		
		Pageable pagedata=PageRequest.of(page, 10);
		Page<Book> books=this.bookRepository.findAll(pagedata);
		model.addAttribute("currentpage", page);
		model.addAttribute("totalpages", books.getTotalPages());

		model.addAttribute("books", books);
		
		model.addAttribute("pageid", 2);
		
		return "/user/userlibrary";
	}
	
	@GetMapping("/search-book/{query}")
	public String searchBook(@PathVariable("query") String query, Model model, Principal principal) {
		
		String userName=principal.getName();
		User user=userRepository.findByUsername(userName);
		model.addAttribute("title", user.getUser_name()+"-Library");
		model.addAttribute("user", user);
		
		List<Book> books=this.bookRepository.findBookByName(query);
		
		
		model.addAttribute("books", books);
		model.addAttribute("query", query);
		model.addAttribute("currentpage", 0);
		model.addAttribute("totalpages", 0);
		
		model.addAttribute("pageid", 2);
		
		return "/user/userlibrary";
	}
	
	@GetMapping("/show-book/{book_id}")
	public String getSingleBook(@PathVariable("book_id") int book_id, Model model, Principal principal) {
		
		String userName=principal.getName();
		User user=userRepository.findByUsername(userName);
		model.addAttribute("title", user.getUser_name()+"-User Library");
		model.addAttribute("user", user);
		
		Book book=this.bookRepository.findSingleBookById(book_id);
		model.addAttribute("book", book);
		System.out.println(book_id);
		
		List<BookStock> bookstock=this.bookStockRepository.findBookStockHavingTheBook(book);
		List<BookStock> newbookstock=new ArrayList<BookStock>();
		int book_count_in_library=0;
		for(BookStock bs:bookstock) {
			if(bs.getUser().getRole().equals("ROLE_ADMIN")) {
				book_count_in_library+=1;
				if(book_count_in_library<=1) {
					newbookstock.add(bs);
				}
			}
			else {
				newbookstock.add(bs);
			}
			
		}
		
		List<RatingReview> ratingReviewList=this.ratingReviewRepository.findRatingReviewByBook(book);
		List<Integer> distinctRating= new ArrayList<Integer>();
		int count=1;
		int totalrating=5;
		for(RatingReview rr:ratingReviewList) {
			count++;
			totalrating+=rr.getRating();
			
			int val=rr.getRating();
			val=val*100/5;
			val=Math.round(val/10);
			val=val*10;
			
			rr.setRating(val);

		}
		double averageRating=(double)totalrating/count;
		Formatter formatter = new Formatter();
        formatter.format("%.1f", averageRating);
        String average=formatter.toString();
        
		int percentageRating=Math.round(((int)averageRating*100/5)/10)*10;

		
		model.addAttribute("averageRating", average);
		model.addAttribute("percentageRating", percentageRating);
		model.addAttribute("ratingReviewList", ratingReviewList);
		model.addAttribute("distinctRating", distinctRating);
		model.addAttribute("book_count_in_library", book_count_in_library);
		model.addAttribute("newbookstock", newbookstock);
		model.addAttribute("count", count);
		
		model.addAttribute("pageid", 2);
		
		return "/user/showbook";
	}
	
	
	//------------------------------------Mybooks------------------------------------------
	//---------------------------------------------------------------------------------------
	@GetMapping("/mybooks")
	public String mybooks(Model model, Principal principal) {
		
		String userName=principal.getName();
		User user=userRepository.findByUsername(userName);
		model.addAttribute("title", user.getUser_name()+"-Library");
		model.addAttribute("user", user);
		
		List<BookStock> bookStock=this.bookStockRepository.findMyBooks(user);

		model.addAttribute("bookStock", bookStock);
		model.addAttribute("pageid", 4);

		return "/user/mybooks";
	}
	
	@PostMapping("/mybooks/release-book/{book_id}")
	public String releaseTo(@RequestParam("release-to-user") int release_to_user, @PathVariable ("book_id") int book_id, Model model, Principal principal) {
		
		String userName=principal.getName();
		User user=userRepository.findByUsername(userName);
		model.addAttribute("title", user.getUser_name()+"-Library");
		model.addAttribute("user", user);
		
		
		//check if already the otp exist for the same book and same users
		//and if present then delete the existing record
		String find_otp=this.otpVerificationRepository.findOtpBySenderAndReceiverAndBook(release_to_user, user.getUser_id(), book_id);
		if(find_otp!=null) {
			otpDeletionService.deleteotpnow(find_otp);
		}
		
		//otp verification process
		//create otp
		OtpVerification otpVerification=new OtpVerification();
		otpVerification.setBook_id(book_id);
		otpVerification.setSender_user_id(user.getUser_id());
		otpVerification.setReceiver_user_id(release_to_user);
		
		//generate random otp
		final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++)
        {
            int randomIndex = random.nextInt(chars.length());
            sb.append(chars.charAt(randomIndex));
        }
        String otp=sb.toString();
        //generate random otp
        
        model.addAttribute("otpVerification", otpVerification);
        
        otpVerification.setOtp(otp);
        OtpVerification result=this.otpVerificationRepository.save(otpVerification);// save the data with otp in otpverification database

        model.addAttribute("pageid", 4);
        
        otpDeletionService.deleteotp(otp);

		return "/user/enterotp";
	}
	
	

	
	
	@PostMapping("/mybooks/compete-transaction/{book-id}/{release-to-user}")
	public String completeTransaction(@RequestParam("otp-input") String otp_input, @PathVariable ("book-id") int book_id, @PathVariable ("release-to-user") int release_to_user, Model model, Principal principal) {
		
		String userName=principal.getName();
		User user=userRepository.findByUsername(userName);
		model.addAttribute("title", user.getUser_name()+"-Library");
		model.addAttribute("user", user);
		
		
		
		BookStock exchange_book=this.bookStockRepository.getById(book_id);
		User receiving_user=this.userRepository.getById(release_to_user);
		
		String find_otp=this.otpVerificationRepository.findOtpBySenderAndReceiverAndBook(release_to_user, user.getUser_id(), book_id);
		
		if(find_otp==null) {
			
			return "/user/home";
		}
		
		
		if(otp_input.equals(find_otp)) {
			//Do the update operation
			exchange_book.setUser(receiving_user);
			BookStock result=this.bookStockRepository.save(exchange_book);
			System.out.println("Successfully exchanged");
			System.out.println(result.getUser());
			
	//-----------------------update old transaction record-------------------------------------------------
	//-----------------------------------------------------------------------------------------------
			TransactionRecord old_transactionRecord=this.transactionRecordRepository.findTransactionRecordByCurrentUserAndBookAndStatus(user, book_id, false);
			old_transactionRecord.setTo_user(receiving_user);
			old_transactionRecord.setReturned(true);
			
			Date today = new Date();
			Calendar cal=Calendar.getInstance();
			today=cal.getTime();
			
			old_transactionRecord.setReturn_date(today);
			
			Date old_expected_return_date=old_transactionRecord.getExpected_return_date();
			if(today.after(old_expected_return_date)) {
				int fine=old_transactionRecord.getFine();
				fine+=50;
				old_transactionRecord.setFine(fine);
			}
			TransactionRecord tr1=this.transactionRecordRepository.save(old_transactionRecord);
			
	//-----------------------update old transaction record-------------------------------------------------
	//-----------------------------------------------------------------------------------------------
			
			TransactionRecord new_transactionRecord=new TransactionRecord();
			new_transactionRecord.setCurrent_user(receiving_user);
			new_transactionRecord.setFrom_user(user);
			new_transactionRecord.setUnique_book_id(book_id);
			new_transactionRecord.setReturned(false);
			
			new_transactionRecord.setIssue_date(today);
			
			cal.add(Calendar.MONTH,2);
			Date expected_return_date=cal.getTime();
			new_transactionRecord.setExpected_return_date(expected_return_date);
			new_transactionRecord.setFine(0);
			
			TransactionRecord tr2=this.transactionRecordRepository.save(new_transactionRecord);
			
			
			
		}
		else {
			System.out.println("transaction failed");
			OtpVerification otpVerification=this.otpVerificationRepository.findOtpVerificationByOtp(find_otp);
			model.addAttribute("otpVerification", otpVerification);
			model.addAttribute("book_id", book_id);
			return "/user/enterotp";
		}
		
		
		model.addAttribute("pageid", 4);

		return "/user/home";
	}
	
	@GetMapping("/show-otp")
	public String showOtp(Model model, Principal principal) {
		
		
		String userName=principal.getName();
		User user=userRepository.findByUsername(userName);
		model.addAttribute("title", user.getUser_name()+"-Library");
		model.addAttribute("user", user);
		
        //for the receiver end OTP will be shown only on receiver profile
        List<OtpVerification> receiver_otp_details=this.otpVerificationRepository.findOtpByReceiver(user.getUser_id());
        model.addAttribute("receiver_otp_details", receiver_otp_details);
        model.addAttribute("pageid", 5);
		return "/user/showotp";
	}
	
	@GetMapping("/mybooks/released-books")
	public String releasedBooks(Model model, Principal principal) {
		
		String userName=principal.getName();
		User user=userRepository.findByUsername(userName);
		model.addAttribute("title", user.getUser_name()+"-Library");
		model.addAttribute("user", user);
		
		List<TransactionRecord> transactionRecord=this.transactionRecordRepository.findTransactionRecordByCurrentUserAndStatus(user, true);
		List<BookStock> releasedBookStock=new ArrayList<BookStock>();
		for(TransactionRecord tr: transactionRecord) {
			BookStock bs=this.bookStockRepository.getById(tr.getUnique_book_id());
			releasedBookStock.add(bs);
		}
		model.addAttribute("releasedBookStock", releasedBookStock);
		model.addAttribute("pageid", 4);
		
		return "/user/myreleasedbooks";
	}
	
	@PostMapping("/mybooks/add-review/{book-id}")
	public String postReview(@RequestParam("review") String review, @RequestParam("rating") String rating ,@PathVariable("book-id") int book_id, Model model, Principal principal) {
		
		String userName=principal.getName();
		User user=userRepository.findByUsername(userName);
		model.addAttribute("title", user.getUser_name()+"-Library");
		model.addAttribute("user", user);
		
		List<TransactionRecord> transactionRecord=this.transactionRecordRepository.findTransactionRecordByCurrentUserAndStatus(user, true);
		List<BookStock> releasedBookStock=new ArrayList<BookStock>();
		for(TransactionRecord tr: transactionRecord) {
			BookStock bs=this.bookStockRepository.getById(tr.getUnique_book_id());
			releasedBookStock.add(bs);
		}
		
		
		Book book=this.bookStockRepository.findBookByUnique_Book_Id(book_id);
		RatingReview rr=new RatingReview();
		rr.setBook(book);
		rr.setUser(user);
		int i=Integer.parseInt(rating);
		rr.setRating(i);
		rr.setReview(review);
		
		RatingReview rrResult=this.ratingReviewRepository.save(rr);
		
		
		model.addAttribute("releasedBookStock", releasedBookStock);
		model.addAttribute("pageid", 4);
		
		return "/user/myreleasedbooks";
	}
	

}


class BookSuggestion{
	Book book;
	int rating;
	int percentagerating;
	String average;
	int count;
	
	public BookSuggestion() {
		super();
		// TODO Auto-generated constructor stub
	}

	

	public BookSuggestion(Book book, int rating, int percentagerating, String average, int count) {
		super();
		this.book = book;
		this.rating = rating;
		this.percentagerating = percentagerating;
		this.average = average;
		this.count=count;
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

	public int getPercentagerating() {
		return percentagerating;
	}

	public void setPercentagerating(int percentagerating) {
		this.percentagerating = percentagerating;
	}



	public String getAverage() {
		return average;
	}



	public void setAverage(String average) {
		this.average = average;
	}



	public int getCount() {
		return count;
	}



	public void setCount(int count) {
		this.count = count;
	}
	


	
	
}
