package br.com.thomasdacosta.config;

import java.time.Duration;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration.JedisClientConfigurationBuilder;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import br.com.thomasdacosta.exception.RedisOperationException;
import lombok.Data;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

@Configuration
@Data
public class RedisConfiguration {

	@Value("${spring.redis.host}")
	private String host;

	@Value("${spring.redis.password}")
	private String password;

	@Value("${spring.redis.port}")
	private int port;
	
	@Value("${redis.ssl}")
	private Boolean redisSsl;
	
	private JedisPool jedisPool;
	
	public Jedis getConnection() {
		final JedisPoolConfig poolConfig = getPoolConfig();

		try {
			jedisPool = new JedisPool(poolConfig, getHost(), getPort(), Protocol.DEFAULT_TIMEOUT,
					getPassword(), getRedisSsl());

			return jedisPool.getResource();
		} catch (Exception ex) {
			throw new RedisOperationException(ex.getMessage(), ex);
		}
	}

	@Bean
	public JedisPoolConfig getPoolConfig() {
		final JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(128);
		poolConfig.setMaxIdle(128);
		poolConfig.setMinIdle(16);
		poolConfig.setTestOnBorrow(true);
		poolConfig.setTestOnReturn(true);
		poolConfig.setTestWhileIdle(true);
		poolConfig.setMinEvictableIdleTimeMillis(Duration.ofSeconds(60).toMillis());
		poolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofSeconds(30).toMillis());
		poolConfig.setNumTestsPerEvictionRun(3);
		poolConfig.setBlockWhenExhausted(true);

		return poolConfig;
	}

	@Bean
	public JedisConnectionFactory getRedisConnectionFactory(JedisPoolConfig config) {
		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
		redisStandaloneConfiguration.setHostName(host);
		redisStandaloneConfiguration.setPort(port);

		if (!StringUtils.isEmpty(password))
			redisStandaloneConfiguration.setPassword(RedisPassword.of(password));

		JedisClientConfigurationBuilder jedisClientConfiguration = JedisClientConfiguration.builder();
		if (redisSsl)
			jedisClientConfiguration.useSsl();
		
		jedisClientConfiguration.connectTimeout(Duration.ofSeconds(60));
		return new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration.build());
	}

	@Bean
	@Primary
	public RedisTemplate<String, String> getStringRedisTemplate(RedisConnectionFactory cf) {
		final RedisTemplate<String, String> template = new RedisTemplate<>();
		template.setConnectionFactory(cf);
		template.setHashValueSerializer(new GenericToStringSerializer<String>(String.class));
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new GenericToStringSerializer<String>(String.class));
		template.setHashKeySerializer(new StringRedisSerializer());
		template.afterPropertiesSet();
		return template;
	}

}