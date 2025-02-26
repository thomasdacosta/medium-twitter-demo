package br.com.thomasdacosta.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.thomasdacosta.dto.TwitterUserError;
import br.com.thomasdacosta.exception.NotFoundException;
import br.com.thomasdacosta.exception.RedisOperationException;

@RestControllerAdvice
public class RestControllerHandler extends ResponseEntityExceptionHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(RestControllerHandler.class);
	
	/**
	 * Erro 404 
	 */
	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<Object> handleNotFound(NotFoundException ex) {
		logger.error(ex.getMessage(), ex);
		return new ResponseEntity<>(TwitterUserError.notFound(ex), HttpStatus.NOT_FOUND);
	}
	
	/**
	 * Erro 405
	 */
	@Override
	public ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		logger.error(ex.getMessage(), ex);
		return new ResponseEntity<>(TwitterUserError.failedPrecondition(ex), status);
	}
	
	/**
	 * Erro 500
	 */
	@ExceptionHandler(RedisOperationException.class)
	public ResponseEntity<Object> handleThrowable(final Exception ex) {
		logger.error(ex.getMessage(), ex);
		return new ResponseEntity<>(TwitterUserError.internalServerError(ex), HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
