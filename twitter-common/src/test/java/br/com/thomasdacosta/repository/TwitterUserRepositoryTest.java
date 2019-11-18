package br.com.thomasdacosta.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import br.com.thomasdacosta.config.RedisConfiguration;
import br.com.thomasdacosta.exception.NotFoundException;
import br.com.thomasdacosta.repository.impl.TwitterUserRepositoryImpl;

@RunWith(MockitoJUnitRunner.class)
public class TwitterUserRepositoryTest {
	
	private TwitterUserRepositoryImpl repository;
	
	@Mock
	private HashOperations<String, String, Integer> hashOperations;

	@Mock
	private RedisTemplate<String, String> redisTemplate;

	@Mock
	private RedisConfiguration redisConfiguration;

	private String mapUser = "twitter-users";
	
	private Map<String, Integer> hash = new HashMap<>();

	@Before
	public void setup() {
		repository = new TwitterUserRepositoryImpl(redisTemplate);
		repository.setHashOperations(hashOperations);
		repository.setRedisConfiguration(redisConfiguration);
		repository.setRedisTemplate(redisTemplate);
		repository.setMapUser(mapUser);
		
		hash.put("dsscreenshot", 7145);
		hash.put("netec", 10031);
		hash.put("keshavbeniwal2", 52448);
		when(hashOperations.entries(contains(mapUser + "_microsoft"))).thenReturn(hash);
		
		when(hashOperations.get(anyString(), anyString())).thenReturn(1);
	}
	
	@Test
	public void testFindByHashTag() {
		Map<String, Integer> result = repository.findByHashTag("microsoft");
		
		String[] values = result.keySet().toArray(new String[result.size()]);
		
		assertEquals(values[0], "keshavbeniwal2");
		assertEquals(values[1], "netec");
		assertEquals(values[2], "dsscreenshot");
	}
	
	@Test(expected=NotFoundException.class)
	public void testFindByHashTag_NotFound() {
		when(hashOperations.entries(anyString())).thenReturn(new HashMap<>());
		repository.findByHashTag("aws");
	}
	
	@Test(expected=Exception.class)
	public void testFindByHashTag_Exception() {
		when(hashOperations.entries(anyString())).thenReturn(null);
		repository.findByHashTag("aws");
	}
	
	@Test
	public void testHash() {
		Optional<Integer> result = repository.getHash("", "");
		assertTrue(result.get() > 0);
	}


}
