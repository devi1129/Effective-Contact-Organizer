package com.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springboot.entity.Userdetails;
import com.springboot.exception.Emailalreadyexistsexception;
import com.springboot.repositry.Userrepositry;
import com.springboot.service.Serviceinterface;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class UserController {

	@Autowired
	private Serviceinterface s;
	@Autowired
	private Userrepositry ur;

	@GetMapping("/home")
	public String home() {
		return "home";
	}
	@GetMapping("/about")
	public String about(Model m)

	{
		
	  
	
		return "about";
	}

	@GetMapping("/signin")
	public String login(Model m) {

		m.addAttribute("title", "Login Page");
	
		
		return "login";
	}
	
	
	

	@GetMapping("/signup")
	public String signup(Model m) {
		Userdetails u = new Userdetails();
		m.addAttribute("user", u);
		return "signup";
	}

	@PostMapping("/saveregister")
	public String saveregister(@Valid @ModelAttribute("user") Userdetails user, BindingResult result, Model m,
			HttpSession session) {

		if (result.hasErrors()) {
			System.out.println("error");
			System.out.println(result.getAllErrors());
			m.addAttribute("user", user);
			return "signup";
		}
System.out.println("yayyy!! win");
		try {
			s.saveregister(user);
			session.setAttribute("message", "Registration Successful! Please Login");
			return "redirect:/signin";
		} catch (Emailalreadyexistsexception e) {
			result.rejectValue("email", "error.user", e.getMessage());
			m.addAttribute("user", user);
			return "signup";
		}
	}

}

//
//@GetMapping("/login-error")
//@RequestMapping("/login")
//public String login(HttpServletRequest request, Model model) {
//    HttpSession session = request.getSession(false);
//    String errorMessage = null;
//    if (session != null) {
//        AuthenticationException ex = (AuthenticationException) session
//                .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
//        if (ex != null) {
//            errorMessage = ex.getMessage();
//        }
//    }
//    System.out.println("--->" + errorMessage);
//    model.addAttribute("errorMessage", errorMessage);
//    return "login";
//}
//

//@PostMapping("/login")
//public String login(@ModelAttribute("user") Userdetails user, HttpServletRequest request) {
//    try {
//        // Attempt to authenticate user
//        request.login(user.getEmail(), user.getPassword());
//        return "redirect:/users";
//    } catch (ServletException e) {
//        // Authentication failed
//        e.printStackTrace();
//        return "login";
//    }
//}
