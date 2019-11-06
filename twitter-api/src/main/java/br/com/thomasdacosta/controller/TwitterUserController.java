package br.com.thomasdacosta.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.thomasdacosta.dto.TwitterUser;
import br.com.thomasdacosta.service.TwitterUserService;

@RestController
@RequestMapping("/")
public class TwitterUserController {
	
	@Autowired
	private TwitterUserService service; 

	@GetMapping(value = "/users/{hashTag}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<TwitterUser>> find(@PathVariable(value = "hashTag", required = true) String hashTag) {
		return ResponseEntity.ok(service.findByHashTag(hashTag));
	}

}
