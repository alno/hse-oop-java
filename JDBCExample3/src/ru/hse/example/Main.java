package ru.hse.example;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {

	/**
	 * @param args
	 * @throws SQLException
	 */
	public static void main( String[] args ) throws SQLException {
		Connection c = DriverManager.getConnection( "jdbc:hsqldb:mem:db-bank" );

		try {
			prepareDb( c );
			fillDb( c );
			workWithDb( c );
		} finally {
			c.close();
		}
	}

	private static void prepareDb( Connection c ) throws SQLException {
		Statement s = c.createStatement();

		try {
			s.execute( "CREATE TABLE companies (id INTEGER IDENTITY, name VARCHAR(100) NOT NULL)" );
			s.execute( "CREATE TABLE clients (id INTEGER IDENTITY, name VARCHAR(100) NOT NULL, company_id INTEGER REFERENCES companies(id) ON DELETE RESTRICT)" );
			s.execute( "CREATE TABLE accounts (id INTEGER IDENTITY, balance INTEGER DEFAULT 0 NOT NULL CHECK(balance >= 0), client_id INTEGER NOT NULL REFERENCES clients(id) ON DELETE RESTRICT)" );
		} finally {
			s.close();
		}
	}

	private static void fillDb( Connection c ) throws SQLException {
		Statement s = c.createStatement();

		try {
			s.execute( "INSERT INTO companies(id,name) VALUES (1, 'COmpanyA'), (2, 'COmpanyB'), (3, 'CompanyC')" );
			s.execute( "INSERT INTO clients(id,company_id,name) VALUES (1, 1, 'Alex'), (2, 1, 'Ivan'), (3, 2, 'DMitriy'), (4,2, 'Tester'), (5,NULL,'Tester2')" );
			s.execute( "INSERT INTO accounts(client_id, balance) VALUES (1, 100), (2, 100), (3, 100), (3, 100), (5,100)" );
			s.execute( "SET AUTOCOMMIT FALSE" );
		} finally {
			s.close();
		}
	}

	private static void workWithDb( Connection c ) throws SQLException {
		Statement s = c.createStatement();

		printAccounts( c, 3 );

		try {
			transfer( c, 2, 3, 50 );

			s.execute( "DELETE FROM accounts WHERE balance = 0" );
			s.execute( "COMMIT" );

		} finally {
			s.close();
		}

		printAccounts( c, 3 );
		printAccountStats( c, 3 );
		printAccountStats2( c, 3 );

		System.out.println( "Success" );
	}

	private static void transfer( Connection c, int from, int to, int amount ) throws SQLException {
		PreparedStatement ps = c.prepareStatement( "UPDATE accounts SET balance = balance + ? WHERE id = ?" );

		try {
			ps.setInt( 1, amount );
			ps.setInt( 2, to );
			ps.execute();
			ps.setInt( 1, -amount );
			ps.setInt( 2, from );
			ps.execute();
			c.commit();
		} catch ( SQLException e ) {
			c.rollback();
			System.err.println( "Some error: " + e.getMessage() );
		} finally {
			ps.close();
		}
	}

	private static void printAccounts( Connection c, int clientId ) throws SQLException {
		PreparedStatement s = c.prepareStatement( "SELECT id, balance FROM accounts WHERE client_id = ?" );

		try {
			s.setInt( 1, clientId );
			ResultSet r = s.executeQuery();

			while ( r.next() )
				System.out.println( r.getInt( 1 ) + ": " + r.getObject( 2 ) );
		} finally {
			s.close();
		}
	}

	private static void printAccountStats( Connection c, int clientId ) throws SQLException {
		PreparedStatement s = c.prepareStatement( "SELECT SUM(COALESCE(balance,0)) AS s, AVG(balance), clients.id, clients.name, companies.name " + "FROM clients "
				+ "LEFT JOIN accounts ON clients.id = accounts.client_id " + "LEFT JOIN companies ON companies.id = clients.company_id "
				+ "GROUP BY clients.id, clients.name, companies.name HAVING AVG(COALESCE(balance,0)) >= (SELECT AVG(balance) FROM accounts) ORDER BY s DESC" );

		try {
			ResultSet r = s.executeQuery();

			while ( r.next() )
				System.out.println( r.getObject( 1 ) + "," + r.getObject( 2 ) + "," + r.getObject( 3 ) + "," + r.getObject( 4 ) + "," + r.getObject( 5 ) );
		} finally {
			s.close();
		}
	}

	private static void printAccountStats2( Connection c, int clientId ) throws SQLException {
		PreparedStatement s = c
				.prepareStatement( "SELECT COALESCE((SELECT SUM(balance) FROM accounts WHERE client_id = clients.id), 0) AS s, clients.name FROM clients ORDER BY s DESC" );

		try {
			ResultSet r = s.executeQuery();

			while ( r.next() )
				System.out.println( r.getObject( 1 ) + "," + r.getObject( 2 ) );
		} finally {
			s.close();
		}
	}
}
