package ru.hse.example;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StudentRepository {

	private Set<Student> allStudents = new HashSet<Student>();
	private Map<String, Set<Student>> studentsByFirstName = new HashMap<String, Set<Student>>();

	public void addStudent(Student student) {
		if ( studentsByFirstName.containsKey(student.firstName)) {
			studentsByFirstName.get(student.firstName).add(student);
		} else {
			Set<Student> students = new HashSet<Student>();
			students.add(student);
			studentsByFirstName.put(student.firstName, students);
		}

		allStudents.add(student);
	}

	public void removeStudent(Student student) {
		allStudents.remove(student);
		studentsByFirstName.get(student.firstName).remove(student);
	}

	public Collection<Student> getStudentsByFirstName(String firstName) {
		Collection<Student> res = studentsByFirstName.get(firstName);

		if ( res == null)
			return Collections.emptySet();
		else
			return res;
	}

	public Collection<Student> allStudents() {
		return allStudents;
	}
}
