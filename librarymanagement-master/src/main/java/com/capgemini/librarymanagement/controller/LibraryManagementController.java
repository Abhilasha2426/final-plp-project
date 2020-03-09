package com.capgemini.librarymanagement.controller;

import java.util.List;
import java.util.Scanner;

import com.capgemini.librarymanagement.LibraryMainPage;
import com.capgemini.librarymanagement.db.DbStore;
import com.capgemini.librarymanagement.dto.BookInfo;
import com.capgemini.librarymanagement.dto.UserInfoBean;
import com.capgemini.librarymanagement.exception.BookGenericException;
import com.capgemini.librarymanagement.exception.UserGenericException;
import com.capgemini.librarymanagement.service.AdminBookService;
import com.capgemini.librarymanagement.service.AdminBookServiceImpl;
import com.capgemini.librarymanagement.service.UserService;
import com.capgemini.librarymanagement.service.UserServiceImpl;
import com.capgemini.librarymanagement.validation.LibraryManageValidation;

public class LibraryManagementController {

	private static final Object userPassword = null;
	public static int count = 0;
	Scanner scanner = new Scanner(System.in);
	AdminBookService adminBookService = new AdminBookServiceImpl();
	UserService userService = new UserServiceImpl();

	public void admin() {

		System.out.println(" Admin Successfull login");
		while (true) {
			System.out.println("-------------------------------------------------------");
			System.out.println("Enter your choice");
			System.out.println("Enter 1 for Add Book: Add Book");
			System.out.println("Enter 2 for Add User: Add User");
			System.out.println("Enter 3 for Delete Book: Delete Book");
			System.out.println("Enter 4 for Delete User: Delete User");
			System.out.println("Enter 5 for Show All Users: Show All Users");
			System.out.println("Enter 6 for Show All Books: Show All Books");
			System.out.println("Enter 7 for Update User Details: Update User Details");
			System.out.println("Enter 8 for Logout: Logout");
			System.out.println("-------------------------------------------------------");

			int choice = scanner.nextInt();

			switch (choice) {
			case 1:
				System.out.println("Enter book id: ");
				String bookId = scanner.next();

				System.out.println("Enter name of book");
				String bookName = scanner.next().trim();

				System.out.println("Enter author name");
				String bookAuth = scanner.next().trim();

				System.out.println("Enter no of books");
				String bookNum = scanner.next();

				System.out.println("Enter publisher name");
				String pubName = scanner.next().trim();

				LibraryManageValidation validation = new LibraryManageValidation();
				if (validation.bookValidation(bookId, bookName, bookAuth, bookNum, pubName)) {
					BookInfo bookInfo = new BookInfo();
					bookInfo.setBookId(bookId);
					bookInfo.setBookName(bookName);
					bookInfo.setBookAuthor(bookAuth);

					bookInfo.setNoOfBooks(bookNum);
					bookInfo.setPublisher(pubName);

					if (adminBookService.addBook(bookInfo)) {
						System.out.println("Books added successfully with " + bookNum + "copy");
					}
				}

				break;
			case 2:
				System.out.println("Enter UserId: ");
				String usrId = scanner.next();

				System.out.println("Enter UserName:");
				String usrName = scanner.next().trim();

				System.out.println("Enter Email: ");
				String usrEmail = scanner.next().trim();

				System.out.println("Enter Password:");
				String usrPassword = scanner.next().trim();

				UserInfoBean userInfoBean = new UserInfoBean();
				LibraryManageValidation userValidation = new LibraryManageValidation();

				for (UserInfoBean users : DbStore.userInfoBean) {
					if (users.getUsrEmail().equalsIgnoreCase(usrEmail) || users.getUsrId().equalsIgnoreCase(usrId)) {
						count++;
					}

				}
				if (count == 0) {
					count = 0;

					if (userValidation.userValidation(usrId, usrName, usrEmail, usrPassword)) {
						userInfoBean.setUsrId(usrId);
						userInfoBean.setUsrName(usrName);
						userInfoBean.setUsrEmail(usrEmail);
						userInfoBean.setUsrPassword(usrPassword);
						if (adminBookService.addUser(userInfoBean)) {
							System.out.println("User added Successfully");
						} else {
							System.err.println("user  not added");
						}

					}
				} else {

					System.err.println("user is already Exist");
					count = 0;
				}
				break;
			case 3:
				System.out.println("enter the Book id for delete");
				String bookId1 = scanner.next();

				LibraryManageValidation bookIdValidation = new LibraryManageValidation();

				if (bookIdValidation.bookIdValidation(bookId1)) {
					if (adminBookService.deleteBook(bookId1)) {
						System.out.println("Book Deleted Successfully");
					} else {
						System.err.println("Book Id not found");
					}

				} else {
					try {
						throw new BookGenericException("Invalid Book Id");
					} catch (BookGenericException e) {
						System.err.println(e.getMessage());
					}

				}

				break;
			case 4:
				System.out.println("Enter the user id for delete");
				String userId = scanner.next();
				LibraryManageValidation userIdValidation = new LibraryManageValidation();

				if (userIdValidation.userIdValidation(userId)) {
					if (adminBookService.deleteUser(userId)) {
						System.out.println("User Deleted Successfully");
					} else {
						System.err.println("User Id not found");
					}
				} else {
					try {
						throw new UserGenericException("User Id is Invalid");
					} catch (UserGenericException e) {
						System.err.println(e.getMessage());
					}

				}
				break;
			case 5:
				System.out.println("---User Details---");
				List<UserInfoBean> list1 = adminBookService.showAllUser();
				if (!list1.isEmpty()) {
					for (UserInfoBean users : list1) {
						System.out.println(
								"User Id=" + users.getUsrId() + "\t User Name=" + users.getUsrName() + "\t User Email="
										+ users.getUsrEmail() + "\t User Password=" + users.getUsrPassword());
					}
				} else {
					System.err.println("No Users to show");
				}
				break;
			case 6:
				System.out.println("---Book Details---");
				List<BookInfo> list = adminBookService.showAllBooks();
				if (!list.isEmpty()) {
					for (BookInfo books : list) {
						System.out.println("Book Id=" + books.getBookId() + "\t Book Name = " + books.getBookName()
								+ " \t Book Author = " + books.getBookAuthor() + "\t Number of book copies"
								+ books.getNoOfBooks() + "\t Publisher Name=" + books.getPublisher());
					}
				} else {
					System.err.println("No books to show");
				}
				break;
			case 7:
				UserInfoBean bean = new UserInfoBean();
				System.out.println("Enter User Id to Update");
				bean.setUsrId(scanner.next());

				System.out.println("Enter User Name to Update");
				bean.setUsrName(scanner.next().trim());

				System.out.println("Enter User Email to Update");
				bean.setUsrEmail(scanner.next().trim());

				System.out.println("Enter User Passsword to Upadte");
				bean.setUsrPassword(scanner.next().trim());

				LibraryManageValidation userValidatin = new LibraryManageValidation();
				if (userValidatin.userValidation(bean.getUsrId(), bean.getUsrName(), bean.getUsrEmail(),
						bean.getUsrPassword())) {
					if (adminBookService.updateUser(bean) != null) {
						System.out.println("User Details Updated Successfully!!!");

					} else {
						System.err.println("Failed to Update");
					}

				} else {
					try {
						throw new UserGenericException("Invalid User Details");
					} catch (UserGenericException e) {
						System.err.println(e.getMessage());
					}

				}

				break;

			case 8:
				System.out.println("Admin Logout Successfull");
				LibraryMainPage.mainPage();
				System.exit(0);

			default:
				System.err.println("Invalid choice");
				break;
			}

		}
	}

}