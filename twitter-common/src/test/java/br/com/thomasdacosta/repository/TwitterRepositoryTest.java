package br.com.thomasdacosta.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.thomasdacosta.exception.TwitterOperationException;
import br.com.thomasdacosta.repository.impl.TwitterRepositoryImpl;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

public class TwitterRepositoryTest {

	@InjectMocks
	private TwitterRepositoryImpl repository;

	@Mock
	private Query query;

	@Mock
	private QueryResult queryResult;

	@Mock
	private Twitter twitter;

	@Mock
	private Status status;

	@Mock
	private User user;

	private List<Status> statuses;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		TwitterRepositoryImpl.setTwitter(twitter);

		when(status.getUser()).thenReturn(user);
		when(user.getScreenName()).thenReturn("microsoft");
		statuses = Arrays.asList(status);
	}

	@Test
	public void testFindByHashTag() {
		try {
			when(twitter.search(query)).thenReturn(queryResult);
			when(queryResult.getTweets()).thenReturn(statuses);

			List<Status> listStatus = repository.findByHashTag("#azure");
			assertEquals(listStatus.get(0).getUser().getScreenName(), "microsoft");
			assertEquals(listStatus.size(), 1);
			
			assertNotNull(TwitterRepositoryImpl.getTwitter());
		} catch (TwitterException ex) {
			fail(ex.getMessage());
		}
	}

	@Test(expected=TwitterOperationException.class)
	public void testFindByHashTag_Exception() {
		try {
			when(twitter.search(query)).thenThrow(TwitterOperationException.class);
			repository.findByHashTag("#aws");
		} catch (TwitterException ex) {
			fail(ex.getMessage());		
		}
	}
	
}
