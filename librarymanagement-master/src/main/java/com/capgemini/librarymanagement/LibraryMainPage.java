package com.capgemini.librarymanagement;

import java.util.InputMismatchException;
import java.util.Scanner;

import com.capgemini.librarymanagement.controller.LibraryManagementController;
import com.capgemini.librarymanagement.controller.UserController;
import com.capgemini.librarymanagement.db.DbStore;
import com.capgemini.librarymanagement.dto.UserInfoBean;
import com.capgemini.librarymanagement.exception.ValidInputException;

public class LibraryMainPage {

	public static void mainPage() {

		LibraryManagementController libraryManagementController = new LibraryManagementController();
		Scanner scanner = new Scanner(System.in);
		System.out.println("-------------------------------------------------------");
		System.out.println("------------**********WELCOME**********----------------");
		System.out.println("------********LIBRARY MANAGEMENT SYSTEM*********-------");
		System.out.println("-------------------------------------------------------");
		boolean flag = true;
		while (flag) {
			System.out.println("Available Choices");
			System.out.println("Enter 1 for Admin: Admin\nEnter 2 for User: User\nEnter 3 for Exit: Exit");
			System.out.println("-------------------------------------------------------");
			System.out.println("Enter Your Choice");
			try {
				int choice = scanner.nextInt();

				switch (choice) {
				case 1:
					System.out.println("--------------****Admin Login****-----------------");
					System.out.println("Enter UserName \n");
					String adminName = scanner.next();
					System.out.println("Enter Password \n");
					String adminPassword = scanner.next();
					if (adminName.equals("a") && adminPassword.equals("a")) {

						libraryManagementController.admin();
					} else {
						System.err.println("Please enter valid username and password");
					}
					break;
				case 2:
					System.out.println("--------------****User Login****--------------------");
					System.out.println("Enter Email \n");
					String userEmail = scanner.next();
					System.out.println("Enter Password \n");
					String userPassword = scanner.next();
					int count = 0;
					for (UserInfoBean user : DbStore.userInfoBean) {
						if (user.getUsrEmail().equals(userEmail) && user.getUsrPassword().equals(userPassword)) {
							DbStore.user.add(user);
							UserController controller = new UserController();
							controller.user();
							System.out.println("User Login successfully");
						}
						// else {
						// System.out.println("Please enter valid credential");
						// }
					}
					break;
				case 3:
					System.out.println("Logout Successfull");
					System.out.println("-------------------------------------------------------");
					System.exit(0);
				}
			} catch (InputMismatchException e) {

				try {
					throw new ValidInputException();
				} catch (ValidInputException exp) {
					System.out.println(exp.getMessage());
					mainPage();
				}
			}

		}
		scanner.close();
	}
}
