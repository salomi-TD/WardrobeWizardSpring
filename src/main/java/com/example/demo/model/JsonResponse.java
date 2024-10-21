package com.example.demo.model;

import java.util.List;

public class JsonResponse {

	@Override
	public String toString() {
		// Customize the string representation here.
		return "JsonResponse{query_image='" + data.getQuery_image() + "', result_groups=" + data.getResult_groups()
				+ '}';
	}

	public Data data;

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

}