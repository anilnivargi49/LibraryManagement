package com.library.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.library.dto.Book;
import com.library.dto.User;

public class LoggedinUser {
	
	public static User getLoggedInUserDetails(Connection con, String userName) {
		
		PreparedStatement p = null;
        ResultSet rs = null;
        try {
 
            String sql = "select * from users where user_name=?";
            p = con.prepareStatement(sql);
            p.setString(1, userName);
            rs = p.executeQuery(); 
            while (rs.next()) {
                userName = rs.getString("user_name");
                String role = rs.getString("role");
                return new User(userName, role);
            }
        }

        catch (SQLException e) {
             System.out.println(e);
        }
		return null;
	}

}
