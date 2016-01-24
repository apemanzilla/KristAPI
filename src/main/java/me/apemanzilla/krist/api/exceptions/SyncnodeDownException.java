package me.apemanzilla.krist.api.exceptions;

public class SyncnodeDownException extends KristException {

	private static final long serialVersionUID = 583310313356465742L;

	public SyncnodeDownException() {
		super("Go yell at Taras a bit.");
	}

	public SyncnodeDownException(String s) {
		super(s);
	}

}
