package com.cognixia.jump.finalJavaProject.ems;

public class InvalidNameException extends Exception{

	/**
	 * This class checks whether a user has entered an  name 
	 */
	private static final long serialVersionUID = 1L;
	
	public InvalidNameException() {
		super("Invalid name, please try again!");
	}
}
