package me.apemanzilla.krist.api.exceptions;

/**
 * Thrown when there is an error with Krist credentials
 * @author apemanzilla
 *
 */
public class KristCredentialsException extends RuntimeException {

	private static final long serialVersionUID = 1834793142378144394L;

	public KristCredentialsException() {
		super();
	}

	public KristCredentialsException(String message) {
		super(message);
	}

	public KristCredentialsException(Throwable cause) {
		super(cause);
	}

	public KristCredentialsException(String message, Throwable cause) {
		super(message, cause);
	}

	public KristCredentialsException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
