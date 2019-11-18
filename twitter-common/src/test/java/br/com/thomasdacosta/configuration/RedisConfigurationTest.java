package br.com.thomasdacosta.configuration;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import br.com.thomasdacosta.config.RedisConfiguration;
import redis.clients.jedis.JedisPoolConfig;

public class RedisConfigurationTest {
	
	private RedisConfiguration config = null;
	
	private JedisPoolConfig poolConfig = null;
	
	private JedisConnectionFactory connFactory = null;
	
	@Before
	public void setup() {
		if (config == null) {
			config = new RedisConfiguration();
			config.setRedisSsl(false);
			config.setHost("localhost");
			config.setPassword("twiiterdemo");
			config.setPort(6379);
		}
	}
	
	@Test
	public void testRedisConfiguration() {
		poolConfig = config.getPoolConfig();
		assertNotNull(poolConfig);
		
		connFactory = config.getRedisConnectionFactory(poolConfig);
		assertNotNull(connFactory);
		
		assertNotNull(config.getStringRedisTemplate(connFactory));
	}

}
