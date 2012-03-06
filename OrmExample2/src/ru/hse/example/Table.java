package ru.hse.example;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import java.lang.reflect.Field;

public class Table<T> {

	private final PreparedStatement insertSt, identitySt, updateSt, findByIdSt, findAllSt;

	private final Class<T> clazz;
	private final Field[] fields;
	private final Field idField;

	public Table( Connection conn, Class<T> clazz ) throws SQLException, SecurityException, NoSuchFieldException {
		this.clazz = clazz;
		this.fields = new Field[clazz.getFields().length - 1];
		this.idField = clazz.getField( "id" );

		for ( int i = 0, j = 0; i < clazz.getFields().length; ++i ) {
			if ( clazz.getFields()[i].getName() != "id" )
				fields[j++] = clazz.getFields()[i];
		}

		Statement st = conn.createStatement();

		try {
			st.execute( generateCreateTable() );
		} finally {
			st.close();
		}

		this.insertSt = conn.prepareStatement( genInsert() );
		this.updateSt = conn.prepareStatement( genUpdate() );
		this.identitySt = conn.prepareStatement( "CALL IDENTITY()" );
		this.findByIdSt = conn.prepareStatement( genFindById() );
		this.findAllSt = conn.prepareStatement( genFindAll() );
	}

	private String genFindAll() {
		StringBuilder b = new StringBuilder();
		b.append( "SELECT id" );

		for ( Field field : fields ) {
			b.append( ", " );
			b.append( field.getName() );
		}

		b.append( " FROM " );
		b.append( clazz.getSimpleName() );

		return b.toString();
	}

	private String genFindById() {
		StringBuilder b = new StringBuilder();
		b.append( "SELECT id" );

		for ( Field field : fields ) {
			b.append( ", " );
			b.append( field.getName() );
		}

		b.append( " FROM " );
		b.append( clazz.getSimpleName() );
		b.append( " WHERE id = ?" );

		return b.toString();
	}

	private String genUpdate() {
		StringBuilder b = new StringBuilder();
		b.append( "UPDATE " );
		b.append( clazz.getSimpleName() );
		b.append( " SET " );

		boolean first = true;
		for ( Field field : fields ) {
			if ( !first )
				b.append( ", " );

			b.append( field.getName() );
			b.append( " = ?" );

			first = false;
		}

		b.append( " WHERE id = ?" );

		return b.toString();
	}

	private String genInsert() {
		StringBuilder b = new StringBuilder();
		b.append( "INSERT INTO " );
		b.append( clazz.getSimpleName() );
		b.append( "(" );

		boolean first = true;
		for ( Field field : fields ) {
			if ( !first )
				b.append( ", " );

			b.append( field.getName() );

			first = false;
		}

		b.append( ") VALUES (" );
		first = true;
		for ( Field field : fields ) {
			if ( !first )
				b.append( ", " );

			b.append( "?" );

			first = false;
		}

		b.append( ")" );

		return b.toString();
	}

	private String generateCreateTable() {
		StringBuilder b = new StringBuilder();

		b.append( "CREATE TABLE " );
		b.append( clazz.getSimpleName() );
		b.append( "(" );

		b.append( "id " );
		b.append( genFieldDesc( idField ) );

		for ( Field field : fields ) {
			b.append( ", " );
			b.append( field.getName() );
			b.append( " " );
			b.append( genFieldDesc( field ) );
		}

		b.append( ")" );
		return b.toString();
	}

	private Object genFieldDesc( Field field ) {
		String type = null;

		if ( field.getType() == String.class ) {
			type = "VARCHAR(100)";
		} else if ( field.getType() == Integer.class || field.getType() == Integer.TYPE ) {
			type = "INTEGER";
		} else {
			throw new RuntimeException( "Unsupported field type " + field.getType() );
		}

		return (field.getName().equals( "id" )) ? type + " IDENTITY" : type;
	}

	public void close() throws SQLException {
		insertSt.close();
		identitySt.close();
		updateSt.close();
		findByIdSt.close();
		findAllSt.close();
	}

	public void save( T u ) throws SQLException, IllegalArgumentException, IllegalAccessException {
		if ( idField.get( u ) == null ) {
			insert( u );
		} else {
			update( u );
		}
	}

	public void update( T u ) throws SQLException, IllegalArgumentException, IllegalAccessException {
		for ( int i = 0; i < fields.length; ++i )
			updateSt.setObject( i + 1, fields[i].get( u ) );
		updateSt.setObject( fields.length + 1, idField.get( u ) );

		updateSt.execute();
	}

	public void insert( T u ) throws SQLException, IllegalArgumentException, IllegalAccessException {
		for ( int i = 0; i < fields.length; ++i )
			insertSt.setObject( i + 1, fields[i].get( u ) );
		insertSt.execute();

		idField.set( u, getInsertedId() );
	}

	public T findById( int id ) throws SQLException, InstantiationException, IllegalAccessException {
		findByIdSt.setObject( 1, id );
		ResultSet rs = findByIdSt.executeQuery();

		try {
			if ( !rs.next() )
				return null;

			return buildT( rs );
		} finally {
			rs.close();
		}
	}

	public List<T> findAll() throws SQLException, InstantiationException, IllegalAccessException {
		ResultSet rs = findAllSt.executeQuery();

		try {
			ArrayList<T> results = new ArrayList<T>();

			while ( rs.next() )
				results.add( buildT( rs ) );

			return results;
		} finally {
			rs.close();
		}
	}

	private T buildT( ResultSet rs ) throws SQLException, InstantiationException, IllegalAccessException {
		T u = clazz.newInstance();

		idField.set( u, rs.getObject( 1 ) );

		for ( int i = 0; i < fields.length; ++i )
			fields[i].set( u, rs.getObject( 2 + i ) );

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
