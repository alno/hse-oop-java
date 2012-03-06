package ru.hse.example;

public class Address {

	public Integer id;
	public String street;
	public int number;

	@Override
	public String toString() {
		return "Address [id=" + id + ", street=" + street + ", number=" + number + "]";
	}

}
