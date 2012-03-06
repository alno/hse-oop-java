package ru.hse.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;

public class Main {

	/**
	 * @param args
	 * @throws SQLException
	 * @throws IOException
	 */
	public static void main( String[] args ) throws SQLException, IOException {
		Connection conn = DriverManager.getConnection( "jdbc:hsqldb:hsql://172.21.19.51/db0" );

		try {
			System.out.println( "Connected" );

			Statement st = conn.createStatement();

			try {
				try {
					st.execute( "CREATE TABLE t1(accessible INTEGER, str VARCHAR(10))" );
					st.execute( "INSERT INTO t1(accessible,str) VALUES(1,'abc')" );
					st.execute( "INSERT INTO t1(accessible,str) VALUES(1,'def')" );
					st.execute( "INSERT INTO t1(accessible,str) VALUES(0,'fgh')" );
				} catch ( SQLSyntaxErrorException e ) {
					if ( e.getMessage().contains( "object name already exists: T" ) )
						System.out.println( "Table exists" );
					else
						throw e;
				}
				work( st );
			} finally {
				st.close();
			}
		} finally {
			conn.close();

		}
	}

	private static void work( Statement st ) throws IOException, SQLException {
		BufferedReader r = new BufferedReader( new InputStreamReader( System.in ) );
		PreparedStatement pst = st.getConnection().prepareStatement( "SELECT str FROM t1 WHERE accessible = 1 AND str > ?" );
		try {
			String s = r.readLine();

			while ( s != null && !s.equals( "q" ) ) {
				String[] parts = s.split( " " );

				if ( parts[0].equals( "insert" ) ) {
					st.execute( "INSERT INTO t1(accessible,str) VALUES(1,'" + parts[1] + "')" );
				} else if ( parts[0].equals( "select" ) ) {
					pst.setString( 1, parts[1] );
					ResultSet rs = pst.executeQuery();
					try {
						while ( rs.next() )
							System.out.println( rs.getString( 1 ) );
					} finally {
						rs.close();
					}
				}

				s = r.readLine();
			}

		} finally {
			pst.close();
		}
	}

}
