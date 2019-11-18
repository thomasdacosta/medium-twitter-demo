package br.com.thomasdacosta.configuration;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import br.com.thomasdacosta.dto.TwitterUserError;

public class TwitterUserErrorTest {
	
	@Test
	public void testErrors() {
		assertNotNull(TwitterUserError.notFound(new Exception()));
		assertNotNull(TwitterUserError.failedPrecondition(new Exception()));
		assertNotNull(TwitterUserError.internalServerError(new Exception()));
	}

}
