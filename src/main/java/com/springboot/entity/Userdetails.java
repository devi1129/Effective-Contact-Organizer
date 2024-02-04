package com.springboot.entity;

import java.util.List;

import com.springboot.javautils.ValidPassword;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Userdetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotEmpty(message = "Name should not be empty")
	private String name;
	@NotEmpty(message = "Email should not be empty")
	@Email(message="Enter valid email")
	private String email;
	
   
    @Size(min = 8, message = "Password must be more than 8 characters in length.")
    @ValidPassword
    private String password;

	private String role;
	
	 @Digits(integer = 10, fraction = 0, message = "Invalid phone number")
    @Size(min = 10, max = 10, message = "Phone number should be 10 digits")  
	private String phone;
	private boolean enabled;
	private String imageurl;
	
private String resettoken;
	private String about;
	
	@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE},fetch = FetchType.EAGER)
	private List<Contact> contacts;

}
