package br.com.thomasdacosta.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
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

import br.com.thomasdacosta.dto.TwitterUser;
import br.com.thomasdacosta.repository.impl.TwitterUserRepositoryImpl;

@RunWith(MockitoJUnitRunner.class)
public class TwitterUserServiceTest {
	
	private static final String HASHTAG_AZURE = "#azure";
	private static final String TWITTER_USER = "microsoft";

	@InjectMocks
	private TwitterUserService service;
	
	@Mock
	private TwitterUserRepositoryImpl repository;
	
    @Rule
    public ErrorCollector collector = new ErrorCollector();
    
    private List<TwitterUser> users;
    
    private Map<String, Integer> map;
    
    @Before
    public void setup() {
    	users = new ArrayList<>();
    	map = new HashMap<>();
    	
    	TwitterUser summary = new TwitterUser();
    	summary.setFollowers("1");
    	summary.setHashtag(HASHTAG_AZURE);
    	summary.setRanking("1");
    	summary.setUser(TWITTER_USER);
    	
    	users.add(summary);
    	map.put("microsoft", 1);
    	
    	when(repository.findByHashTag(HASHTAG_AZURE)).thenReturn(map);
    }
    
    @Test
    public void testRespositoryFindSummary() {
    	Map<String, Integer> values = repository.findByHashTag(HASHTAG_AZURE);
    	collector.checkThat(values.get(TWITTER_USER), is(equalTo(1)));
    }
    
    @Test
    public void testServiceFindSummary() {
    	List<TwitterUser> values = service.findByHashTag(HASHTAG_AZURE);
    	collector.checkThat(values.size(), is(equalTo(1)));
    	collector.checkThat(values.get(0).getUser(), is(TWITTER_USER));
    }
    
}
