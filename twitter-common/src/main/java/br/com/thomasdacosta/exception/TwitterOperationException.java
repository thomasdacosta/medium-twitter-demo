package br.com.thomasdacosta.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code=HttpStatus.INTERNAL_SERVER_ERROR)
public class TwitterOperationException extends RuntimeException {

	private static final long serialVersionUID = -8200822991694106562L;

	public TwitterOperationException(String message) {
		super(message);
	}

	public TwitterOperationException(Throwable cause) {
		super(cause);
	}

	public TwitterOperationException(String message, Throwable cause) {
		super(message, cause);
	}

	public TwitterOperationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
