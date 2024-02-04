package com.springboot.emailutils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.springboot.repositry.Userrepositry;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class Emailserviceimpl implements EmailService {
	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired
	private Userrepositry ur;

	@Value("${spring.mail.username}")
	private String sender;
	 @Autowired
	    private TemplateEngine thymeleafTemplateEngine;

	@Override
	public String sendPasswordResetEmail(String recipient, String resetLink) {
	    // Creating a mime message
	    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
	    MimeMessageHelper mimeMessageHelper;

	    try {
	        // Setting multipart as true for attachments to be sent
	        mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
	        mimeMessageHelper.setFrom(sender);
	        mimeMessageHelper.setTo(recipient);
	        mimeMessageHelper.setSubject("Password Reset Mail from Smart Contact Manager");

	        // Read the Thymeleaf HTML template
//	        ClassPathResource resource = new ClassPathResource("templates/email.html");
//	        String htmlContent = new String(Files.readAllBytes(resource.getFile().toPath()), StandardCharsets.UTF_8);

	        // Set Thymeleaf variables
	        Context thymeleafContext = new Context();
	        thymeleafContext.setVariable("resetLink", resetLink);
	        
	        thymeleafContext.setVariable("nameuser", ur.findByEmail(recipient).getName());


	        // Process the Thymeleaf template
	        String processedHtmlContent = thymeleafTemplateEngine.process("email.html", thymeleafContext);

	        // Set the HTML content with inline image
	        mimeMessageHelper.setText(processedHtmlContent, true);
	        
	        // Add inline image
	        ClassPathResource img = new ClassPathResource("static/img/scmlogo.png");
	        mimeMessageHelper.addInline("logo", img);

	        // Sending the mail
	        javaMailSender.send(mimeMessage);

	        System.out.println("HTML Content: " + processedHtmlContent);

	        return "Mail sent Successfully";
	    } catch (MessagingException e) {
	        e.printStackTrace();
	        return "Error while sending mail!!!";
	    }
	}


}
