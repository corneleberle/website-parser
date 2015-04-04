package ch.coeb.websiteparser.exception;

public class ServiceLayerException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ServiceLayerException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
