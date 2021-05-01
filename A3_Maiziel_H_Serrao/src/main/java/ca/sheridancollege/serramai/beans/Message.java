package ca.sheridancollege.serramai.beans;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private long id;
	private String thread_id;
	private String author;
	private String message;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate message_date;
	@DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
	private LocalTime message_time;

}
