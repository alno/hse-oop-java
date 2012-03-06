package ru.hse.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
	/**
	 * @param args
	 * @throws SQLException
	 */
	public static void main( String[] args ) throws SQLException {
		Connection conn = DriverManager.getConnection( "jdbc:hsqldb:mem:db1" );

		Table users = new Table( conn );

		User u1 = new User();
		u1.login = "tester";
		u1.passwordSalt = "rhbfd";
		u1.setPassword( "qwerty" );
		users.save( u1 );

		System.out.println( users.findAll() );

		System.out.println( "abcd: " + u1.checkPassword( "abcd" ) );
		System.out.println( "qwerty: " + u1.checkPassword( "qwerty" ) );
	}

}
