package ru.hse.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {

	public static void main( String[] args ) throws SQLException, SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InstantiationException {
		Connection conn = DriverManager.getConnection( "jdbc:hsqldb:mem:db1" );

		Table<User> users = new Table<User>( conn, User.class );
		Table<Address> addresses = new Table<Address>( conn, Address.class );

		User u1 = new User();
		u1.login = "tester";
		u1.passwordSalt = "rhbfd";
		u1.setPassword( "qwerty" );
		users.save( u1 );

		System.out.println( users.findAll() );

		System.out.println( "abcd: " + u1.checkPassword( "abcd" ) );
		System.out.println( "qwerty: " + u1.checkPassword( "qwerty" ) );

		Address a = new Address();
		a.street = "Gagarina";
		a.number = 11;
		addresses.save( a );

		System.out.println( addresses.findAll() );

		a.number = 12;

		addresses.save( a );

		System.out.println( addresses.findAll() );
	}

}