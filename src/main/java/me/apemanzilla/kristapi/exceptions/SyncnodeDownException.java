package me.apemanzilla.kristapi.exceptions;

@SuppressWarnings("serial")
public class SyncnodeDownException extends KristException {

	public SyncnodeDownException() {
		super("Krist is down! Go yell at Taras a bit.");
	}

}
