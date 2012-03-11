package ru.hse.example.model;

import java.util.Set;
import java.util.HashSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table( name = "teachers" )
public class Teacher {

	private int id;

	private String firstName;

	private String lastName;

	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}

	public void setId( int id ) {
		this.id = id;
	}

	@Column( name = "first_name" )
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName( String firstName ) {
		this.firstName = firstName;
	}

	@Column( name = "last_name" )
	public String getLastName() {
		return lastName;
	}

	public void setLastName( String lastName ) {
		this.lastName = lastName;
	}

	private Set<Course> courses = new HashSet<Course>();

	@OneToMany( mappedBy = "teacher" )
	public Set<Course> getCourses() {
		return courses;
	}

	public void setCourses( Set<Course> courses ) {
		this.courses = courses;
	}

}
