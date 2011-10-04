package ru.hse.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class StudentApp {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		StudentRepository rep = new StudentRepository();
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

		while (true) {
			System.out.print("> ");

			String command = in.readLine();

			if (command.equals("add")) {
				addStudent(rep, in);
			} else if (command.equals("list")){
				listStudents(rep, in);
			} else if (command.equals("byfirstname")) {
				listStudentsByFirstName(rep, in);
			} else if (command.equals("remove")) {
				removeStudents(rep, in);
			} else {
				System.out.println("Unknow command '" + command + "'");
			}
		}
	}

	private static void listStudentsByFirstName(StudentRepository rep,
			BufferedReader in) throws IOException {
		String firstName = in.readLine();

		for ( Student s : rep.getStudentsByFirstName(firstName))
			System.out.println(s);
	}

	private static void removeStudents(StudentRepository rep, BufferedReader in) throws IOException {
		Student student = new Student();

		System.out.print("Enter first name: ");
		student.firstName = in.readLine();

		rep.removeStudent(student);

		System.out.println("Student removed");
	}

	private static void listStudents(StudentRepository rep, BufferedReader in) {
		for ( Student student : rep.allStudents() )
			System.out.println(student);
	}

	private static void addStudent(StudentRepository rep, BufferedReader in) throws IOException {
		Student student = new Student();

		System.out.print("Enter first name: ");
		student.firstName = in.readLine();

		rep.addStudent(student);

		System.out.println("Student created");
	}

}
