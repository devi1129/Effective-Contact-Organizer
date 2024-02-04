package com.springboot.entity;

import java.util.List;

import org.hibernate.annotations.ManyToAny;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
public class Contact {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cid;
	
	@NotEmpty(message = "Name should not be empty")
	private String firstname;
	
	private String lastname;
	private String work;
	
	@NotEmpty(message = "Email should not be empty")
	@Email(message="Enter Valid Email")
	private String email;
	@NotEmpty(message = "Phone number is required")
	    @Size(min = 10, max = 10, message = "Phone number should be 10 digits")
	    @Digits(integer = 10, fraction = 0, message = "Invalid phone number")
	private String phone;
	
	private String image;
	private String gender;
	
	@Column(length=50000)
	private String description;
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	private Userdetails user;
	

}
