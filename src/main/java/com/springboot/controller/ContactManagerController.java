package com.springboot.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.entity.Contact;
import com.springboot.entity.Userdetails;
import com.springboot.exception.Emailalreadyexistsexception;
import com.springboot.repositry.Contactrepositry;
import com.springboot.repositry.Userrepositry;
import com.springboot.service.Serviceinterface;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/users")
public class ContactManagerController {
	@Autowired
	private Userrepositry ur;
	@Autowired
	private Serviceinterface s;
	@Autowired
	private Contactrepositry cr;
	String username;
	Userdetails u;
	@ModelAttribute
	public void getuserdetails(Principal p,Model m)
	{
		 username=p.getName();
		System.out.println(username);
		
		 u=ur.findByEmail(username);
		m.addAttribute("user",u);
	   
	}
	
	@GetMapping("/index")
	public String users(Model m)

	{
		
	  
	
		return "users";
	}
	
	
	
	@GetMapping("/profile")
	public String userprofile(Model m)

	{
		
		
		
		m.addAttribute("user",u);
		
		return "userprofile";
	}
	
	@PostMapping("/update/profile")
	public String updateprofile(@Valid @ModelAttribute("user") Userdetails user, BindingResult result, Model m,
			HttpSession session,Principal p, @RequestParam("userprofile") MultipartFile file)
	{
		
		if (result.hasErrors()) {
			m.addAttribute("user", u);
			return "userprofile";
		}

		try {
			s.saveuserprofile(u,file);
			return "userprofile";
		
			//return "redirect:/users/profile";
		
		} catch (Emailalreadyexistsexception e) {
			result.rejectValue("email", "error.user", e.getMessage());
			m.addAttribute("user", u);
			return "userprofile";
		}
	}
		
	
	@GetMapping("/add-contact")
	public String addcontact(Model m)

	{
		
		Contact c=new Contact();
		
		m.addAttribute("contact",c);
		
		return "Addcontact";
	}
	
	@PostMapping("/savecontact")
	public String saveregister(@Valid @ModelAttribute("contact") Contact contact, BindingResult result, Model m,
			HttpSession session,Principal p, @RequestParam("profileimage") MultipartFile file) {
		
		
		   username=p.getName();
			System.out.println(username);
			
			 u=ur.findByEmail(username);

		if (result.hasErrors()) {
			m.addAttribute("contact", contact);
			return "Addcontact";
		}

		try {
			s.savecontact(contact,u,file);
			session.setAttribute("contactmessage", "Add Contact successful!!");
			return "redirect:/users/add-contact";
		} catch (Emailalreadyexistsexception e) {
			result.rejectValue("email", "error.user", e.getMessage());
			m.addAttribute("contact", contact);
			return "Addcontact";
		}
	}
	@GetMapping("/view-contacts/{currentpage}")
	public String viewcontacts(@PathVariable("currentpage") Integer currentpage,Model m,Principal p)

	{
		System.out.println(u.getName());
		//current page
		//contact per page
		Pageable pageable = PageRequest.of(currentpage, 5,Sort.by("firstname"));
		
		Page<Contact> li=cr.findContactsByUser(u.getId(),pageable);
			 
		m.addAttribute("contacts",li);
		m.addAttribute("CurrentPage",currentpage);
		m.addAttribute("totalPages",li.getTotalPages());
		return "viewcontacts";
	}
	
	@GetMapping("/view-contact/{cid}")
	public String showcontact(@PathVariable Long cid,Model m)
	{
		
		Contact contactdetails = cr.findById(cid).get();
		
		if(u.getId()==contactdetails.getUser().getId())
		m.addAttribute("contactdetails",contactdetails);
		
		
		return "show-contact";
	}
	
	@GetMapping("/delete-contact/{cid}/{currentpage}")
	public String deleteContact(@PathVariable Long cid, @PathVariable("currentpage") Integer currentpage, Principal principal, HttpSession session) {
	    String username = principal.getName();
	    Userdetails user = ur.findByEmail(username);

	    Optional<Contact> optionalContact = cr.findById(cid);

	    if (optionalContact.isPresent()) {
	        Contact contact = optionalContact.get();
	        
	        
	        
	        if (contact.getUser().getId().equals(user.getId())) {
	        s.delete(user,contact);

	      

	            // Refresh the user in the session (if necessary)
	            session.setAttribute("user", ur.findByEmail(username));

	            return "redirect:/users/view-contacts/" + currentpage;
	        }
	    }

	    // Handle the case where the contact does not exist or does not belong to the user
	    return "redirect:/users/view-contacts/" + currentpage;
	}

	@GetMapping("/edit-contact/{cid}/{currentpage}")
	public String editcontact(@PathVariable Long cid,Model m,@PathVariable("currentpage") Integer currentpage)
	{
		
		Contact contact = cr.findById(cid).get();
		
		if(u.getId()==contact.getUser().getId())
		m.addAttribute("contact",contact);
		m.addAttribute("currentpage",currentpage);
		m.addAttribute("cid",cid);
		return "edit-contact";
	}
	@PostMapping("/updatecontact/{currentpage}/{cid}")
	public String updatecontact(@Valid @ModelAttribute("contact") Contact contact, BindingResult result, Model m,@PathVariable Long cid,
			HttpSession session,Principal p, @RequestParam("profileimage") MultipartFile file,@PathVariable("currentpage") Integer currentpage) {
		
		
		   username=p.getName();
			System.out.println(username);
			
			 u=ur.findByEmail(username);

		if (result.hasErrors()) {
			m.addAttribute("contact", contact);
			return "edit-contact";
		}

		try {
			s.updatecontact(contact,u,file,cid);
			return "redirect:/users/view-contacts/"+currentpage;
//			return "redirect:/users/edit-contact/" + cid+"/"+currentpage;
		} catch (Emailalreadyexistsexception e) {
			result.rejectValue("email", "error.user", e.getMessage());
			m.addAttribute("contact", contact);
			return "edit-contact";
		}
	}
	
    @GetMapping("/settings")
	public String settings()
	{
    	
    	return "settings";
	}
}
