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
import lombok.Getter;
import lombok.Setter;

@Component
@Configuration
public class TwitterScheduled {

	private static final Logger logger = LoggerFactory.getLogger(TwitterScheduled.class);

	@Autowired
	private TwitterService twitterService;
	
	@Getter @Setter
	private boolean existApp = true;

	@Value("${hashTag}") @Getter @Setter
	private String[] hashTags;

	@PostConstruct
	public void init() {
		if (ArrayUtils.isEmpty(hashTags)) {
			logger.error("#### HashTag is empty");
			if (existApp)
				System.exit(-10);
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
