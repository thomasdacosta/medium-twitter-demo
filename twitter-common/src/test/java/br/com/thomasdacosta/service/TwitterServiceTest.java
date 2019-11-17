package br.com.thomasdacosta.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import br.com.thomasdacosta.repository.impl.TwiiterUserRepositoryImpl;
import br.com.thomasdacosta.repository.impl.TwitterRepositoryImpl;
import twitter4j.Status;
import twitter4j.User;

@RunWith(MockitoJUnitRunner.class)
public class TwitterServiceTest {
	
	private static final String HASHTAG_AZURE = "#azure";
	
	@InjectMocks
	private TwitterService service;
	
	@Mock
	private TwitterRepositoryImpl twitterRepository;
	
	@Mock
	private TwiiterUserRepositoryImpl summaryRepository; 
	
	@Mock
	private Status status;
	
	@Mock
	private User user;
	
    @Rule
    public ErrorCollector collector = new ErrorCollector();

	private List<Status> statuses;
    
    @Before
    public void setup() {
    	statuses = new ArrayList<>();
    	when(status.getUser()).thenReturn(user);
    	when(user.getScreenName()).thenReturn("microsoft");
    	statuses.add(status);
    	when(twitterRepository.findByHashTag(HASHTAG_AZURE)).thenReturn(statuses);
    }

    @Test
    public void testRepositoryFindByHashTag() {
    	List<Status> values = twitterRepository.findByHashTag(HASHTAG_AZURE);
    	collector.checkThat(values.size(), is(equalTo(1)));
    }
    
    @Test
    public void testServiceFindByHashTag() {
    	Map<String, Integer> values = new HashMap<>();
    	values.put("microsoft", 1);
    	service.findByHashTag(HASHTAG_AZURE);
    	
    	verify(twitterRepository, atLeastOnce()).findByHashTag(HASHTAG_AZURE);
    }

}
