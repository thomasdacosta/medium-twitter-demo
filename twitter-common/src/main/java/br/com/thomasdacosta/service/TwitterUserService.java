package br.com.thomasdacosta.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.thomasdacosta.dto.TwitterUser;
import br.com.thomasdacosta.repository.TwitterUserRepository;

@Service
public class TwitterUserService {

	@Autowired
	private TwitterUserRepository repository;

	public List<TwitterUser> findByHashTag(String hashTag) {
		Map<String, Integer> values = repository.findByHashTag(hashTag);
		
		AtomicLong atomicLong = new AtomicLong(0);
		
		return values.entrySet().stream().map(v -> {
			TwitterUser summary = new TwitterUser();
			summary.setUser(v.getKey());
			summary.setFollowers("" + v.getValue());
			summary.setHashtag(hashTag);
			summary.setRanking("" + atomicLong.incrementAndGet());
			return summary;
		}).collect(Collectors.toList());
	}

}
