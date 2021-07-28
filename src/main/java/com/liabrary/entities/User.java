package com.liabrary.entities;

//import java.sql.Date;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@Entity
@Table(name="USER")
public class User {
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int user_id;
	
	@NotBlank(message="name can not be blank")
	@Size(min=3, max=20, message="name must contain 3-20 characters")
	private String user_name;
	
	@Column(unique=true)
	@Pattern(regexp="(^$|[0-9]{6})", message="")
	@Size(min=6, max=6, message="your roll number must be of 6 digits")
	private String roll_no;
	
	@NotBlank(message=" select valid department")
	private String department;
	
	@NotBlank(message=" select valid year")
	private String year;
	
	private String image;
	
	@Column(unique=true)
	@NotBlank(message="email can not be blank")
	@Pattern(regexp="^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$", message="")
	private String email;
	
	@NotBlank(message="password can not be blank")
	private String password;
	
	@Transient
	private String confirmpassword;
	
	@Column(unique=true)
	@NotBlank(message="mobile no can not be blank")
	@Size(min=10, max=10, message="put a valid mobile number")
	@Pattern(regexp="(^$|[0-9]{10})", message="")
	private String mob_no;
	
	private boolean profile_activity;
	
	@Temporal(TemporalType.DATE)
	private Date expiry_date;
	
	private String role;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="user")
	private List<BookStock> user_borrows=new ArrayList<>();
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getRoll_no() {
		return roll_no;
	}
	public void setRoll_no(String roll_no) {
		this.roll_no = roll_no;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	public String getConfirmpassword() {
		return confirmpassword;
	}
	public void setConfirmpassword(String confirmpassword) {
		this.confirmpassword = confirmpassword;
	}
	public String getMob_no() {
		return mob_no;
	}
	public void setMob_no(String mob_no) {
		this.mob_no = mob_no;
	}

	public boolean isProfile_activity() {
		return profile_activity;
	}
	public void setProfile_activity(boolean profile_activity) {
		this.profile_activity = profile_activity;
	}
	
	public Date getExpiry_date() {
		return expiry_date;
	}
	public void setExpiry_date(Date expiry_date) {
		this.expiry_date = expiry_date;
	}
	public List<BookStock> getUser_borrows() {
		return user_borrows;
	}
	public void setUser_borrows(List<BookStock> user_borrows) {
		this.user_borrows = user_borrows;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "User [user_id=" + user_id + ", user_name=" + user_name + ", roll_no=" + roll_no + ", department="
				+ department + ", year=" + year + ", image=" + image + ", email=" + email + ", password=" + password
				+ ", mob_no=" + mob_no + ", profile_activity=" + profile_activity + ", expiry_date=" + expiry_date
				+ ", role=" + role + ", user_borrows=" + user_borrows + "]";
	}
	
	
	
	

}
