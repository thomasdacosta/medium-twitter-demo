package br.com.thomasdacosta;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import br.com.thomasdacosta.exception.NotFoundException;
import br.com.thomasdacosta.exception.RedisOperationException;
import br.com.thomasdacosta.exception.TwitterOperationException;

public class ExceptionTest {
	
	@Test
	public void testException() {
		assertNotNull(new NotFoundException(""));
		assertNotNull(new NotFoundException("", new Exception()));
		
		assertNotNull(new RedisOperationException(""));
		assertNotNull(new RedisOperationException("", new Exception()));

		assertNotNull(new TwitterOperationException(""));
		assertNotNull(new TwitterOperationException("", new Exception()));
	}

}
