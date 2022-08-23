package com.example.spring.controllers.advice;

import java.util.NoSuchElementException;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.spring.controllers.dto.MessageDto;

@RestControllerAdvice
public class Advice {
	
	@ExceptionHandler(NullPointerException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public MessageDto processNullPointerException(NullPointerException exception) {
		MessageDto message = new MessageDto();
		message.setMessage("Null pointer error found in request, try again later.");
		message.setType("NULL POINTER ERROR");
		return message;
	}
	
	
	@ExceptionHandler(NoSuchElementException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public MessageDto processNoSuchElementException(NoSuchElementException exception) {
		MessageDto message = new MessageDto();
		message.setMessage("No Such Element Exception error found in request, try again later.");
		message.setType("NO SUCH ELEMENT ERROR");
		return message;
	}
	
	@ExceptionHandler(EmptyResultDataAccessException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public MessageDto processEmptyResultDataAccessException(EmptyResultDataAccessException exception) {
		MessageDto message = new MessageDto();
		message.setMessage("Empty Result Data Access - object not found.");
		message.setType("EMPTY RESULT DATA ACCESS ERROR");
		return message;
	}

}
