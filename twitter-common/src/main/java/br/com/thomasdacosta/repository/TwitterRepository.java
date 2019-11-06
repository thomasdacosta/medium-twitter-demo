package br.com.thomasdacosta.repository;

import java.util.List;

import twitter4j.Status;

public interface TwitterRepository {

	List<Status> findByHashTag(String hashTag);

}