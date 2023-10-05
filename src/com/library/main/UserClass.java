package com.library.main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.library.dto.Book;

public class UserClass {

	public static double getUserLibraryAmount(Connection con, String userName)
			throws ClassNotFoundException, SQLException {
		PreparedStatement p = null;
		ResultSet rs = null;
		double amount = 0;
		try {
			String sql = "SELECT  * FROM library_account where user_name = ? ";
			p = con.prepareStatement(sql);
			p.setString(1, userName);
			rs = p.executeQuery();
			while (rs.next()) {
				return rs.getInt("amount");
			}
		}

		catch (SQLException e) {
			System.out.println(e);
		}
		return amount;
	}

	public static boolean removeUser(Connection con, String userName) {
		try {
			removeUserFromLibraryAccount(con, userName);
			removeUserFromBookIssued(con, userName);
			removeUserFromUserTable(con, userName);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	private static boolean removeUserFromUserTable(Connection con, String userName)
			throws ClassNotFoundException, SQLException {
		PreparedStatement p = null;
		try {
			String sql = "DELETE FROM users where user_name=?";
			p = con.prepareStatement(sql);
			p.setString(1, userName);
			int i = p.executeUpdate();
			if (i > 0) {
				return true;
			}
		}

		catch (SQLException e) {
			System.out.println(e);
		}
		return false;
	}

	private static boolean removeUserFromBookIssued(Connection con, String userName)
			throws ClassNotFoundException, SQLException {
		PreparedStatement p = null;
		try {
			String sql = "DELETE FROM book_issued where user_name=?";
			p = con.prepareStatement(sql);
			p.setString(1, userName);
			int i = p.executeUpdate();
			if (i > 0) {
				return true;
			}
		}

		catch (SQLException e) {
			System.out.println(e);
		}
		return false;
	}

	private static boolean removeUserFromLibraryAccount(Connection con, String userName)
			throws ClassNotFoundException, SQLException {
		PreparedStatement p = null;
		try {
			String sql = "DELETE FROM library_account where user_name=?";
			p = con.prepareStatement(sql);
			p.setString(1, userName);
			int i = p.executeUpdate();
			if (i > 0) {
				return true;
			}
		}

		catch (SQLException e) {
			System.out.println(e);
		}
		return false;
	}

	public static boolean issueBook(Connection con, String bookId, String userName)
			throws ClassNotFoundException, SQLException {
		PreparedStatement p = null;
		DateFormat dform = new SimpleDateFormat("dd-MM-yyyy");
		try {
			String sql = "INSERT INTO book_issued(book_id, user_name, book_issued) values(?, ?,?) ";
			p = con.prepareStatement(sql);
			p.setString(1, bookId);
			p.setString(2, userName);
			p.setString(3, dform.format(new Date()));
			int i = p.executeUpdate();
			if (i > 0) {
				return true;
			}
		}

		catch (SQLException e) {
			System.out.println(e);
		}
		return false;
	}

	public static boolean returnBook(Connection con, String bookId, String userName)
			throws ParseException, ClassNotFoundException, SQLException {
		String dateIssued = fetchIssuedBookDate(con, bookId, userName);
		DateFormat dform = new SimpleDateFormat("dd-MM-yyyy");
		if (dateIssued != null && !dateIssued.isEmpty()) {
			Date date1 = dform.parse(dateIssued);
			Date date2 = dform.parse(dform.format(new Date()));
			long diff = date2.getTime() - date1.getTime();
			double charge = (diff + 1) * 2;
			boolean isBookReturned = returnBookToLibrary(con, bookId, userName, charge);
			if (isBookReturned) {
				return updateUserAccount(con, userName, charge);
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	private static boolean updateUserAccount(Connection con, String userName, double charge)
			throws ClassNotFoundException, SQLException {
		PreparedStatement p = null;
		try {
			String sql = "UPDATE library_account SET amount=amount - ? WHERE user_name=?";
			p = con.prepareStatement(sql);
			p.setDouble(1, charge);
			p.setString(2, userName);
			int i = p.executeUpdate();
			if (i > 0) {
				return true;
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
		return false;
	}

	private static String fetchIssuedBookDate(Connection con, String bookId, String userName) {
		PreparedStatement p = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM book_issued WHERE user_name = ? AND book_id=?";
			p = con.prepareStatement(sql);
			p.setString(1, userName);
			p.setString(2, bookId);
			rs = p.executeQuery();
			while (rs.next()) {
				return rs.getString("book_issued");
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
		return null;
	}

	private static boolean returnBookToLibrary(Connection con, String bookId, String userName, double charge)
			throws ClassNotFoundException, SQLException {
		PreparedStatement p = null;
		DateFormat dform = new SimpleDateFormat("dd-MM-yyyy");
		try {
			String sql = "UPDATE book_issued SET book_returned=?,charged = ? WHERE book_id=? AND user_name=?";
			p = con.prepareStatement(sql);
			p.setString(1, dform.format(new Date()));
			p.setDouble(2, charge);
			p.setString(3, bookId);
			p.setString(4, userName);
			int i = p.executeUpdate();
			if (i > 0) {
				return true;
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
		return false;
	}

	public static List<Book> totalBookIssuedByAuthor(Connection con, String userName, String authorName)
			throws ClassNotFoundException, SQLException {
		List<Book> books = new ArrayList<Book>();
		PreparedStatement p = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT  * FROM book bk, book_issued bi " + "WHERE bk.book_id = bi.book_id "
					+ "and bi.book_returned IS NULL and bi.user_name = ? and bk.book_author = ? ";
			p = con.prepareStatement(sql);
			p.setString(1, userName);
			p.setString(2, authorName);
			rs = p.executeQuery();
			while (rs.next()) {
				String bookId = rs.getString("book_id");
				String bookName = rs.getString("book_name");
				String bookAuthor = rs.getString("book_author");
				Integer quantity = rs.getInt("quantity");
				System.out.println("Book Details - bookId - " + bookId + ", bookName - " + bookName + ", bookAuthor - "
						+ bookAuthor);
				books.add(new Book(bookId, bookName, bookAuthor, quantity));
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
		return books;
	}

}
