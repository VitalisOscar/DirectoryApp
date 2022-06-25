package com.directoryapp.models;

import java.util.ArrayList;
import java.util.Map;

public class User extends Model {
    public static final String DB_COLLECTION_NAME = "users";

    private String name, email, phone;
    private ArrayList<String> favorites;

    public User(String name, String email, String phone, ArrayList<String> favorites) {
        this(name, email, phone, favorites, null, null);
    }

    public User(String name, String email, String phone, ArrayList<String> favorites, String id, String timestamp) {
        super(id, timestamp);

        this.name = name;
        this.email = email;
        this.phone = phone;
        this.favorites = favorites == null ? new ArrayList<>() : favorites;
    }

    public static User fromMap(Map<String, Object> data){
        if(data == null) return null;

        return new User(
            (String) data.get("name"),
            (String) data.get("email"),
            (String) data.get("phone"),
            (ArrayList<String>) data.get("favorites"),
            (String) data.get("id"),
            (String) data.get("timestamp")
        );
    }

    @Override
    public Map<String, Object> toMiniMap(){
        Map<String, Object> data = super.toMiniMap();

        data.put("name", name);
        data.put("email", email);
        data.put("phone", phone);

        return data;
    }

    @Override
    public Map<String, Object> toMap(){
        Map<String, Object> data = toMiniMap();

        data.put("favorites", favorites);

        return data;
    }



    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}
