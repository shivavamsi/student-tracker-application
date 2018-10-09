package com.udemy.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class StudentDbUtil {

	private DataSource dataSource;

	public StudentDbUtil(DataSource dataSource) {

		this.dataSource = dataSource;
	}

	public List<Student> getStudents() {

		List<Student> students = new ArrayList<Student>();

		// 1. get db connection
		Connection myConnection = null;
		Statement myStatement = null;
		ResultSet myResults = null;

		try {
			myConnection = dataSource.getConnection();

			// 2. create SQL query
			String myQuery = "SELECT * FROM student ORDER BY last_name";
			myStatement = myConnection.createStatement();

			// 3. execute SQL query
			myResults = myStatement.executeQuery(myQuery);

			// 4. process result set
			while (myResults.next()) {

				// extract attributes from each result
				String firstName = myResults.getString("first_name");
				String lastName = myResults.getString("last_name");
				String email = myResults.getString("email");
				int id = myResults.getInt("id");

				// create new object from extracted data
				Student tempStudent = new Student(firstName, lastName, email, id);

				// add each result to list
				students.add(tempStudent);

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			// 5. close the connections
			close(myConnection, myStatement, myResults);
		}

		return students;
	}

	private static void close(Connection myConnection, Statement myStatement, ResultSet myResults) {
		System.out.println("closing connections");
		try {
			if (myConnection != null) {
				myConnection.close();
			}
			if (myStatement != null) {
				myStatement.close();
			}
			if (myResults != null) {
				myResults.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void addStudent(Student theStudent) {

		// 1. get db connection
		Connection myConnection = null;
		PreparedStatement myStatement = null;

		try {
			myConnection = dataSource.getConnection();

			// 2. create SQL insert
			String sql = "INSERT INTO student(first_name, last_name, email) VALUES(?, ?, ?)";

			myStatement = myConnection.prepareStatement(sql);

			// 3. Set the parameter values for student
			myStatement.setString(1, theStudent.getFirstName());
			myStatement.setString(2, theStudent.getLastName());
			myStatement.setString(3, theStudent.getEmail());

			System.out.println(sql);

			// 4. execute SQL insert
			myStatement.execute();

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			// 5. close connections
			close(myConnection, myStatement, null);
		}
	}

	public Student getStudent(String studentId) throws Exception {

		Student theStudent = null;
		int theId = Integer.parseInt(studentId);

		// 1. get db connection
		Connection myConnection = null;
		PreparedStatement myStatement = null;
		ResultSet myResult = null;

		try {
			myConnection = dataSource.getConnection();

			// 2.a create SQL query
			String myQuery = "SELECT * FROM student WHERE id=?";
			myStatement = myConnection.prepareStatement(myQuery);

			// 2.b convert studentId to integer
			myStatement.setInt(1, theId);

			// 3. execute SQL query
			myResult = myStatement.executeQuery();

			// 4.a extract attributes from result
			if (myResult.next()) {
				String firstName = myResult.getString("first_name");
				String lastName = myResult.getString("last_name");
				String email = myResult.getString("email");
				int id = myResult.getInt("id");

				// 4.b create Student object
				theStudent = new Student(firstName, lastName, email, id);
			} else {
				throw new Exception("cannot find student id: " + studentId);
			}

		} catch (Exception e) {
			// print exception details
			e.printStackTrace();
		} finally {

			// 5. close the connections
			close(myConnection, myStatement, myResult);
		}

		return theStudent;
	}

	public void updateStudent(Student theStudent) throws Exception {

		// 1. get db connection
		Connection myConnection = null;
		PreparedStatement myStatement = null;

		try {
			myConnection = dataSource.getConnection();

			// 2. create SQL insert
			String sql = "UPDATE student SET first_name=?, last_name=?, email=? WHERE id=?";

			myStatement = myConnection.prepareStatement(sql);

			// 3. Set the parameter values for student
			myStatement.setString(1, theStudent.getFirstName());
			myStatement.setString(2, theStudent.getLastName());
			myStatement.setString(3, theStudent.getEmail());
			myStatement.setInt(4, theStudent.getId());

			// 4. execute SQL insert
			myStatement.execute();

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			// 5. close connections
			close(myConnection, myStatement, null);
		}

	}

	public void deleteStudent(int studentId) throws Exception {

		// 1. get db connection
		Connection myConnection = null;
		PreparedStatement myStatement = null;

		try {
			myConnection = dataSource.getConnection();

			// 2. create SQL insert
			String sql = "DELETE FROM student WHERE id=?";

			myStatement = myConnection.prepareStatement(sql);

			// 3. Set the parameter values for student
			myStatement.setInt(1, studentId);

			// 4. execute SQL insert
			myStatement.execute();

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			// 5. close connections
			close(myConnection, myStatement, null);
		}

	}
}