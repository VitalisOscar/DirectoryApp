package com.directoryapp.models;

import com.directoryapp.helpers.Utils;

import java.util.ArrayList;
import java.util.Map;

public class Business extends Model {
    public static final String DB_COLLECTION_NAME = "businesses";

    private String name, description, imageUrl, address, town;
    private Category category = null;
    private User owner = null;
    private Map<String, Map<String, Object>> openHours;
    private ArrayList<String> searchIndex;


    public Business(String name, String description, String address, String town, Map<String, Map<String, Object>> openHours, String imageUrl, Category category, User owner) {
        this(name, description, address, town, openHours, imageUrl, category, owner, null, null, null);
    }

    public Business(String name, String description, String address, String town, Map<String, Map<String, Object>> openHours, String imageUrl, Category category, User owner, ArrayList<String> searchIndex, String id, String timestamp) {
        super(id, timestamp);

        this.name = name;
        this.description = description;
        this.address = address;
        this.town = town;
        this.openHours = openHours;
        this.imageUrl = imageUrl;
        this.category = category;
        this.owner = owner;

        this.searchIndex = searchIndex == null ?
                Utils.generateSearchIndex(new ArrayList<>(), name, category.getName()) : searchIndex;
    }

    public static Business fromMap(Map<String, Object> data){
        if(data == null) return null;

        return new Business(
            (String) data.get("name"),
            (String) data.get("description"),
            (String) data.get("address"),
            (String) data.get("town"),
            (Map<String, Map<String, Object>>) data.get("openHours"),
            (String) data.get("imageUrl"),
            Category.fromMap((Map<String, Object>) data.get("category")),
            User.fromMap((Map<String, Object>) data.get("owner")),
            (ArrayList<String>) data.get("searchIndex"),
            (String) data.get("id"),
            (String) data.get("timestamp")
        );
    }

    @Override
    public Map<String, Object> toMiniMap(){
        Map<String, Object> data = super.toMiniMap();

        Map<String, Object> owner_map = owner == null ? null : owner.toMiniMap();
        Map<String, Object> category_map = category == null ? null : category.toMiniMap();

        data.put("name", name);
        data.put("description", description);
        data.put("imageUrl", imageUrl);
        data.put("address", address);
        data.put("town", town);
        data.put("owner", owner_map);
        data.put("category", category_map);

        return data;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> data = super.toMap();
        data.put("searchIndex", searchIndex);
        data.put("openHours", openHours);
        return data;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public User getSeller() {
        return owner;
    }

    public ArrayList<String> getSearchIndex() {
        return searchIndex;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getAddress() {
        return address;
    }

    public String getTown() {
        return town;
    }

    public User getOwner() {
        return owner;
    }

    public Map<String, Map<String, Object>> getOpenHours() {
        return openHours;
    }
}
