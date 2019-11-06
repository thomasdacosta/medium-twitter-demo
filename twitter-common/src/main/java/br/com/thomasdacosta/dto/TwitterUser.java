package br.com.thomasdacosta.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
public class TwitterUser {

	@Getter @Setter
	private String user;
	
	@Getter @Setter
	private String followers;
	
	@Getter @Setter
	private String ranking;
	
	@Getter @Setter
	private String hashtag;

}
