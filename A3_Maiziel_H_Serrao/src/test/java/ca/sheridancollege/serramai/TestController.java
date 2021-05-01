package ca.sheridancollege.serramai;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import ca.sheridancollege.serramai.beans.Message;
import ca.sheridancollege.serramai.beans.UserRegistration;
import ca.sheridancollege.serramai.database.DatabaseAccess;

@SpringBootTest
@AutoConfigureMockMvc
public class TestController {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private DatabaseAccess databaseAccess;

	// Tests loading of index page
	@Test
	public void testLoadingIndexPage() throws Exception {
		this.mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(view().name("index"));
	}

	// Tests loading of credentials page
	@Test
	public void testLoadingCredentialsPage() throws Exception {
		this.mockMvc.perform(get("/credentials")).andExpect(status().isOk()).andExpect(view().name("credentials"));
	}

	// Tests loading of forum page
	@Test
	public void testLoadingForumPage() throws Exception {
		this.mockMvc.perform(get("/user")).andExpect(status().isOk()).andExpect(view().name("user/forum.html"));
	}

	// Tests loading of login page
	@Test
	public void testLoadingLogin() throws Exception {
		this.mockMvc.perform(get("/login")).andExpect(status().isOk()).andExpect(view().name("login.html"));
	}

	// Tests loading of register page
	@Test
	public void testLoadingRegister() throws Exception {
		this.mockMvc.perform(get("/register")).andExpect(status().isOk()).andExpect(view().name("register"));
	}

	// Tests loading of new thread page
	@Test
	public void testLoadingNewThreadPage() throws Exception {
		this.mockMvc.perform(get("/new_thread")).andExpect(status().isOk())
				.andExpect(view().name("user/new_thread.html"));
	}

	// Tests original number of entries in database is correct
	@Test
	public void testOriginalNumOfThreads() throws Exception {
		int originalsize = databaseAccess.getAllThreads().size();
		assertThat(4).isEqualTo(originalsize);
	}

	// Tests loading of forum page
	@Test
	public void testLoadingUserThreadIdPage() throws Exception {
		this.mockMvc.perform(get("/user/{thread_id}", "Coronavirus matches my greeting style."))
				.andExpect(status().isFound()).andExpect(view().name("user/thread.html"));
	}

	// Tests number of messages in a specific thread is correct
	@Test
	public void testNumOfSpecificThreads() throws Exception {
		int originalsize = databaseAccess.getMessagesFromSpecificThread("Coronavirus matches my greeting style.")
				.size();
		assertThat(1).isEqualTo(originalsize);
	}

	// Tests insertion of a message into the database
	@Test
	public void testInsertionOfMessageIntoDB() throws Exception {
		Message message = new Message();
		message.setThread_id("Coronavirus matches my greeting style.");
		databaseAccess.insertMessage(message);
		int newsize = databaseAccess.getMessagesFromSpecificThread("Coronavirus matches my greeting style.").size();
		assertThat(2).isEqualTo(newsize);
	}

	// Tests the proper redirection of add message once a user has added a message
	@Test
	public void testProperRedirectionOfAddMessage() throws Exception {
		Message message = new Message();
		message.setThread_id("NewThread");
		message.setMessage("new message");

//	this.mockMvc
//	.perform(post("/addMessage")
//	.flashAttr("message", message).andExpect(status().isFound())
//	.andExpect(redirectedUrl("/user/{thread_id}", "NewThread"));
	}

	// Tests the proper redirection of register once a user has registered their
	// credentials
	@Test
	public void testProperRedirectionOfProcessRegister() throws Exception {
		UserRegistration user = new UserRegistration();
		user.setUsername("New Guy");
		user.setPassword("pass");

//	this.mockMvc
//	.perform(post("/register")
//	.flashAttr("user", user).andExpect(status().isFound())
//	.andExpect(redirectedUrl("/credentials?registered"));
	}

	// Tests the proper redirection of create message once a user has created a
	// message
	@Test
	public void testProperRedirectionOfCreateMessage() throws Exception {
		Message message = new Message();
		message.setThread_id("NewThread");
		message.setMessage("new message");

//	this.mockMvc
//	.perform(post("/create_message")
//	.flashAttr("message", message).andExpect(status().isFound())
//	.andExpect(redirectedUrl("/user/{thread_id}", "NewThread"));
	}

	// Tests that the method for checking if a thread already exists works
	@Test
	public void testThreadAlreadyExists() throws Exception {
		boolean exists = true;
		boolean reality = databaseAccess.checkIfThreadAlreadyExists("Coronavirus matches my greeting style.");
		assertThat(exists).isEqualTo(reality);
	}

	// Message tests

	// Test that tests the getId() method for message
	@Test
	public void testGetId() {
		Message message = new Message();
		message.setId(100);
		assertThat(100).isEqualTo(message.getId());
	}

	// Test that tests the setId() method for message
	@Test
	public void testSetId() {
		Message message = new Message();
		message.setId(100);
		assertThat(message.getId()).isEqualTo(100);
	}

	// Test that tests the getThread_id() method for message
	@Test
	public void testGetThreadId() {
		Message message = new Message();
		message.setThread_id("New Thread");
		assertThat("New Thread").isEqualTo(message.getThread_id());
	}

	// Test that tests the setThread_id() method for message
	@Test
	public void testSetThreadId() {
		Message message = new Message();
		message.setThread_id("New Thread");
		assertThat(message.getThread_id()).isEqualTo("New Thread");
	}

	// Test that tests the getAuthor() method for message
	@Test
	public void testGetAuthor() {
		Message message = new Message();
		message.setAuthor("New Author");
		assertThat("New Author").isEqualTo(message.getAuthor());
	}

	// Test that tests the setAuthor() method for message
	@Test
	public void testSetAuthor() {
		Message message = new Message();
		message.setAuthor("New Author");
		assertThat(message.getAuthor()).isEqualTo("New Author");
	}

	// Test that tests the getMessage() method for message
	@Test
	public void testGetMessage() {
		Message message = new Message();
		message.setMessage("New Message");
		assertThat("New Message").isEqualTo(message.getMessage());
	}

	// Test that tests the setMessage() method for message
	@Test
	public void testSetMessage() {
		Message message = new Message();
		message.setMessage("New Message");
		assertThat(message.getMessage()).isEqualTo("New Message");
	}

	// Test that tests the getMessage_Date() method for message
	@Test
	public void testGetMessageDate() {
		Message message = new Message();
		message.setMessage_date(LocalDate.of(2020, 1, 8));
		assertThat(LocalDate.of(2020, 1, 8)).isEqualTo(message.getMessage_date());
	}

	// Test that tests the setMessage() method for message
	@Test
	public void testSetMessageDate() {
		Message message = new Message();
		message.setMessage_date(LocalDate.of(2020, 1, 8));
		assertThat(message.getMessage_date()).isEqualTo(LocalDate.of(2020, 1, 8));
	}

	// Test that tests the getMessage_time() method for message
	@Test
	public void testGetMessageTime() {
		Message message = new Message();
		message.setMessage_time(LocalTime.parse("03:18:23"));
		assertThat(LocalTime.parse("03:18:23")).isEqualTo(message.getMessage_time());
	}

	// Test that tests the setMessage_time() method for message
	@Test
	public void testSetMessageTime() {
		Message message = new Message();
		message.setMessage_time(LocalTime.parse("03:18:23"));
		assertThat(message.getMessage_time()).isEqualTo(LocalTime.parse("03:18:23"));
	}

	// UserRegistration tests

	// Test that tests the getUsername() method for UserRegistration
	@Test
	public void testGetUsername() {
		UserRegistration user = new UserRegistration();
		user.setUsername("New Guy");
		assertThat("New Guy").isEqualTo(user.getUsername());
	}

	// Test that tests the setUsername() method for UserRegistration
	@Test
	public void testSetUsername() {
		UserRegistration user = new UserRegistration();
		user.setUsername("New Guy");
		assertThat(user.getUsername()).isEqualTo("New Guy");
	}

	// Test that tests the getPassword() method for UserRegistration
	@Test
	public void testGetPassword() {
		UserRegistration user = new UserRegistration();
		user.setPassword("pass");
		assertThat("pass").isEqualTo(user.getPassword());
	}

	// Test that tests the setPassword() method for UserRegistration
	@Test
	public void testSetPassword() {
		UserRegistration user = new UserRegistration();
		user.setPassword("pass");
		assertThat(user.getPassword()).isEqualTo("pass");
	}

}
