package com.example.demo.controllers;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class CurlToJavaHttpClientExample {

    private List<String> categoriesList;
    private String imageURL;

    public void getCategory() {
        try {
            String url = "https://localhost:9002/occ/v2/electronics/getcategory";

            URL apiUrl = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) apiUrl.openConnection();
            httpURLConnection.setRequestMethod("GET");

            int responseCode = httpURLConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()))) {
                    StringBuilder responseContent = new StringBuilder();
                    String inputLine;

                    while ((inputLine = bufferedReader.readLine()) != null) {
                        responseContent.append(inputLine);
                    }

                    ObjectMapper objectMapper = new ObjectMapper();
                    categoriesList = objectMapper.readValue(responseContent.toString(), new TypeReference<>() {
                    });

                    // Extract the image URL (assuming it's the last element in the list)
                    imageURL = categoriesList.remove(categoriesList.size() - 1);

                    System.out.println("Response Status: " + responseCode);
                    System.out.println("Categories List: " + categoriesList);
                    System.out.println("Image URL: " + imageURL);
                }
            } else {
                System.err.println("HTTP request failed with response code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        CurlToJavaHttpClientExample example = new CurlToJavaHttpClientExample();
        example.getCategory();
    }
}
