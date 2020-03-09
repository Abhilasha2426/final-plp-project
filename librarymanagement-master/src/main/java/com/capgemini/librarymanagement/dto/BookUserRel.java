package com.capgemini.librarymanagement.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class BookUserRel {
	
	private List<UserBookDetail> book;
	
	private UserInfoBean userInfoBean;
 
	private Date returnDate ;
	

	
	
}
