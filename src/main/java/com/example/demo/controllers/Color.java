package com.example.demo.controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class Color {
    private double confidence;
    private String hexCode;
    private String name;

    @JsonProperty("confidence")
    public double getConfidence() {
        return confidence;
    }

    @JsonProperty("hex_code")
    public String getHexCode() {
        return hexCode;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }
}

 class Label {
    private String classification;
    private double confidence;
    private String name;
    private String secondaryClassification;

    @JsonProperty("classification")
    public String getClassification() {
        return classification;
    }

    @JsonProperty("confidence")
    public double getConfidence() {
        return confidence;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("secondary_classification")
    public String getSecondaryClassification() {
        return secondaryClassification;
    }
}

 class Item {
    private String category;
    private double confidence;
    private String name;

    @JsonProperty("category")
    public String getCategory() {
        return category;
    }

    @JsonProperty("confidence")
    public double getConfidence() {
        return confidence;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }
}

 class Data {
    private List<Color> colors;
    private List<Label> labels;
    private List<Item> items;

    @JsonProperty("colors")
    public List<Color> getColors() {
        return colors;
    }

    @JsonProperty("labels")
    public List<Label> getLabels() {
        return labels;
    }

    @JsonProperty("items")
    public List<Item> getItems() {
        return items;
    }
}

 class Response {
    private Data data;

    @JsonProperty("data")
    public Data getData() {
        return data;
    }
}

 
