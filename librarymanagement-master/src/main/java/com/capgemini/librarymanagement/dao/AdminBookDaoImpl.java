package com.capgemini.librarymanagement.dao;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.capgemini.librarymanagement.db.DbStore;
import com.capgemini.librarymanagement.dto.BookInfo;
import com.capgemini.librarymanagement.dto.BookUserRel;
import com.capgemini.librarymanagement.dto.UserBookDetail;
import com.capgemini.librarymanagement.dto.UserInfoBean;

public class AdminBookDaoImpl implements AdminBookDao {

	public static int count = 0;
	public static int size = 0;
	public static int add = 0;

	public boolean addBook(BookInfo bookInfo) {
		DbStore.bookInfo.add(bookInfo);
		return true;
	}

	public boolean addUser(UserInfoBean userInfoBean) {
		DbStore.userInfoBean.add(userInfoBean);
		return true;
	}

	public boolean deleteUser(String userId) {
		Iterator<UserInfoBean> itr = DbStore.userInfoBean.iterator();
		while (itr.hasNext()) {
			UserInfoBean user = itr.next();
			if (user.getUsrId().equals(userId)) {
				DbStore.userInfoBean.remove(user);
				return true;
			}
		}
		return false;
	}

	public boolean deleteBook(String bookId) {
		Iterator<BookInfo> itr = DbStore.bookInfo.iterator();
		while (itr.hasNext()) {
			BookInfo book = itr.next();
			if (book.getBookId().equals(bookId)) {
				DbStore.bookInfo.remove(book);
				return true;
			}
		}
		return false;
	}

	public List<BookInfo> showAllBooks() {
		return DbStore.bookInfo;

	}

	public List<UserInfoBean> showAllUser() {
		return (DbStore.userInfoBean);
	}

	public UserInfoBean updateUser(UserInfoBean userInfoBean) {
		UserInfoBean bean;
		Iterator<UserInfoBean> itr = DbStore.userInfoBean.iterator();
		while (itr.hasNext()) {
			bean = itr.next();
			if (bean.getUsrId() != userInfoBean.getUsrId()) {
				bean.setUsrId(userInfoBean.getUsrId());
				bean.setUsrName(userInfoBean.getUsrName());
				bean.setUsrEmail(userInfoBean.getUsrEmail());
				bean.setUsrPassword(userInfoBean.getUsrPassword());
				return bean;
			} else {
				return null;
			}
		}
		return userInfoBean;

	}

	public boolean requestPass() {
		BookUserRel userRel = new BookUserRel();
		List<UserBookDetail> bookList = new LinkedList<UserBookDetail>();
		for (UserBookDetail books : DbStore.userBookDetails) {
			for (UserInfoBean users : DbStore.user) {
				if (books.getUserId().equals(users.getUsrId())) {
					count++;
					if (books.getCount() <= 3 && count <= 3) {
						books.setBorrowed(true);
						books.setIssueDate(new Date());
						books.setCount(count);
						long lTime = new Date().getTime() + 15 * 24 * 60 * 60 * 1000;
						SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
						count = 0;
						try {
							books.setReturnDate(sdf.parse(sdf.format(new Date(lTime))));
						} catch (ParseException e) {
							e.printStackTrace();
						}

						bookList.add(books);
						userRel.setUserInfoBean(users);
					} else {
						System.out.println("Borrow limit is exceeded(more than three Books)");
						count = 0;
					}
					System.out.println("Your Book Request is Accepted");
					System.out.println("Please return at mentioned date " + books.getReturnDate());
				}
			}
		}
		userRel.setBook(bookList);
		DbStore.userBorrowedBook.add(userRel);
		return true;
	}

	public boolean bookRecieve() {
		for (BookUserRel recieveBook : DbStore.userReturnBooks) {
			for (UserBookDetail detail : DbStore.userBookDetails) {
				if (recieveBook.getUserInfoBean().getUsrId().equals(detail.getUserId())) {
					long diff = recieveBook.getReturnDate().getTime() - detail.getReturnDate().getTime();
					long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
					if (days > 0) {
						for (BookUserRel checkSameUser : DbStore.userBorrowedBook) {
							if (recieveBook.getUserInfoBean().getUsrId()
									.equals(checkSameUser.getUserInfoBean().getUsrId())) {
								size++;
							}
						}
						double amount = days * 5 * size;
						System.out.println(
								"User Id " + recieveBook.getUserInfoBean().getUsrId() + " has a fine amount " + amount);
						amount = 0;
						size = 0;
					} else {
						System.out.println("Thank You for retuning before " + detail.getReturnDate() + " date ");
					}
				}

			}
		}
		for (UserBookDetail bookDetails : DbStore.userBookDetails) {
			for (BookUserRel userRelation : DbStore.userReturnBooks) {
				if (bookDetails.getUserId().equals(userRelation.getUserInfoBean().getUsrId())) {
					DbStore.userBookDetails.remove(bookDetails);
				}
			}
		}

		for (BookUserRel barrowedBook : DbStore.userBorrowedBook) {
			for (BookUserRel userReturnBooks : DbStore.userReturnBooks) {
				if (userReturnBooks.getUserInfoBean().getUsrId().equals(barrowedBook.getUserInfoBean().getUsrId())) {
					DbStore.userBorrowedBook.remove(barrowedBook);
					DbStore.userReturnBooks.remove(userReturnBooks);
				}
			}
		}
		// DbStore1.user.clear();

		return true;
	}

}
