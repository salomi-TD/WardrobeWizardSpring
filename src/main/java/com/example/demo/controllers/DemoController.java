package com.example.demo.controllers;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.ImageUploadResponse;
import com.example.demo.model.JsonResponse;
import com.example.demo.model.SimilarProduct;
import com.example.demo.service.CaptureAndUploadProcess;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;

import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

@Controller
@RequestMapping("/")
public class DemoController {

	@PostMapping("/logIn")
	@ResponseBody
	public String logIn(@RequestParam(value = "phone", required = false) final String phone) {

		String loginResponse = postRequest(phone);
		return loginResponse;

	}

	@PostMapping("/verify")
	@ResponseBody
	public String verify(@RequestParam(value = "otp", required = false) final String otp) {

		String loginResponse = verifyOTP(otp);
		return loginResponse;

	}

	public String postRequest(String phoneNumber) {
		try {
			configureTrustManager();
			URL url = new URL("https://localhost:9002/occ/v2/electronics/logIn?phone=" + phoneNumber);
			Map<String, String> headers = Map.of("accept", "application/json");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			// Set the HTTP request method to POST
			conn.setRequestMethod("POST");

			// Set the request headers
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				conn.setRequestProperty(entry.getKey(), entry.getValue());
			}

			// Enable input and output streams
			conn.setDoOutput(true);

			// Get the response code
			int responseCode = conn.getResponseCode();

			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String inputLine;
				StringBuilder response = new StringBuilder();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}

				in.close();
				conn.disconnect();

				return response.toString();
			} else {
				throw new RuntimeException("HTTP POST request failed with response code: " + responseCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to make the API call: " + e.getMessage());
		}
	}
	@GetMapping("/history")
	@ResponseBody
	public ResponseEntity<String> getHistory(@RequestParam String phoneNumber) {
	    try {
	        configureTrustManager();
	        URL url = new URL("https://localhost:9002/occ/v2/electronics/history?phone=" + phoneNumber);
	        Map<String, String> headers = Map.of("accept", "application/json");
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

	        // Set the HTTP request method to GET
	        conn.setRequestMethod("GET");

	        // Set the request headers
	        for (Map.Entry<String, String> entry : headers.entrySet()) {
	            conn.setRequestProperty(entry.getKey(), entry.getValue());
	        }

	        // Enable input and output streams
	        conn.setDoOutput(true);

	        // Get the response code
	        int responseCode = conn.getResponseCode();

	        if (responseCode == HttpURLConnection.HTTP_OK) {
	            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	            String inputLine;
	            StringBuilder response = new StringBuilder();

	            while ((inputLine = in.readLine()) != null) {
	                response.append(inputLine);
	            }

	            in.close();
	            conn.disconnect();

	            // Return JSON response with ResponseEntity
	            return ResponseEntity.ok(response.toString());
	        } else {
	            throw new RuntimeException("HTTP GET request failed with response code: " + responseCode);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException("Failed to make the API call: " + e.getMessage());
	    }
	}


	public String verifyOTP(String otp) {
		try {
			configureTrustManager();
			String apiUrl = "https://localhost:9002/occ/v2/electronics/verify?enteredOTP=" + otp;
			URL url = new URL(apiUrl);

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			int responseCode = connection.getResponseCode();
			if (responseCode == 200) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				StringBuilder response = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					response.append(line);
				}
				reader.close();
				return response.toString();
			} else {
				return "HTTP Request failed with response code: " + responseCode;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "An error occurred: " + e.getMessage();
		}
	}

	@PostMapping("/upload")
	@ResponseBody
	public String uploadFile(@RequestParam("file") MultipartFile file) {

		CloseableHttpClient httpClient = null;

		try {
			// Create a custom SSL context that trusts all certificates
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, new TrustManager[] { new X509TrustManager() {
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			} }, null);

			httpClient = HttpClients.custom().setSslcontext(sslContext).build();

			HttpPost httpPost = new HttpPost("https://localhost:9002/occ/v2/electronics/upload");
			MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
			entityBuilder.addBinaryBody("file", file.getInputStream(), ContentType.APPLICATION_OCTET_STREAM,
					file.getOriginalFilename());
			HttpEntity multipartEntity = entityBuilder.build();
			httpPost.setEntity(multipartEntity);

			// Set headers
			httpPost.addHeader("Accept", "application/json");

			CloseableHttpResponse response = httpClient.execute(httpPost);
			HttpEntity responseEntity = response.getEntity();

			// Handle the response, e.g., read the response content
			String responseContent = EntityUtils.toString(responseEntity);
			System.out.println(responseContent);
			return responseContent;
		} catch (Exception e) {
			e.printStackTrace();
			return "An error occurred: " + e.getMessage();
		}

	}
	
	@PostMapping("/submitURL")
    @ResponseBody
    public String submitURL(@RequestParam("imageUrl") String imageUrl) {
        CloseableHttpClient httpClient = null;

        try {
            // Create a custom SSL context that trusts all certificates
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[] { new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            } }, null);

            httpClient = HttpClients.custom().setSSLContext(sslContext).build();

            // Build the URI with the provided URL as a query parameter
            URIBuilder uriBuilder = new URIBuilder("https://localhost:9002/occ/v2/electronics/getThroughURL");
            uriBuilder.setParameter("url", imageUrl);

            HttpPost httpPost = new HttpPost(uriBuilder.build());

            // Set headers
            httpPost.addHeader("Accept", "application/json");
            httpPost.addHeader("Content-Type", "application/json");

            // Log the request details
            System.out.println("Sending POST request to: " + httpPost.getURI());

            // Execute the request
            CloseableHttpResponse response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();

            // Handle the response, e.g., read the response content
            String responseContent = EntityUtils.toString(responseEntity);
            System.out.println("Response: " + responseContent);

            return responseContent;
        } catch (Exception e) {
            e.printStackTrace();
            return "An error occurred: " + e.getMessage();
        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
	}




	public static void configureTrustManager() throws Exception {
		TrustManager[] trustAllCertificates = new TrustManager[] { new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}
		} };

		SSLContext sslContext = SSLContext.getInstance("TLS");
		sslContext.init(null, trustAllCertificates, new java.security.SecureRandom());

		HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
	}
}
