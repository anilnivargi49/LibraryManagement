package com.library.main;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.Scanner;

import com.library.dto.Book;
import com.library.dto.User;
import com.library.exception.UserException;
import com.library.util.ConnectionUtil;
import com.library.util.LoggedinUser;

public class LibraryMainClass {

	public static void main(String[] args) throws ClassNotFoundException, SQLException, ParseException {
		try (Scanner scanner = new Scanner(System.in)) {
			boolean flagForToContinueLoop = true;
			while (flagForToContinueLoop) {
				System.out.println("Enter your username: ");
				String userName = scanner.nextLine();
				Connection con = ConnectionUtil.getConnection();
				System.out.println("Connection established");
				User user = LoggedinUser.getLoggedInUserDetails(con, userName);
				if (user == null) {
					throw new UserException("User doesn't exist");
				}
				System.out.println("Your role is " + user.getRole());

				if (user.getRole().equalsIgnoreCase("ADMIN")) {
					String optionToContinue = null;
					boolean flag = true;
					while (flag) {
						System.out.println(" you can enter any one of the functionality below");
						System.out.println("1 - Add book to library\n" + "2 - Remove book from the library\n"
								+ "3 - Get list of books issued by the particular user\n"
								+ "4 - Get balance amount of particular user\n"
								+ "5 - Close user account from the library");
						String optionSelected = scanner.nextLine();
						switch (optionSelected) {
						case "1":
							System.out.println(
									"Enter book details - book Id, book name, book author and qty - add it in comma separated ");
							String bookDetails = scanner.nextLine();
							String[] bookDetailsSplit = bookDetails.split(",");
							boolean updated = BookFunctionality.addBook(con, new Book(bookDetailsSplit[0],
									bookDetailsSplit[1], bookDetailsSplit[2], Integer.valueOf(bookDetailsSplit[3])));
							if (!updated) {
								System.out.println(
										"There is an issue with system, book details not added in the database");
							} else {
								System.out.println("Book details are saved successfully");
							}
							System.out.println(
									"If you wish to continue to check other functionality, if yes then type Y else N");
							optionToContinue = scanner.nextLine();
							if (optionToContinue.equalsIgnoreCase("Y")) {
								continue;
							} else {
								flag = false;
							}
							break;
						case "2":
							System.out.println("Enter book id to remove from the database");
							String bookId = scanner.nextLine();
							boolean deleted = BookFunctionality.removeBook(con, bookId);
							if (!deleted) {
								System.out.println(
										"There is an issue with system, given bookId is not deleted from the database");
							} else {
								System.out.println("Book details removed from the system successfully");
							}
							System.out.println(
									"If you wish to continue to check other functionality, if yes then type Y else N");
							optionToContinue = scanner.nextLine();
							if (optionToContinue.equalsIgnoreCase("Y")) {
								continue;
							} else {
								flag = false;
							}
							break;
						case "3":
							System.out.println("Enter a student username to check the issued book");
							String studentUserName = scanner.nextLine();
							List<Book> books = BookFunctionality.getBookDetails(con, studentUserName);
							if (books == null || books.isEmpty()) {
								System.out.println("There is no any book issued to the user");
							} else {
								System.out.println("Book issued successfully");
							}
							System.out.println(
									"If you wish to continue to check other functionality, if yes then type Y else N");
							optionToContinue = scanner.nextLine();
							if (optionToContinue.equalsIgnoreCase("Y")) {
								continue;
							} else {
								flag = false;
							}
							break;
						case "4":
							System.out.println("Enter a student username to check the balance amount");
							studentUserName = scanner.nextLine();
							double amount = UserClass.getUserLibraryAmount(con, studentUserName);
							System.out.println("Student library account amount is - " + amount);
							System.out.println(
									"If you wish to continue to check other functionality, if yes then type Y else N");
							optionToContinue = scanner.nextLine();
							if (optionToContinue.equalsIgnoreCase("Y")) {
								continue;
							} else {
								flag = false;
							}
							break;
						case "5":
							System.out.println("Enter user name to remove from the database");
							studentUserName = scanner.nextLine();
							boolean isRemovedUser = UserClass.removeUser(con, studentUserName);
							if (!isRemovedUser) {
								System.out.println(
										"There is an issue with system, given user name is not deleted from the database");
							} else {
								System.out.println("UserName removed from the system successfully");
							}
							System.out.println(
									"If you wish to continue to check other functionality, if yes then type Y else N");
							optionToContinue = scanner.nextLine();
							if (optionToContinue.equalsIgnoreCase("Y")) {
								continue;
							} else {
								flag = false;
							}
							break;
						}
					}
				} else if (user.getRole().equalsIgnoreCase("STUDENT")) {
					String optionToContinue = null;
					boolean flag = true;
					while (flag) {
						System.out.println(" you can enter any one of the functionality below");
						System.out.println("1 - Issue books from the library\n" + "2 - Return book to the library\n"
								+ "3 - Total books issued for the particular author");
						String optionSelected = scanner.nextLine();
						switch (optionSelected) {
						case "1":
							System.out.println("Enter bookId to issue book");
							String bookId = scanner.nextLine();
							boolean isBookIssued = UserClass.issueBook(con, bookId, user.getUserName());
							if (!isBookIssued) {
								System.out.println("There is an issue with system, given book is not issued");
							} else {
								System.out.println("Book issued successfully");
							}
							System.out.println(
									"If you wish to continue to check other functionality, if yes then type Y else N");
							optionToContinue = scanner.nextLine();
							if (optionToContinue.equalsIgnoreCase("Y")) {
								continue;
							} else {
								flag = false;
							}
							break;
						case "2":
							System.out.println("Enter bookId to return");
							bookId = scanner.nextLine();
							boolean isBookReturned = UserClass.returnBook(con, bookId, user.getUserName());
							if (!isBookReturned) {
								System.out.println("There is an issue with system, given book is not returned");
							} else {
								System.out.println("Book is returned to the library successfully");
							}
							System.out.println(
									"If you wish to continue to check other functionality, if yes then type Y else N");
							optionToContinue = scanner.nextLine();
							if (optionToContinue.equalsIgnoreCase("Y")) {
								continue;
							} else {
								flag = false;
							}
							break;
						case "3":
							System.out.println("Enter book author name");
							String authorName = scanner.nextLine();
							List<Book> books = UserClass.totalBookIssuedByAuthor(con, user.getUserName(), authorName);
							if (books == null || books.isEmpty()) {
								System.out.println("There is an issue with system, unable to fect the data");
							}
							System.out.println(
									"If you wish to continue to check other functionality, if yes then type Y else N");
							optionToContinue = scanner.nextLine();
							if (optionToContinue.equalsIgnoreCase("Y")) {
								continue;
							} else {
								flag = false;
							}
							break;
						} 
						continue;
					}

				} else {
					throw new UserException("Not a valid role for the user");
				}
			}
			scanner.close();
		}
	}
}
