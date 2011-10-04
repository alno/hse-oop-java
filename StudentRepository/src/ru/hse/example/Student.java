package ru.hse.example;

public class Student {

	public String firstName;
	public String lastName;
	public int age;
	public int group;

	public String toString() {
		return "Student{firstName = " + firstName +
				", lastName = " + lastName +
				", age = " + age +
				", group = " + group + "}";
	}

	public int hashCode() {
		int code = 132;

		if (firstName != null)
			code = code * 17 + firstName.hashCode();

		if (lastName != null)
			code = code * 17 + lastName.hashCode();

		code = code * 13 + age;
		code = code * 11 + group;

		return code;
	}

	public boolean equals(Object o) {
		if (o == null) return false;
		if (o == this) return true;
		if (!(o instanceof Student)) return false;

		Student other = (Student) o;

		if (firstName != null) {
			if (!firstName.equals(other.firstName)) return false;
		} else {
			if (other.firstName != null) return false;
		}

		if (lastName != null) {
			if (!lastName.equals(other.lastName)) return false;
		} else {
			if (other.lastName != null) return false;
		}

		if (age != other.age) return false;
		if (group != other.group) return false;

		return true;
	}
}
