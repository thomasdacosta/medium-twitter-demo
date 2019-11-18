package br.com.thomasdacosta;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import br.com.thomasdacosta.exception.TwitterOperationException;
import br.com.thomasdacosta.scheduled.TwitterScheduled;
import br.com.thomasdacosta.service.TwitterService;

@RunWith(MockitoJUnitRunner.class)
public class TwitterScheduledTest {

	@InjectMocks
	private TwitterScheduled scheduled;

	@Mock
	private TwitterService service;
	
	@Before
	public void setup() {
		scheduled.setExistApp(false);
	}
	
	@Test(expected=TwitterOperationException.class)
	public void testExitApp() {
		scheduled.init();
	}
	
	@Test
	public void testGetTweets_One() {
		scheduled.setHashTags(new String[] { "microsoft" });
		scheduled.getTweets();
		verify(service, atLeastOnce()).findByHashTag(Mockito.anyString());
	}

	@Test
	public void testGetTweets_Array() {
		scheduled.setHashTags(new String[] { "microsoft", "aws" });
		scheduled.getTweets();
		verify(service, atLeastOnce()).findByHashTag(Mockito.anyString());
	}
	
}
