package ru.hse.example;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Table {

	private final PreparedStatement insertSt, identitySt, updateSt, findByIdSt, findAllSt;

	public Table( Connection conn ) throws SQLException {
		Statement st = conn.createStatement();

		try {
			st.execute( "CREATE TABLE User(id INTEGER IDENTITY, login VARCHAR(100), passwordHash VARCHAR(100), passwordSalt VARCHAR(100))" );
		} finally {
			st.close();
		}

		this.insertSt = conn.prepareStatement( "INSERT INTO User(login,passwordHash,passwordSalt) VALUES (?,?,?)" );
		this.updateSt = conn.prepareStatement( "UPDATE User SET login = ?, passwordHash = ?, passwordSalt = ? WHERE id = ?" );
		this.identitySt = conn.prepareStatement( "CALL IDENTITY()" );
		this.findByIdSt = conn.prepareStatement( "SELECT id, login, passwordHash, passwordSalt from User where id = ?" );
		this.findAllSt = conn.prepareStatement( "SELECT id, login, passwordHash, passwordSalt from User" );
	}

	public void close() throws SQLException {
		insertSt.close();
		identitySt.close();
		updateSt.close();
		findByIdSt.close();
		findAllSt.close();
	}

	public void save( User u ) throws SQLException {
		if ( u.id == null ) {
			insert( u );
		} else {
			update( u );
		}
	}

	public void update( User u ) throws SQLException {
		updateSt.setObject( 1, u.login );
		updateSt.setObject( 2, u.passwordHash );
		updateSt.setObject( 3, u.passwordSalt );
		updateSt.setObject( 4, u.id );
		updateSt.execute();
	}

	public void insert( User u ) throws SQLException {
		insertSt.setObject( 1, u.login );
		insertSt.setObject( 2, u.passwordHash );
		insertSt.setObject( 3, u.passwordSalt );
		insertSt.execute();

		u.id = getInsertedId();
	}

	public User findById( int id ) throws SQLException {
		findByIdSt.setObject( 1, id );
		ResultSet rs = findByIdSt.executeQuery();

		try {
			if ( !rs.next() )
				return null;

			return buildUser( rs );
		} finally {
			rs.close();
		}
	}

	public List<User> findAll() throws SQLException {
		ResultSet rs = findAllSt.executeQuery();

		try {
			ArrayList<User> results = new ArrayList<User>();

			while ( rs.next() )
				results.add( buildUser( rs ) );

			return results;
		} finally {
			rs.close();
		}
	}

	private User buildUser( ResultSet rs ) throws SQLException {
		User u = new User();
		u.id = rs.getInt( 1 );
		u.login = rs.getString( 2 );
		u.passwordHash = rs.getString( 3 );
		u.passwordSalt = rs.getString( 4 );

		return u;
	}

	private int getInsertedId() throws SQLException {
		ResultSet rs = identitySt.executeQuery();

		try {
			if ( !rs.next() )
				throw new RuntimeException( "No inserted id in results" );

			return rs.getInt( 1 );
		} finally {
			rs.close();
		}
	}

}
