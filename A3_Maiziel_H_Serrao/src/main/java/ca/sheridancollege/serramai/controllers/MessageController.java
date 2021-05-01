package ca.sheridancollege.serramai.controllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ca.sheridancollege.serramai.beans.Message;
import ca.sheridancollege.serramai.beans.UserRegistration;
import ca.sheridancollege.serramai.database.DatabaseAccess;

/** SERVER PORT CHANGED TO 8443 */

@Controller
public class MessageController {

	@Autowired
	JdbcUserDetailsManager jdbcUserDetailsManager;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private DatabaseAccess da;

	// takes the user the index page
	@GetMapping("/")
	public String getRoot() {
		return "index";
	}

	// takes the user to the credentials page
	@GetMapping("/credentials")
	public String getCredentials() {
		return "credentials";
	}

	// takes the user to the page displaying all threads
	@GetMapping("/user")
	public String getForum(Model model) {
		ArrayList<Message> threads = da.getAllThreads();
		model.addAttribute("threads", threads);
		return "user/forum.html";
	}

	// takes the user to the page displaying all the messages in a specific thread
	// - user can also add a message
	@GetMapping("/user/{thread_id}")
	public String viewThread(Model model, @ModelAttribute("thread_id") String thread_id) {
		ArrayList<Message> all_messages = da.getMessagesFromSpecificThread(thread_id);
		if (all_messages.size() == 0) {
			return "redirect:/user";
		}
		model.addAttribute("messages", all_messages);

		String currentUserName = "ANONYMOUS";
		User user = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		currentUserName = (String) user.getUsername();

		Message new_message = new Message();
		new_message.setAuthor(currentUserName);
		new_message.setThread_id(thread_id);
		new_message.setMessage_date(LocalDate.now());
		new_message.setMessage_time(LocalTime.now());
		model.addAttribute("new_message", new_message);
		return "user/thread.html";
	}

	// inserts message into the database
	@PostMapping("/addMessage")
	public String addMessageToDatabase(@ModelAttribute Message message, Model model) {
		String thread_id = message.getThread_id();
		if (message.getMessage().trim().equals("")) {
			return "redirect:/user/" + thread_id + "?message_null";
		}
		if (message.getMessage().length() >= 255) {
			return "redirect:/user/" + thread_id + "?message_too_long";
		}
		da.insertMessage(message);
		ArrayList<Message> all_messages = da.getMessagesFromSpecificThread(thread_id);
		model.addAttribute("messages", all_messages);
		return "redirect:/user/" + thread_id;
	}

	// method that brings the user to the login page
	@GetMapping("/login")
	public String login() {
		return "login.html";
	}

	// takes the user to registration page
	@GetMapping("/register")
	public String register(Model model, UserRegistration user) {
		model.addAttribute("user", user);
		return "register";
	}

	// inserts a user into the database with role "MINION"
	@PostMapping("/register")
	public String processRegister(@ModelAttribute UserRegistration user) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("ROLE_MINION"));
		String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
		User newuser = new User(user.getUsername(), encodedPassword, authorities);
		jdbcUserDetailsManager.createUser(newuser);
		return "redirect:/credentials?registered";
	}

	// takes the a user to a page where they can add a thread
	@GetMapping("new_thread")
	public String makeNewThread(Model model) {
		String currentUserName = "ANONYMOUS";
		User user = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		currentUserName = (String) user.getUsername();

		Message new_message = new Message();
		new_message.setAuthor(currentUserName);
		new_message.setMessage_date(LocalDate.now());
		new_message.setMessage_time(LocalTime.now());
		model.addAttribute("new_message", new_message);
		return "user/new_thread.html";
	}

	// inserts the new message and thread_id into the database
	@PostMapping("create_thread")
	public String addThreadAndMessageToDatabase(@ModelAttribute Message message, Model model) {
		String thread_id = message.getThread_id();
		if (da.checkIfThreadAlreadyExists(thread_id)) {
			System.out.println(da.checkIfThreadAlreadyExists(thread_id));
			return "redirect:/new_thread?thread_exists";
		}
		if (thread_id.trim().equals("")) {
			return "redirect:/new_thread?thread_null";
		}
		if (message.getMessage().trim().equals("")) {
			return "redirect:/new_thread?message_null";
		}
		if (message.getMessage().length() >= 255) {
			return "redirect:/new_thread?message_too_long";
		}
		da.insertMessage(message);
		ArrayList<Message> all_messages = da.getMessagesFromSpecificThread(thread_id);
		model.addAttribute("messages", all_messages);
		return "redirect:/user/" + thread_id;
	}

}
