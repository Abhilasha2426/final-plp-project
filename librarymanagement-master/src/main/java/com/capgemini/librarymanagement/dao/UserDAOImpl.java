package com.capgemini.librarymanagement.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import com.capgemini.librarymanagement.db.DbStore;
import com.capgemini.librarymanagement.dto.BookInfo;
import com.capgemini.librarymanagement.dto.BookUserRel;
import com.capgemini.librarymanagement.dto.UserBookDetail;
import com.capgemini.librarymanagement.dto.UserInfoBean;
import com.capgemini.librarymanagement.validation.LibraryManageValidation;

public class UserDAOImpl implements UserDAO {

	static int count = 0;

	public BookInfo searchBookWithName(String name) {
		for (BookInfo book : DbStore.bookInfo) {
			if (book.getBookName().startsWith(name) || book.getBookAuthor().startsWith(name)) {
				return book;
			}
		}
		return null;
	}

	public boolean requestCheck(BookInfo book) {
		UserBookDetail bookDetail = new UserBookDetail();
		bookDetail.setBookId(book.getBookId());
		bookDetail.setCount(1);
		bookDetail.setUserId(DbStore.user.get(0).getUsrId());
		DbStore.userBookDetails.add(bookDetail);

		for (UserBookDetail bookDetailReq : DbStore.userBookDetails) {
			System.out.println("Book Id:" + bookDetailReq.getBookId());
			System.out.println("User Id:" + bookDetailReq.getUserId());
		}
		return true;
	}

	public boolean bookReturn(String date) {
		LibraryManageValidation validation = new LibraryManageValidation();
		if (validation.dateValidation(date)) {
			BookUserRel userRel = new BookUserRel();
			List<UserBookDetail> bookList = new LinkedList<UserBookDetail>();
			for (BookUserRel relation : DbStore.userBorrowedBook) {
				try {
					userRel.setReturnDate(new SimpleDateFormat("dd-MM-yyyy").parse(date));

				} catch (ParseException e) {
					System.err.println(e.getMessage());
				}
				userRel.setUserInfoBean(relation.getUserInfoBean());
				bookList.addAll(relation.getBook());
			}
			DbStore.userReturnBooks.add(userRel);

			return true;
		} else {
			System.err.println("Please enter the date in this form dd-mm-yyyy");
			return false;
		}

	}

}
