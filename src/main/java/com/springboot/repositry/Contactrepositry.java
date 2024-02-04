package com.springboot.repositry;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.entity.Contact;
import com.springboot.entity.Userdetails;

public interface Contactrepositry extends JpaRepository<Contact, Long> {
	
	   Contact findByEmail(String email);
	   
	   @Query("from Contact as c where c.user.id=:userid")
	   public Page<Contact> findContactsByUser(@Param("userid") Long userid,Pageable pageable);
	   
	   @Query("SELECT c FROM Contact c WHERE c.user = :user " +
	           "AND (LOWER(c.email) LIKE %:keyword% OR LOWER(c.phone) LIKE %:keyword% OR " +
	           "LOWER(c.firstname) LIKE %:keyword% OR LOWER(c.lastname) LIKE %:keyword% OR " +
	           "LOWER(concat(c.firstname, ' ', c.lastname)) LIKE %:keyword% OR " +
	           "LOWER(concat(c.lastname, ' ', c.firstname)) LIKE %:keyword%)")
	    Page<Contact> searchContacts(
	        @Param("user") Userdetails user,
	        @Param("keyword") String keyword,
	        Pageable pageable
	    );

	   @Transactional
	    void deleteAllByUser(Userdetails user);
	   
	   List<Contact> findAllByUser(Userdetails user);

}
