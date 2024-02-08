package com.springboot.controller;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.springboot.config.SpringSecurityconfig;
import com.springboot.entity.Contact;
import com.springboot.entity.Userdetails;
import com.springboot.repositry.Contactrepositry;
import com.springboot.repositry.Userrepositry;
import com.springboot.service.Serviceinterface;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/setting")
public class SettingsController {

	@Autowired
	private Userrepositry ur;
	@Autowired
	private Serviceinterface s;
	@Autowired
	private Contactrepositry cr;
	String username;
	Userdetails u;

	@ModelAttribute
	public void getuserdetails(Principal p, Model m) {
		username = p.getName();
		System.out.println(username);

		u = ur.findByEmail(username);
		m.addAttribute("user", u);

	}

	@RequestMapping(value = "/change", method = {RequestMethod.GET, RequestMethod.POST})
	public String changepassword(@RequestParam String oldpassword, @RequestParam String newpassword,

			HttpSession session,Model m) {

		
		String storedpass = u.getPassword();
		System.out.println(storedpass);

		boolean matched = SpringSecurityconfig.passwordEncoder().matches(oldpassword, storedpass);
		System.out.println(matched);
		 PasswordData passwordData = new PasswordData(newpassword);
	        PasswordValidator validator = new PasswordValidator(Arrays.asList(
	            new LengthRule(8, Integer.MAX_VALUE),
	            new CharacterRule(EnglishCharacterData.UpperCase, 1),
	            new CharacterRule(EnglishCharacterData.LowerCase, 1),
	            new CharacterRule(EnglishCharacterData.Digit, 1),
	            new CharacterRule(EnglishCharacterData.Special, 1),
	            new WhitespaceRule()
	        ));
	        RuleResult result = validator.validate(passwordData);

		
		if(!matched)
		{
			   m.addAttribute("error", "Invalid Old Password");
			   m.addAttribute("oldpassword",oldpassword);
			   m.addAttribute("newpassword",newpassword);
			   return "settings";
		}
		else if(!result.isValid())
		{
			  java.util.List<String> messages = validator.getMessages(result);
	            m.addAttribute("criteriaMessages", messages);
	            m.addAttribute("oldpassword",oldpassword);
				   m.addAttribute("newpassword",newpassword);

	           
	            return "settings";
		}
		else 
			{
			  String encodenew = SpringSecurityconfig.passwordEncoder().encode(newpassword);
		        u.setPassword(encodenew);
		        ur.save(u);
		        
		        m.addAttribute("success", "Password Change success!");


		        return "settings";
				
			}
		
		
		
		
		
		
		
		
		
		
		
		
		

	}
	
	
	
	
	@GetMapping("/delete")
	public String deleteaccount()
	{
		List<Contact> findAllByUser = cr.findAllByUser(u);
		
		for (Contact contact : findAllByUser) {
			s.delete(u, contact);
		}
		s.deleteuser(u);
	
		return "redirect:/home";
	}
}