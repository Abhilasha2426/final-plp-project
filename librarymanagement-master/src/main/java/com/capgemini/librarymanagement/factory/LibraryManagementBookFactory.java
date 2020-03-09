package com.capgemini.librarymanagement.factory;

import com.capgemini.librarymanagement.dao.AdminBookDao;
import com.capgemini.librarymanagement.dao.AdminBookDaoImpl;
import com.capgemini.librarymanagement.dao.UserDAO;
import com.capgemini.librarymanagement.dao.UserDAOImpl;
import com.capgemini.librarymanagement.service.AdminBookService;
import com.capgemini.librarymanagement.service.AdminBookServiceImpl;
import com.capgemini.librarymanagement.service.UserService;
import com.capgemini.librarymanagement.service.UserServiceImpl;

public class LibraryManagementBookFactory {
	public LibraryManagementBookFactory() {

	}

	public static AdminBookDao objLibraryDao() {
		AdminBookDao dao1 = new AdminBookDaoImpl();
		return dao1;

	}

	public static AdminBookService objLibraryService() {
		AdminBookService service = new AdminBookServiceImpl();
		return service;

	}

	public static UserDAO objUserLibraryDao() {
		UserDAO userDao = new UserDAOImpl();
		return userDao;
	}

	public static UserService objUserLibraryService() {
		UserService user = new UserServiceImpl();
		return user;
	}

}
