package ca.sheridancollege.serramai.database;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.serramai.beans.Message;

@Repository
public class DatabaseAccess {

	@Autowired
	protected NamedParameterJdbcTemplate jdbc;

	// method that inserts messages into the database
	public void insertMessage(Message message) {
		String query = "INSERT INTO Messages (thread_id, author, message, message_date, message_time) "
				+ "VALUES (:thread_id, :author, :message, :message_date, :message_time)";

		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("thread_id", message.getThread_id());
		parameters.addValue("author", message.getAuthor());
		parameters.addValue("message", message.getMessage());
		parameters.addValue("message_date", message.getMessage_date());
		parameters.addValue("message_time", message.getMessage_time());

		jdbc.update(query, parameters);
	}

	// method to retrieve distinct threads from the database
	public ArrayList<Message> getAllThreads() {
		String q = "SELECT DISTINCT thread_id FROM Messages GROUP BY thread_id;";
		ArrayList<Message> thread_ids = (ArrayList<Message>) jdbc.query(q,
				new BeanPropertyRowMapper<Message>(Message.class));
		ArrayList<String> thread_titles = new ArrayList<String>();
		MapSqlParameterSource parameters = new MapSqlParameterSource();

		for (int i = 0; i < thread_ids.size(); i++) {
			String thread_title_i = thread_ids.get(i).getThread_id();
			thread_titles.add(thread_title_i);
		}

		ArrayList<Long> all_message_threads = new ArrayList<Long>();

		// using string array of thread titles, find the minimum id of the thread titles
		for (int i = 0; i < thread_titles.size(); i++) {
			String query = "SELECT DISTINCT id FROM Messages WHERE thread_id=:thread_id GROUP BY id";
			parameters.addValue("thread_id", thread_titles.get(i));
			ArrayList<Message> messages = (ArrayList<Message>) jdbc.query(query, parameters,
					new BeanPropertyRowMapper<Message>(Message.class));
			all_message_threads.add(messages.get(0).getId());
		}

		ArrayList<Long> all_message_thread_ids = new ArrayList<Long>();

		// int array of thread ids
		for (int i = 0; i < all_message_threads.size(); i++) {
			Long thread_id_i = all_message_threads.get(i);
			all_message_thread_ids.add(thread_id_i);
		}

		ArrayList<Message> threads_messages_containing_all_info = new ArrayList<Message>();

		// using int array of thread ids, find all attributes associated with messages
		for (int i = 0; i < all_message_thread_ids.size(); i++) {
			String query = "SELECT * FROM Messages where id=:id";
			parameters.addValue("id", all_message_thread_ids.get(i));
			ArrayList<Message> messages = (ArrayList<Message>) jdbc.query(query, parameters,
					new BeanPropertyRowMapper<Message>(Message.class));
			threads_messages_containing_all_info.add(messages.get(0));
		}

		return threads_messages_containing_all_info;
	}

	// method to retrieve all messages in the same thread by sorting by thread_id
	public ArrayList<Message> getMessagesFromSpecificThread(String thread_id) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "SELECT * FROM Messages WHERE thread_id=:thread_id";
		parameters.addValue("thread_id", thread_id);
		ArrayList<Message> messages = (ArrayList<Message>) jdbc.query(query, parameters,
				new BeanPropertyRowMapper<Message>(Message.class));
		return messages;
	}

	// method that checks if thread exists
	public boolean checkIfThreadAlreadyExists(String thread_id) {
		boolean thread_exists = true;
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "SELECT * FROM Messages WHERE thread_id=:thread_id";
		parameters.addValue("thread_id", thread_id);
		ArrayList<Message> messages = (ArrayList<Message>) jdbc.query(query, parameters,
				new BeanPropertyRowMapper<Message>(Message.class));
		if (messages.size() == 0) {
			thread_exists = false;
		}
		return thread_exists;
	}

}
