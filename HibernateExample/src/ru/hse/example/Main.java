package ru.hse.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;

import ru.hse.example.model.Course;
import ru.hse.example.model.Student;
import ru.hse.example.model.Teacher;

public class Main {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		Configuration config = new Configuration().configure();
		SessionFactory sessionFactory = config.buildSessionFactory();
		Session session = sessionFactory.openSession();

		session.beginTransaction();

		Student student = new Student();
		student.setFirstName("Ivan");
		student.setLastName("Ivanov");
		student.setAge(19);

		session.save(student);

		Student student2 = new Student();
		student2.setFirstName("DMitriy");
		student2.setLastName("DMitriyev");
		student2.setAge(21);

		session.save(student2);
		
		Teacher teacher = new Teacher();
		teacher.setFirstName("Name");
		teacher.setLastName("Tester");
		
		session.save(teacher);
		
		Course course1 = new Course();
		course1.setName("C1");
		course1.setTeacher(teacher);
		
		session.save(course1);
		
		Course course2 = new Course();
		course2.setName("C2");
		
		session.save(course2);
		
		course1.getStudents().add(student);
		
		session.update(student);

		session.getTransaction().commit();
		session.clear();

		Teacher t = ((Teacher) session.byId(Teacher.class).load(1));
		
		System.out.println(t + ": " + t.getCourses());

		System.out.println(session.createCriteria(Student.class)
				.add(Restrictions.gt("age", 20)).list());
	}

}

