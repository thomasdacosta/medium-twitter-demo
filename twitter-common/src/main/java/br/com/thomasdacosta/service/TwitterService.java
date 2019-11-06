package br.com.thomasdacosta.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.thomasdacosta.repository.TwitterUserRepository;
import br.com.thomasdacosta.repository.TwitterRepository;
import twitter4j.Status;
import twitter4j.User;

@Service
public class TwitterService {

	private static final Logger logger = LoggerFactory.getLogger(TwitterService.class);

	@Autowired
	private TwitterRepository twitterRepository;

	@Autowired
	private TwitterUserRepository twitterUserUserRepository;

	public void findByHashTag(String hashTag) {
		List<Status> tweets = twitterRepository.findByHashTag(hashTag);
		logger.info("#### Total Tweets: {} - HashTag: {}", tweets.size(), hashTag);

		if (!tweets.isEmpty()) {
			Map<String, Integer> values = tweets.stream().map(Status::getUser).distinct()
					.collect(Collectors.toMap(User::getScreenName, User::getFollowersCount));

			logger.info("#### Starting Insert Redis");
			synchronized (this) {
				twitterUserUserRepository.insertUser(hashTag.replaceAll("\\#", ""), values);
			}
			logger.info("#### Completed Insert Redis");
		} else {
			logger.info("#### Tweets Not Found - {}" + hashTag);
		}
	}

}
