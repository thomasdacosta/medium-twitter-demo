package br.com.thomasdacosta.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import br.com.thomasdacosta.exception.TwitterOperationException;
import br.com.thomasdacosta.repository.impl.TwitterRepositoryImpl;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

@RunWith(MockitoJUnitRunner.Silent.class)
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
	public void setup() {
		try {
			statuses = new ArrayList<>();
			when(status.getUser()).thenReturn(user);
			when(user.getScreenName()).thenReturn("microsoft");
			statuses.add(status);
			
			TwitterRepositoryImpl.setTwitter(twitter);
			when(twitter.search(query)).thenReturn(queryResult);
			when(queryResult.getTweets()).thenReturn(statuses);
			when(repository.findByHashTag("#azure")).thenReturn(statuses);
		} catch (TwitterException ex) {
			fail(ex.getMessage());
		}
	}
	
	@Test
	public void testFindByHashTag() {
		List<Status> listStatus = repository.findByHashTag("#azure");
		assertEquals(listStatus.get(0).getUser().getScreenName(), "microsoft");
		assertEquals(listStatus.size(), 1);
	}
	
	@Test(expected=TwitterOperationException.class)
	public void testFindByHashTag_Exception() {
		when(repository.findByHashTag("#aws")).thenThrow(TwitterOperationException.class);
		repository.findByHashTag("#aws");
	}
	

}
