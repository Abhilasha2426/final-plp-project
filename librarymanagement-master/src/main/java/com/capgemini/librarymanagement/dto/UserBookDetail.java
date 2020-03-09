package com.capgemini.librarymanagement.dto;

import java.util.Date;

import lombok.Data;

@Data
public class UserBookDetail {
	
	private String userId;
	private String bookId;
	private Date issueDate;
	private Date returnDate;
	private double fine;
	private boolean isBorrowed;
	private int count;
	
}
