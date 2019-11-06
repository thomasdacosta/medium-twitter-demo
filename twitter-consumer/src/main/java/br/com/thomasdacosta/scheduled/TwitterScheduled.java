package br.com.thomasdacosta.scheduled;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.com.thomasdacosta.exception.TwitterOperationException;
import br.com.thomasdacosta.service.TwitterService;

@Component
@Configuration
public class TwitterScheduled {

	private static final Logger logger = LoggerFactory.getLogger(TwitterScheduled.class);

	@Autowired
	private TwitterService twitterService;

	@Value("${hashTag}")
	private String[] hashTags;

	@PostConstruct
	public void init() {
		if (ArrayUtils.isEmpty(hashTags)) {
			System.exit(-10);
			logger.error("#### HashTag is empty");
			throw new TwitterOperationException("HashTag is empty");
		}
	}

	@Scheduled(fixedRate = 60000)
	public void getTweets() {
		if (hashTags.length == 1)
			twitterService.findByHashTag(hashTags[0]);
		else {
			for (String hashTag : hashTags) {
				twitterService.findByHashTag(hashTag);
			}
		}
	}

}
