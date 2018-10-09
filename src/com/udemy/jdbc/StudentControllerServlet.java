package com.udemy.jdbc;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class StudentControllerServlet
 */
@WebServlet("/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private StudentDbUtil studentDbUtil;

	@Resource(name = "jdbc/web_student_tracker")
	private DataSource dataSource;

	@Override
	public void init() throws ServletException {
		super.init();

		try {
			studentDbUtil = new StudentDbUtil(dataSource);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			// read the "command" parameter
			String theCommand = request.getParameter("command");

			// if the command is missing, then default to listing students
			if (theCommand == null) {
				theCommand = "LIST";
			}

			// route to appropriate method
			switch (theCommand) {

			case "LIST":
				listStudents(request, response);
				break;

			case "LOAD":
				loadStudent(request, response);
				break;

			case "UPDATE":
				updateStudent(request, response);
				break;

			case "DELETE":
				deleteStudent(request, response);
				break;

			default:
				listStudents(request, response);
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			// read the "command" parameter
			String theCommand = request.getParameter("command");

			// route to the appropriate method
			switch (theCommand) {

			case "ADD":
				addStudent(request, response);
				break;

			default:
				listStudents(request, response);
			}

		} catch (Exception exc) {
			throw new ServletException(exc);
		}

	}

	private void listStudents(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// get students from db util
		List<Student> students = studentDbUtil.getStudents();

		// add students to the request
		request.setAttribute("STUDENT_LIST", students);

		// send to JSP page
		RequestDispatcher dispatcher = request.getRequestDispatcher("/list-students.jsp");
		dispatcher.forward(request, response);
	}

	private void addStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// read form data
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");

		// create student object
		Student theStudent = new Student(firstName, lastName, email);

		// insert into database
		studentDbUtil.addStudent(theStudent);

		// list the students
		response.sendRedirect(request.getContextPath() + "/StudentControllerServlet?command=LIST");

	}

	private void loadStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// read the student ID
		String studentId = request.getParameter("studentId");

		// get student from db util
		Student theStudent = studentDbUtil.getStudent(studentId);

		// add student to request attribute
		request.setAttribute("THE_STUDENT", theStudent);

		// send to JSP page
		RequestDispatcher dispatcher = request.getRequestDispatcher("/update-student-form.jsp");
		dispatcher.forward(request, response);

	}

	private void updateStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// read the student ID
		int studentId = Integer.parseInt(request.getParameter("studentId"));

		// read form data
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");

		// create new student object
		Student theStudent = new Student(firstName, lastName, email, studentId);

		// update student in data base
		studentDbUtil.updateStudent(theStudent);

		// list the students
		listStudents(request, response);

	}

	private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// read the student ID
		int studentId = Integer.parseInt(request.getParameter("studentId"));

		// delete student in data base
		studentDbUtil.deleteStudent(studentId);

		// list the students
		listStudents(request, response);

	}
}