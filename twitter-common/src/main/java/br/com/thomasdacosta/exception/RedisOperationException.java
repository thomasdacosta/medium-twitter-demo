package br.com.thomasdacosta.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code=HttpStatus.INTERNAL_SERVER_ERROR)
public class RedisOperationException extends RuntimeException {

	private static final long serialVersionUID = 6524965167790040099L;

	public RedisOperationException(String message) {
		super(message);
	}

	public RedisOperationException(Throwable cause) {
		super(cause);
	}

	public RedisOperationException(String message, Throwable cause) {
		super(message, cause);
	}

	public RedisOperationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
