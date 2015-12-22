package me.apemanzilla.kristapi.exceptions;

@SuppressWarnings("serial")
public class InvalidKristKeyException extends KristException {

	public InvalidKristKeyException() {
		super();
	}
	
	public InvalidKristKeyException(String string) {
		super(string);
	}
	
}