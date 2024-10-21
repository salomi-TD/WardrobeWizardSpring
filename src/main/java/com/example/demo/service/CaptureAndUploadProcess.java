package com.example.demo.service;

import java.util.List;


import org.apache.http.NameValuePair;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
@Service
@Component
public interface CaptureAndUploadProcess {

	public String uploadImage(String apiKey, String imageUrl, String uploadUrl);
	
	 public String performHttpPost(String url, List<NameValuePair> params);
}
