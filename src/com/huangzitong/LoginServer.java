package com.huangzitong;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

/**
 * Created by 30812 on 2016-10-06.
 */
public class LoginServer extends HttpServlet {
    final String sqlDriver = "com.mysql.jdbc.Driver";
    final String sqlURL = "jdbc:mysql://localhost:3306/LoginServer";
    final String databaseUsername = "root";
    final String databasePassword = "1234";
    //Change the four strings above to make it run under your configuration of database.
    //Your database table should be named as "user" and has 3 fields:
    // (1) username: varchar(32) primary key and unique.
    // (2) password: varchar(32)
    // (3) profile: varchar(255)

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String command = request.getParameter("command"); // Command indicates the purpose of this request.
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        PrintWriter out = response.getWriter();

        try {
            Class.forName(sqlDriver);
            Connection con = DriverManager.getConnection(sqlURL, databaseUsername, databasePassword);
            //Connect to the database.
            if (command.equals("register")) {
                //Registration:
                String profile = request.getParameter("profile"); //Get the encrypted user profile.
                PreparedStatement pst = (PreparedStatement) con.prepareStatement(
                        "insert into user (username, password, profile) values (?, ?, ?)");
                pst.setString(1, username);
                pst.setString(2, password);
                pst.setString(3, profile);
                //Insert the new user to the database table.

                int i = pst.executeUpdate(); //Execute the insertion.
                if (i > 0) {
                    out.print("1Registration succeeds."); //Respond to the client.
                    // The '1' at the beginning is to notify the client the registration succeeds with no error.
                }
            }

            else if (command.equals("login")) {
                //Login:
                String query = "select * from user where username='" + username + "' ";
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(query);
                //Search the username in the database.
                if (rs.next()) {
                    if (password.equals(rs.getString(2))) {
                        out.print("1" + rs.getString(3));
                        //Login succeeds and return the user profile to the client.
                        //Similarly, the '1' indicates the success of login.
                    } else {
                        out.print("Password is incorrect");
                    }
                } else {
                    out.print("Username doesn't exist");
                }
            }

            else if (command.equals("changepassword"))
            {
                //Change the password:
                String query = "select * from user where username='" + username + "' ";
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(query);
                //Search for the username in database.
                if (rs.next())
                {
                    if (password.equals(rs.getString(2)))
                    {
                        String newpassword = request.getParameter("newpassword");
                        PreparedStatement pst = (PreparedStatement)con.prepareStatement(
                            "UPDATE user SET password='" + newpassword + "' WHERE username='"
                            + username + "'"); //Update the password in database.
                        if (pst.executeUpdate() > 0)
                        {
                            out.print("1Password change successfully");
                        }
                    }
                    else {
                        out.print("Wrong old password");
                    }
                }
            }

            else if (command.equals("updateprofile"))
            {
                //Update the profile:
                String query = "select * from user where username='" + username + "' ";
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(query);
                //Search the username.
                if (rs.next())
                {
                    if (password.equals(rs.getString(2)))
                    {
                        String profile = request.getParameter("profile");
                        PreparedStatement pst = (PreparedStatement)con.prepareStatement(
                                "UPDATE user SET profile='" + profile + "' WHERE username='"
                                        + username + "'"); //Update the profile.
                        if (pst.executeUpdate() > 0) {
                            out.print("1Profile update successfully");
                        }
                    }
                    else {
                        out.print("Wrong password");
                    }
                }
            }

            else if (command.equals("postprofile"))
            {
                //Update the profile:
                String query = "select * from user where username='" + username + "' ";
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(query);
                //Search the username.
                if (rs.next())
                {
                    String profile = request.getParameter("profile");
                    PreparedStatement pst = (PreparedStatement)con.prepareStatement(
                            "UPDATE user SET profile='" + profile + "' WHERE username='"
                                    + username + "'"); //Update the profile.
                    pst.executeUpdate();
                }
            }

            con.close(); //Terminate the connection to database.
        } catch (Exception e){
            out.print("Error: " + e);
            //Respond with the exception information.
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response); // Always execute the statement in doPost.
    }
}
