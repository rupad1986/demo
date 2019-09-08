package com.example.demo.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.Search;

@RestController
@RequestMapping("/youtube")
public class YouTubeResource {
	
	@Autowired
	private Search search;

	@RequestMapping("/details")
	public ResponseEntity<String> getTelecomeDetails() {
		search.retrieveDetails();
		return new ResponseEntity<>("success", HttpStatus.OK);
	}

	
	
}
