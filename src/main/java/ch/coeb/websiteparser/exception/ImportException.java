package ch.coeb.websiteparser.exception;

public class ImportException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ImportException(String message, Throwable t) {
		super(message, t);
	}
}
