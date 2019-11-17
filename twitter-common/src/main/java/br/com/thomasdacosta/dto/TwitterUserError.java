package br.com.thomasdacosta.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
public class TwitterUserError implements Serializable {

	private static final long serialVersionUID = 557098096679648463L;

	@Getter @Setter
	private String message;

	@Setter
	private String detail;

	public static TwitterUserError notFound(Exception ex) {
		return TwitterUserError.create().message("NOT FOUND").detail(ex.getMessage());
	}

	public static TwitterUserError failedPrecondition(Exception ex) {
		return TwitterUserError.create().message("FAILED PRECONDITION").detail(ex.getMessage());
	}

	public static TwitterUserError internalServerError(Exception ex) {
		return TwitterUserError.create().message("INTERNAL SERVER ERROR").detail(ex.getMessage());
	}

	public static TwitterUserError create() {
		return new TwitterUserError();
	}

	public TwitterUserError message(String message) {
		this.setMessage(message);
		return this;
	}

	public TwitterUserError detail(String detail) {
		this.setDetail(detail);
		return this;
	}

	@JsonInclude(Include.NON_NULL)
	public String getDetail() {
		return detail;
	}

}
