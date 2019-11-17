package br.com.thomasdacosta.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import br.com.thomasdacosta.exception.TwitterOperationException;
import br.com.thomasdacosta.repository.TwitterRepository;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;

@Repository
public class TwitterRepositoryImpl implements TwitterRepository {
	
	private static final Logger logger = LoggerFactory.getLogger(TwitterRepositoryImpl.class);
	
	private static final Integer LIMIT_TWEETS = 100;
	private static Twitter twitter = TwitterFactory.getSingleton();
	
	private Query query = null;
	private QueryResult queryResult = null;
	
	@Override
	public List<Status> findByHashTag(String hashTag) {
		List<Status> tweets = new ArrayList<>();
		
		try {
			query = new Query(hashTag);
			query.count(LIMIT_TWEETS);
			
			queryResult = twitter.search(query);
			tweets = queryResult.getTweets();
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new TwitterOperationException(ex.getMessage(), ex);
		}
		return tweets;
	}

}
