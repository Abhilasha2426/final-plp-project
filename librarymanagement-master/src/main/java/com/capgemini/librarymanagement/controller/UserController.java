package com.capgemini.librarymanagement.controller;

import java.util.Date;
import java.util.Scanner;

import com.capgemini.librarymanagement.LibraryMainPage;
import com.capgemini.librarymanagement.dao.AdminBookDao;
import com.capgemini.librarymanagement.dao.AdminBookDaoImpl;
import com.capgemini.librarymanagement.db.DbStore;
import com.capgemini.librarymanagement.dto.BookInfo;
import com.capgemini.librarymanagement.dto.UserBookDetail;
import com.capgemini.librarymanagement.dto.UserInfoBean;
import com.capgemini.librarymanagement.exception.BookGenericException;
import com.capgemini.librarymanagement.service.UserService;
import com.capgemini.librarymanagement.service.UserServiceImpl;
import com.capgemini.librarymanagement.validation.LibraryManageValidation;

public class UserController {

	UserService userService = new UserServiceImpl();
	Scanner scanner = new Scanner(System.in);

	public void user() {
		System.out.println("User login successfully");
		while (true) {
			System.out.println("-------------------------------------------------------");
			System.out.println("Enter your choice");
			System.out.println("Enter 1 for Search Book: Search Book\nEnter 2 for Logout: Logout");

			int choice = scanner.nextInt();
			switch (choice) {
			case 1:
				displaySearchBookWithName();
				break;
			case 2:
				System.out.println("User Logout Successfull");
				System.out.println("-------------------------------------------------------");
				LibraryMainPage.mainPage();
				DbStore.user.clear();

				break;

			}
		}
	}

	private void displaySearchBookWithName() {
		System.out.println("Search Based on Book Name");
		String name = scanner.next();
		LibraryManageValidation searchValidation = new LibraryManageValidation();

		if (searchValidation.bookValidation(name)) {
			BookInfo book = userService.searchBookWithName(name);
			if (book != null) {
				System.out.println("-------------------Book Details------------------------");
				System.out.println("Book Name:" + book.getBookName());
				System.out.println("Book Author Name:" + book.getBookAuthor());
				System.out.println("Publisher Name:" + book.getPublisher());
				System.out.println("-------------------------------------------------------");
				UserInfoBean bean = new UserInfoBean();
				System.out.println(
						"Enter 1 for Borrow Book: Borrow Book\nEnter 2 for Return Book: Return Book\nEnter 3 for Logout: Logout\n");
				int choice = scanner.nextInt();
				switch (choice) {

				case 1:
					if (barrow(book, bean)) {
						try {
							System.out.println("Your Book Request is on proccessing");
							System.out.println("-------------------------------------------------------");
							Thread.sleep(10000);
							AdminBookDao adminBookDaoImpl = new AdminBookDaoImpl();
							adminBookDaoImpl.requestPass();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					} else {
						System.err.println("unable to send Request");
					}

					break;
				case 2:
					System.out.println("Enter return date");
					String date = scanner.next();
					if (userService.bookReturn(date)) {
						try {
							System.out.println("Your Book Return Request is on proccessing");
							System.out.println("-------------------------------------------------------");
							Thread.sleep(10000);
							System.out.println("Book Return Request Sent Successfully");
							AdminBookDao adminBookDaoImpl = new AdminBookDaoImpl();
							adminBookDaoImpl.bookRecieve();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					} else {
						System.err.println("Unable to send book return request");

					}
					break;
				case 3:
					System.out.println("User Logout Successfull");
					System.out.println("-------------------------------------------------------");
					LibraryMainPage.mainPage();
					DbStore.user.clear();

				default:
					break;
				}
			}
		} else {
			try {
				throw new BookGenericException("Invalid Name");
			} catch (BookGenericException e) {
				System.err.println(e.getMessage());
			}

		}

	}

	public boolean barrow(BookInfo book, UserInfoBean bean) {

		if (userService.requestCheck(book, bean)) {
			return true;

		} else {
			return false;
		}

	}

}
