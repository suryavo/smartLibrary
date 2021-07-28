package com.liabrary.controller.admin;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.liabrary.dao.BookRepository;
import com.liabrary.dao.BookStockRepository;
import com.liabrary.dao.TransactionRecordRepository;
import com.liabrary.dao.UserRepository;
import com.liabrary.entities.Book;
import com.liabrary.entities.BookStock;
import com.liabrary.entities.TransactionRecord;
import com.liabrary.entities.User;
import com.liabrary.helper.AssignBook;
import com.liabrary.helper.Message;

@Controller
@RequestMapping("/admin")
public class AdminDashboard {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private BookStockRepository bookStockRepository;
	@Autowired
	private TransactionRecordRepository transactionRecordRepository;
	
// ------------------------Entry New Book-------------------------------------------------------------
//----------------------------------------------------------------------------------------------------
	
	@GetMapping("/entrybook")
	public String entrybook(Model model, Principal principal) {
		String userName=principal.getName();
		User user=userRepository.findByUsername(userName);
		model.addAttribute("title", user.getUser_name()+"-Entry Book");
		model.addAttribute("user", user);
		model.addAttribute("book", new Book());
		return "/admin/entrybook";
	}
	
	@PostMapping("/do_entry")
	public String entryBook(@Valid @ModelAttribute("book") Book book, BindingResult res1, @RequestParam("book-image") MultipartFile file,  Model model, HttpSession session, Principal principal) {
		
		String userName=principal.getName();
		User user=userRepository.findByUsername(userName);	
		model.addAttribute("title", user.getUser_name()+"-Entry Book");
		model.addAttribute("user", user);
		
		try {
				
			//errors
			if(res1.hasErrors()) {
				System.out.println(res1);
				model.addAttribute("book", book);
				return "/admin/entrybook";
			}
			
			//handling the image file
			if(file.isEmpty()) {
				//if the file is empty
				book.setImage("bookavatar.png");
				System.out.println("bookavatar.png");
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
				
				book.setImage(new_file_name);
				
				File savefile=new ClassPathResource("static/images").getFile();
			
				Path path=Paths.get(savefile.getAbsolutePath()+File.separator+new_file_name);
				
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

				
			}
			
			
			
			//storing the data
			Book result=this.bookRepository.save(book);
			List<BookStock> book_stock=new ArrayList<BookStock>();
			
			int book_count=result.getBook_count();
			
			for(int i=0;i<book_count;i++) {
				BookStock bookstock=new BookStock();
				bookstock.setBook(result);
				bookstock.setUser(user);
				BookStock result1=this.bookStockRepository.save(bookstock);
				book_stock.add(result1);
			}
			result.setBookstock(book_stock);
			Book final_result=this.bookRepository.save(result);

			
			model.addAttribute("book", new Book());
			session.setAttribute("message", new Message("Successfully entered", "alert-success"));
			return "/admin/entrybook";
			
		} catch (Exception e) {

			e.printStackTrace();
			model.addAttribute("book", book);
			session.setAttribute("message", new Message("Sorry! Something went wrong !! ", "alert-danger"));
			return "/admin/entrybook";
		}
		
	}
	
// ------------------------Admin Library-------------------------------------------------------------
//----------------------------------------------------------------------------------------------------
	
	@GetMapping("/adminlibrary/{page}")
	public String adminLibrary(Model model, @PathVariable("page") Integer page, Principal principal) {
		String userName=principal.getName();
		User user=userRepository.findByUsername(userName);
		model.addAttribute("title", user.getUser_name()+"-Admin Library");
		model.addAttribute("user", user);
		
		Pageable pagedata=PageRequest.of(page, 10);
		Page<Book> books=this.bookRepository.findAll(pagedata);
		if(books.isEmpty()) {
			model.addAttribute("currentpage", 0);
		}
		else{
			model.addAttribute("currentpage", page);
		}
		model.addAttribute("totalpages", books.getTotalPages());

		model.addAttribute("books", books);
		
		return "/admin/adminlibrary";
	}
	
	@GetMapping("/search-book/{query}")
	public String searchBook(@PathVariable("query") String query, Model model, Principal principal) {
		
		String userName=principal.getName();
		User user=userRepository.findByUsername(userName);
		model.addAttribute("title", user.getUser_name()+"-Admin Library");
		model.addAttribute("user", user);
		
		List<Book> books=this.bookRepository.findBookByName(query);
		
		
		model.addAttribute("books", books);
		model.addAttribute("query", query);
		model.addAttribute("currentpage", 0);
		model.addAttribute("totalpages", 0);
		
		return "/admin/adminlibrary";
	}
	
	@GetMapping("/show-book/{book_id}")
	public String getSingleBook(@PathVariable("book_id") int book_id, Model model, Principal principal) {
		
		String userName=principal.getName();
		User user=userRepository.findByUsername(userName);
		model.addAttribute("title", user.getUser_name()+"-Admin Library");
		model.addAttribute("user", user);
		
		Book book=this.bookRepository.findSingleBookById(book_id);
		model.addAttribute("book", book);
		System.out.println(book_id);
		
		return "/admin/showbook";
	}
	
	
	// ------------------------Update book details-------------------------------------------------------------
	//----------------------------------------------------------------------------------------------------
	int available_book_count;
	String existing_book_image;
	@PostMapping("/update-book/{book_id}")
	public String updateBook(@PathVariable("book_id") int book_id, Model model, Principal principal) {
		
		String userName=principal.getName();
		User user=userRepository.findByUsername(userName);
		model.addAttribute("title", user.getUser_name()+"-Admin Library");
		model.addAttribute("user", user);
		
		Book book=this.bookRepository.findSingleBookById(book_id);
		model.addAttribute("book", book);
		
		available_book_count=book.getBook_count();
		existing_book_image=book.getImage();
		System.out.println(existing_book_image);
		
		return "/admin/updatebook";
	}
	
	@PostMapping("/update-book/{book_id}/do-update")
	public String doUpdate(@Valid @ModelAttribute("book") Book book,  BindingResult res, @PathVariable("book_id") int book_id, Model model, @RequestParam("book-image") MultipartFile file,Principal principal, HttpSession session) {
		
		String userName=principal.getName();
		User user=userRepository.findByUsername(userName);
		model.addAttribute("title", user.getUser_name()+"-Admin Library");
		model.addAttribute("user", user);
		


		model.addAttribute("book", book);
		
		
		try {
				
			
			if(res.hasErrors()) {
				System.out.println(res);
				model.addAttribute("book", book);
				return "/admin/updatebook";
			}
			
			if(file.isEmpty()) {
				//if the file is empty do nothing
				book.setImage(existing_book_image);
				System.out.println(existing_book_image);
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
				
				book.setImage(new_file_name);
				
				File savefile=new ClassPathResource("static/images").getFile();
			
				Path path=Paths.get(savefile.getAbsolutePath()+File.separator+new_file_name);
				
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

				
			}
			
			
			//update and store books in repository
			int to_add=book.getBook_count();
			int total_book_count=to_add+available_book_count;
			book.setBook_id(book_id);
			book.setBook_count(total_book_count);
			Book result=this.bookRepository.save(book);
			List<BookStock> book_stock=result.getBookstock();

			for(int i=0;i<to_add;i++) {
				BookStock bookstock=new BookStock();
				bookstock.setBook(result);
				bookstock.setUser(user);
				BookStock result1=this.bookStockRepository.save(bookstock);
				book_stock.add(result1);
			}
			result.setBookstock(book_stock);
			Book final_result=this.bookRepository.save(result);

			
			model.addAttribute("book", book);
			session.setAttribute("message", new Message("Successfully Updated", "alert-success"));
			return "/admin/updatebook";
			
		} catch (Exception e) {

			e.printStackTrace();
			model.addAttribute("book", book);
			session.setAttribute("message", new Message("Sorry! Something went wrong !! ", "alert-error"));
			return "/admin/updatebook";
		}
		
		
		
	}
	
	// ------------------------Assign Book to user-------------------------------------------------------------
	//----------------------------------------------------------------------------------------------------
	@GetMapping("/assign-book")
	public String assignBook(@ModelAttribute("assignbook") AssignBook assignbook, Model model, Principal principal) {
		
		String userName=principal.getName();
		User user=userRepository.findByUsername(userName);
		model.addAttribute("title", user.getUser_name()+"-Admin Library");
		model.addAttribute("user", user);
		

		return "/admin/assignbook";
		
	}
	@PostMapping("/assign-book/do_assign")
	public String doAssignBook(@ModelAttribute("assignbook") AssignBook assignbook, Model model, Principal principal, HttpSession session) {
		
		String userName=principal.getName();
		User user=userRepository.findByUsername(userName);
		model.addAttribute("title", user.getUser_name()+"-Admin Library");
		model.addAttribute("user", user);
		
		try {
			
			
			int unique_book_id=assignbook.getUnique_book_id();
			BookStock bookstock=bookStockRepository.getById(unique_book_id);
			int user_id=assignbook.getUser_id();
			User userAssigned=userRepository.getById(user_id);
			
			User currentuser=bookstock.getUser();
			if(currentuser!=user) {
				session.setAttribute("message", new Message("Sorry! The book is already assigned to another user !! ", "alert-danger"));
				return "/admin/assignbook";
			}
			
			bookstock.setUser(userAssigned);
			BookStock result=this.bookStockRepository.save(bookstock);
			System.out.println(bookstock.getUnique_book_id());
			System.out.println(userAssigned);
			
			//add record to transactionRecord
			TransactionRecord transactionRecord=new TransactionRecord();
			transactionRecord.setCurrent_user(userAssigned);
			transactionRecord.setFrom_user(user);
			transactionRecord.setUnique_book_id(unique_book_id);
			transactionRecord.setReturned(false);
			
			Date today = new Date();
			Calendar cal=Calendar.getInstance();
			today=cal.getTime();
			transactionRecord.setIssue_date(today);
			
			cal.add(Calendar.MONTH,2);
			Date expected_return_date=cal.getTime();
			transactionRecord.setExpected_return_date(expected_return_date);
			transactionRecord.setFine(0);
			
			TransactionRecord tr=this.transactionRecordRepository.save(transactionRecord);
			
			//add record to transactionRecord
			
			session.setAttribute("message", new Message("successfully assigned", "alert-success"));
			
			return "/admin/assignbook";
			
		}catch(Exception e) {
			e.printStackTrace();
			session.setAttribute("message", new Message("Sorry! Something went wrong !! ", "alert-danger"));
			return "/admin/assignbook";
		}
		
		
	}
	
}
