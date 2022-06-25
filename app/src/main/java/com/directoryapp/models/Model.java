package com.directoryapp.models;

import com.directoryapp.helpers.Utils;

import java.util.HashMap;
import java.util.Map;

public abstract class Model {

    public static final String DB_COLLECTION_NAME = "models";

    protected String id, timestamp;

    public Model(String id, String timestamp){
        this.id = id;

        // If null, set current timestamp
        this.timestamp = timestamp == null ? Utils.getCurrentTimestamp() : timestamp;
    }

    public void setId(String id){
        this.id = id;
    }

    /**
     * Will be called to return the model instance created from data fetched from the database
     * Data will come as a hashmap
     *
     * @param data Map<String, Object>
     *
     * @return Model
     */
    public static Model fromMap(Map<String, Object> data){
        return new Model(null, null) {
            @Override
            public Map<String, Object> toMiniMap() {
                return null;
            }
        };
    }

    /**
     * Minimal object representation to be saved as part of another model in database
     * e.g a product's category or a product's seller
     *
     * @return Map<String, Object>
     */
    public Map<String, Object> toMiniMap(){
        Map<String, Object> data = new HashMap<>();

        data.put("id", id);
        data.put("timestamp", timestamp);

        return data;
    }

    /**
     * Object representation to be saved as a single model in database
     *
     * @return Map<String, Object>
     */
    public Map<String, Object> toMap(){
        return toMiniMap();
    }


    public String getId() {
        return id;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
