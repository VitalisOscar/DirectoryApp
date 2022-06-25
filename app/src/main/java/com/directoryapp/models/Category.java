package com.directoryapp.models;

import java.util.Map;

public class Category extends Model {
    public static final String DB_COLLECTION_NAME = "categories";

    private String name, description, imageUrl;

    public Category(String name, String description, String imageUrl, String id, String timestamp) {
        super(id, timestamp);

        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public static Category fromMap(Map<String, Object> data){
        if(data == null) return null;

        return new Category(
            (String) data.get("name"),
            (String) data.get("description"),
            (String) data.get("imageUrl"),
            (String) data.get("id"),
            (String) data.get("timestamp")
        );
    }

    @Override
    public Map<String, Object> toMiniMap(){
        Map<String, Object> data = super.toMiniMap();

        data.put("name", name);
        data.put("description", description);
        data.put("imageUrl", imageUrl);

        return data;
    }



    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
