package com.example.demo.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

@Component
public class CaptureAndUploadProcessImpl implements CaptureAndUploadProcess {

	@Override
	public String uploadImage(String apiKey, String imageUrl, String uploadUrl) {
		File fileToUpload = new File(imageUrl);

		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			HttpPost httpPost = new HttpPost(uploadUrl);

			// Set the Authorization header with your API key
			String authHeader = "Basic " + java.util.Base64.getEncoder().encodeToString((apiKey + ":").getBytes());
			httpPost.setHeader("Authorization", authHeader);

			// Create a multipart entity with the file, content type, and file name
			HttpEntity entity = MultipartEntityBuilder.create()
					.addBinaryBody("file", fileToUpload, ContentType.create("image/jpg"), fileToUpload.getName())
					.addTextBody("fileName", fileToUpload.getName()) // Add the fileName parameter
					.build();

			httpPost.setEntity(entity);

			// Execute the POST request
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity responseEntity = response.getEntity();

			// Handle the response
			if (responseEntity != null) {
				String responseBody = EntityUtils.toString(responseEntity);
				return responseBody;
			} else {
				System.err.println("No response received from the server.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String performHttpPost(String url, List<NameValuePair> params) {
		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");

			String paramString = URLEncodedUtils.format(params, StandardCharsets.UTF_8);
			httpPost.setEntity(new UrlEncodedFormEntity(params));

			try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
				HttpEntity entity = response.getEntity();
				int statusCode = response.getStatusLine().getStatusCode();
				System.out.println("Response Code: " + statusCode);

				if (entity != null) {
					String responseBody = EntityUtils.toString(entity, StandardCharsets.UTF_8);
					return responseBody;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
