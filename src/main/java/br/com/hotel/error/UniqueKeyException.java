package br.com.hotel.error;

public class UniqueKeyException extends Exception {

	private static final long serialVersionUID = 1L;

	public UniqueKeyException(String message) {
		super(message);
	}
}
