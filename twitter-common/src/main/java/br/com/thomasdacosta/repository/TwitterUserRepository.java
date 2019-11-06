package br.com.thomasdacosta.repository;

import java.util.Map;
import java.util.Optional;

public interface TwitterUserRepository {

	void insertUser(String hashTag, Map<String, Integer> values);

	Map<String, Integer> findByHashTag(String hashTag);

	Optional<Integer> getHash(String key, String hashKey);

}