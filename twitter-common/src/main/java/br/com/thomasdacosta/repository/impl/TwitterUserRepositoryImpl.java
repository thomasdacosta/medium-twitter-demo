package br.com.thomasdacosta.repository.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import br.com.thomasdacosta.config.RedisConfiguration;
import br.com.thomasdacosta.exception.NotFoundException;
import br.com.thomasdacosta.exception.RedisOperationException;
import br.com.thomasdacosta.repository.TwitterUserRepository;
import lombok.Getter;
import lombok.Setter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

@Repository
public class TwitterUserRepositoryImpl implements TwitterUserRepository {

	private static final Logger logger = LoggerFactory.getLogger(TwitterUserRepositoryImpl.class);

	@Getter @Setter
	private HashOperations<String, String, Integer> hashOperations;

	@Autowired @Getter @Setter
	private RedisTemplate<String, String> redisTemplate;

	@Autowired @Getter @Setter
	private RedisConfiguration redisConfiguration;

	@Value("${map.user}") @Getter @Setter
	private String mapUser;
	
	@Autowired
	public TwitterUserRepositoryImpl(RedisTemplate<String, String> redisUserTemplate) {
		this.redisTemplate = redisUserTemplate;
	}

	@PostConstruct
	private void init() {
		hashOperations = redisTemplate.opsForHash();
	}

	@Override
	public Optional<Integer> getHash(String key, String hashKey) {
		HashOperations<String, String, Integer> hashOperations = getHashOperations();
		return Optional.ofNullable(hashOperations.get(key, hashKey));
	}

	@Override
	public void insertUser(String hashTag, Map<String, Integer> values) {
		String hash = mapUser + "_" + hashTag;

		try (Jedis jedis = redisConfiguration.getConnection(); Pipeline pipeline = jedis.pipelined()) {
			values.forEach((k, v) -> {
				pipeline.hset(hash, k.toLowerCase(), String.valueOf(v));
			});
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new RedisOperationException(ex.getMessage(), ex);
		}
	}
	
	@Override
	public Map<String, Integer> findByHashTag(String hashTag) {
		Map<String, Integer> values = new HashMap<>();
		try {
			Map<String, Integer> allEntries = hashOperations.entries(mapUser + "_" + hashTag);
			Map<String, Integer> entries = new HashMap<>();

			for (String key : allEntries.keySet()) {
				entries.put(key, Integer.parseInt("" + allEntries.get(key)));
			}

			if (entries.isEmpty()) {
				throw new NotFoundException("Empty Database");
			} else {
				values = entries.entrySet().stream()
						.sorted(Collections.reverseOrder(Map.Entry.<String, Integer>comparingByValue())).limit(5)
						.collect(Collectors.toMap(Map.Entry<String, Integer>::getKey,
								Map.Entry<String, Integer>::getValue, (oldValue, newValue) -> oldValue,
								LinkedHashMap::new));
			}
		} catch (NotFoundException ex) {
			throw ex;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new RedisOperationException(ex.getMessage(), ex);
		}
		return values;
	}

}
