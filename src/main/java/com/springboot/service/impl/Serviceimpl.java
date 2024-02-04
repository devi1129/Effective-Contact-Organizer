package com.springboot.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.config.SpringSecurityconfig;
import com.springboot.emailutils.EmailService;
import com.springboot.entity.Contact;
import com.springboot.entity.Userdetails;
import com.springboot.exception.Emailalreadyexistsexception;
import com.springboot.repositry.Contactrepositry;
import com.springboot.repositry.Userrepositry;
import com.springboot.service.Serviceinterface;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Service
public class Serviceimpl implements Serviceinterface {

	@Autowired
	private Userrepositry ur;

	@Autowired
	private Contactrepositry cr;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private EmailService es;

	@Override
	public String getBaseUrl() {
		String scheme = request.getScheme();
		String serverName = request.getServerName();
		int serverPort = request.getServerPort();

		StringBuilder baseUrl = new StringBuilder();
		baseUrl.append(scheme).append("://").append(serverName);

		if ((scheme.equals("http") && serverPort != 80) || (scheme.equals("https") && serverPort != 443)) {
			baseUrl.append(':').append(serverPort);
		}
		System.out.println(baseUrl.toString());
		return baseUrl.toString();
	}

	public void sendPasswordResetEmail(String userEmail) {

		String resetToken = generateResetToken();

		String resetLink = getBaseUrl() + "/reset-password?token=" + resetToken;
		Userdetails u=ur.findByEmail(userEmail);
		u.setResettoken(resetToken);
		ur.save(u);
		es.sendPasswordResetEmail(userEmail, resetLink);
	}

	private String generateResetToken() {
		return UUID.randomUUID().toString();
	}

	@Override
	public void saveregister(Userdetails user) {

		Userdetails st = ur.findByEmail(user.getEmail());

		if (st != null && (st.getEmail()).equals(user.getEmail()))

		{
			throw new Emailalreadyexistsexception("Email already exists");
		}

		user.setRole("ROLE_user");
		user.setEnabled(true);
		user.setImageurl("user.png");

		user.setPassword(SpringSecurityconfig.passwordEncoder().encode(user.getPassword()));
		ur.save(user);

	}

	@Override
	public void savecontact(Contact contact, Userdetails u, MultipartFile file) {
		Contact c = cr.findByEmail(contact.getEmail());

		if (c != null && c.getEmail().equals(contact.getEmail())) {
			throw new Emailalreadyexistsexception("Email already exists");
		}

		if (file.isEmpty()) {
			contact.setImage("contact.png");
		} else {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss-SSS");
			String formattedDateTime = LocalDateTime.now().format(formatter);

			String fileName = formattedDateTime + "_" + file.getOriginalFilename();

			String filePath = "src/main/resources/static/contactprofiles/" + fileName;

			try {
				Path path = Paths.get(filePath);
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				contact.setImage(fileName);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		u.getContacts().add(contact);
		contact.setUser(u);
		ur.save(u);

	}

	@Override
	public void updatecontact(Contact updatedContact, Userdetails user, MultipartFile file, Long cid) {
		// Fetch the existing contact details from the database
		Contact duplicateContact = cr.findByEmail(updatedContact.getEmail());
		Contact existingContact = cr.findById(cid).get();

		if (duplicateContact != null && duplicateContact.getEmail().equals(updatedContact.getEmail())
				&& duplicateContact.getCid() != cid) {
			throw new Emailalreadyexistsexception("Email already exists for another contact");
		}
		if (file.isEmpty()) {
			// If no new image is provided, keep the existing image or set a default one
			if (existingContact.getImage() == null || existingContact.getImage().isEmpty()) {
				existingContact.setImage("contact.png");
			}
		} else {
			String imageName = existingContact.getImage();

			// Delete the image file from the static/contactprofiles folder
			if (imageName != null && !imageName.isEmpty() && !imageName.equals("contact.png")) {
				try {
					String imagePath = "src/main/resources/static/contactprofiles/" + imageName;
					Files.deleteIfExists(Paths.get(imagePath));
				} catch (IOException e) {
					e.printStackTrace(); // Handle the exception as needed
				}
			}
			// If a new image file is provided, update the contact's image
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss-SSS");
			String formattedDateTime = LocalDateTime.now().format(formatter);

			String fileName = formattedDateTime + "_" + file.getOriginalFilename();
			String filePath = "src/main/resources/static/contactprofiles/" + fileName;

			try {
				Path path = Paths.get(filePath);
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				existingContact.setImage(fileName);
			} catch (IOException e) {
				e.printStackTrace();
				// Handle the exception as needed
			}
		}

		// Update the contact details with the new values
		existingContact.setFirstname(updatedContact.getFirstname());
		existingContact.setLastname(updatedContact.getLastname());
		existingContact.setWork(updatedContact.getWork());
		existingContact.setEmail(updatedContact.getEmail());
		existingContact.setPhone(updatedContact.getPhone());
		existingContact.setGender(updatedContact.getGender());
		existingContact.setDescription(updatedContact.getDescription());
		existingContact.setUser(user);
		// Save the updated contact
		cr.save(existingContact);

	}

	@Override
	public void delete(Userdetails user, Contact contact) {
		// TODO Auto-generated method stub

		user.getContacts().remove(contact);
		String imageName = contact.getImage();

		// Update the user in the database to reflect the removal of the contact
		ur.save(user);

		// Delete the contact from the database
		cr.delete(contact);

		// Get the image filename associated with the contact

		// Delete the image file from the static/contactprofiles folder
		if (imageName != null && !imageName.isEmpty() && !imageName.equals("contact.png")) {
			try {
				String imagePath = "src/main/resources/static/contactprofiles/" + imageName;
				Files.deleteIfExists(Paths.get(imagePath));
			} catch (IOException e) {
				e.printStackTrace(); // Handle the exception as needed
			}
		}

	}

	@Override
	public void saveuserprofile(Userdetails u, MultipartFile file) {
		Userdetails duplicateuser = ur.findByEmail(u.getEmail());

		if (duplicateuser != null && duplicateuser.getEmail().equals(u.getEmail())
				&& duplicateuser.getId() != u.getId()) {
			throw new Emailalreadyexistsexception("Email already exists for another user");
		}
		if (file.isEmpty()) {
			// If no new image is provided, keep the existing image or set a default one
			if (u.getImageurl() == null || u.getImageurl().isEmpty()) {
				u.setImageurl("user.png");
			}
		} else {
			String imageName = u.getImageurl();

			// Delete the image file from the static/contactprofiles folder
			if (imageName != null && !imageName.isEmpty() && !imageName.equals("user.png")) {
				try {
					String imagePath = "src/main/resources/static/userprofiles/" + imageName;
					Files.deleteIfExists(Paths.get(imagePath));
				} catch (IOException e) {
					e.printStackTrace(); // Handle the exception as needed
				}
			}
			// If a new image file is provided, update the contact's image
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss-SSS");
			String formattedDateTime = LocalDateTime.now().format(formatter);

			String fileName = formattedDateTime + "_" + file.getOriginalFilename();
			String filePath = "src/main/resources/static/userprofiles/" + fileName;

			try {
				Path path = Paths.get(filePath);
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				u.setImageurl(fileName);
			} catch (IOException e) {
				e.printStackTrace();
				// Handle the exception as needed
			}
		}

		// Update the contact details with the new values
		ur.save(u);

	}

	@Override
	public void deleteuser(Userdetails user) {
		// TODO Auto-generated method stub

		String imageName = user.getImageurl();

		ur.delete(user);
		if (imageName != null && !imageName.isEmpty() && !imageName.equals("user.png")) {
			try {
				String imagePath = "src/main/resources/static/userprofiles/" + imageName;
				Files.deleteIfExists(Paths.get(imagePath));
			} catch (IOException e) {
				e.printStackTrace(); // Handle the exception as needed
			}
		}

	}

}
