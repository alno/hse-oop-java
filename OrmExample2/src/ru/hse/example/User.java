package ru.hse.example;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User {

	public Integer id;
	public String login;
	public String passwordSalt, passwordHash;

	public void setPassword( String password ) {
		this.passwordHash = encrypt( password );
	}

	private String encrypt( String password ) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance( "SHA1" );
		} catch ( NoSuchAlgorithmException e ) {
			throw new RuntimeException( "SHA1 implementation not found", e );
		}

		byte[] bytes = md.digest( (password + "." + passwordSalt).getBytes() );
		StringBuilder builder = new StringBuilder();

		for ( byte b : bytes ) {
			if ( b < 16 )
				builder.append( '0' );
			builder.append( Integer.toHexString( b & 0xFF ) );
		}

		return builder.toString();
	}

	public boolean checkPassword( String password ) {
		return passwordHash.equals( encrypt( password ) );
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", login=" + login + ", passwordSalt=" + passwordSalt + ", passwordHash=" + passwordHash + "]";
	}

}
