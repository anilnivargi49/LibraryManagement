package com.library.main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.library.dto.Book;

public class BookFunctionality {
	
	public static List<Book> getBookDetails(Connection con, String userName)
			throws ClassNotFoundException, SQLException {
		List<Book> bookList = new ArrayList<>();
		PreparedStatement p = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT  * FROM book bk, book_issued bi " + "WHERE bk.book_id = bi.book_id "
					+ "and bi.book_returned IS NULL and bi.user_name = ? ";
			p = con.prepareStatement(sql);
			p.setString(1, userName);
			rs = p.executeQuery();
			while (rs.next()) {
				String bookId = rs.getString("book_id");
				String bookName = rs.getString("book_name");
				String bookAuthor = rs.getString("book_author");
				Integer quantity = rs.getInt("quantity");
				System.out.println("Book Details - bookId - " + bookId + ", bookName - " + bookName + ", bookAuthor - "
						+ bookAuthor);
				bookList.add(new Book(bookId, bookName, bookAuthor, quantity));
			}
		}

		catch (SQLException e) {
			System.out.println(e);
		}
		return bookList;
	}

	public static boolean addBook(Connection con, Book book)
			throws ClassNotFoundException, SQLException {
		PreparedStatement p = null;
		try {
			String sql = "INSERT INTO book(book_id, book_name, book_author, quantity) values(?, ?,?,?) ";
			p = con.prepareStatement(sql);
			p.setString(1, book.getBookId());
			p.setString(2, book.getBookName());
			p.setString(3, book.getBookAuthor());
			p.setInt(4, book.getQty());
			int i = p.executeUpdate();
			if (i>0) {
				return true;
			}
		}

		catch (SQLException e) {
			System.out.println(e);
		}
		return false;
	}
	
	public static boolean removeBook(Connection con, String bookId)
			throws ClassNotFoundException, SQLException {
		PreparedStatement p = null;
		try {
			String sql = "DELETE FROM book where book_id=?";
			p = con.prepareStatement(sql);
			p.setString(1, bookId);
			int i = p.executeUpdate();
			if (i>0) {
				return true;
			}
		}

		catch (SQLException e) {
			System.out.println(e);
		}
		return false;
	}
}
