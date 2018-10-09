package com.udemy.jdbc;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class TestServlet
 */
@WebServlet("/TestServlet")
public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// define datasource/connection pool for resource injection
	@Resource(name = "jdbc/web_student_tracker")
	private DataSource dataSource;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 1. Set up print writer
		PrintWriter out = response.getWriter();
		response.setContentType("text/plain");

		// 2. get database connection
		Connection myConnection;
		Statement myStatement;
		ResultSet myResults;

		try {
			myConnection = dataSource.getConnection();

			// 3. create SQL query
			String myQuery = "SELECT * FROM STUDENT";
			myStatement = myConnection.createStatement();

			// 4. execute SQL query
			myResults = myStatement.executeQuery(myQuery);

			// 5. process the result set
			while (myResults.next()) {
				String email = myResults.getString("email");
				out.println(email);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}