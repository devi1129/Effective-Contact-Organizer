package com.springboot.controller;

import java.util.Arrays;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springboot.config.SpringSecurityconfig;
import com.springboot.emailutils.EmailDetails;
import com.springboot.emailutils.EmailService;
import com.springboot.entity.Userdetails;
import com.springboot.repositry.Userrepositry;
import com.springboot.service.Serviceinterface;
import com.springboot.service.impl.Serviceimpl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
@Controller
public class MessageController {
    @Autowired
    private EmailService emailService;
    @Autowired
    private Serviceinterface simpl;
    
    @Autowired
	private Userrepositry ur;
    
    String email;


    @GetMapping("/forgot")
    public String forgot(@RequestParam("username") String username, Model m) {
        Userdetails user = ur.findByEmail(username);
        

        if (username.isEmpty()) {
            m.addAttribute("emailempty", "Please Enter Email!!");
         
        } else if (user == null) {
            m.addAttribute("emailnotexist", "Account does not exist.");
         
        
        } else {
            String existuserEmail = user.getEmail();
            email=existuserEmail;
            m.addAttribute("resetmail", "An email containing password reset link has been sent to" + " " + existuserEmail);
           simpl.sendPasswordResetEmail(existuserEmail);
        
        }
        return "login";
    }
    
    @GetMapping("/reset-password")
    public String resetpassword(@RequestParam("token") String token,Model model,HttpSession session)
    {
    	System.out.println(token);
    	Userdetails user=ur.findByResettoken(token);
    	
    	
    	model.addAttribute("user",user);
    	   session.setAttribute("resetUser", user);
    	return "resetpassword";
    }
    @RequestMapping(value = "/changepassword", method = {RequestMethod.GET, RequestMethod.POST})
	public String changepassword(@RequestParam String newpassword, @RequestParam String confirmpassword,

			HttpSession session,Model m,RedirectAttributes redirectAttributes) {
    	
    	Userdetails user = (Userdetails) session.getAttribute("resetUser");
 	
    	
    
    	
    	
    	
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
   
    	
    	String cleannew=newpassword.trim();
    	String cleanconfirm=confirmpassword.trim();
    	String token=user.getResettoken();
    	
    	if(cleannew.equals(cleanconfirm))
    	{
    		if(result.isValid())
    		{
    			String encodenew = SpringSecurityconfig.passwordEncoder().encode(newpassword);
		        user.setPassword(encodenew);
		        user.setResettoken(null);
		        ur.save(user);
		    
		        
		        redirectAttributes.addFlashAttribute("success", "Password Reset Success");
		      
    		}
    		else
    		{
    			 java.util.List<String> messages = validator.getMessages(result);
    			 redirectAttributes.addFlashAttribute("criteriaMessages", messages);
    			 redirectAttributes.addFlashAttribute("newpassword",newpassword);
    			 redirectAttributes.addFlashAttribute("confirmpassword",confirmpassword);
 	        
 	           return "redirect:/reset-password?token=" + token;

    		}
    	}
    	else {
    		  redirectAttributes.addFlashAttribute("error", "Password does not match");
    		  redirectAttributes.addFlashAttribute("newpassword",newpassword);
 			 redirectAttributes.addFlashAttribute("confirmpassword",confirmpassword);
    		
    		 return "redirect:/reset-password?token=" + token;

		}
   	 return "redirect:/signin";
		
		

	}
	
    
    
 
	
}

