package com.directoryapp.models;

import java.util.Map;

public class Order extends Model{
    public static final String DB_COLLECTION_NAME = "orders";

    private int quantity;
    private Business product;
    private User buyer;

    public Order(int quantity, Business product, User buyer, String id, String timestamp) {
        super(id, timestamp);

        this.quantity = quantity;
        this.product = product;
        this.buyer = buyer;
    }

    public Order(int quantity, Business product, User buyer){
        this(quantity, product, buyer, null, null);
    }

    @Override
    public Map<String, Object> toMiniMap() {
        Map<String, Object> data = super.toMiniMap();

        Map<String, Object> buyer_map = buyer == null ? null : buyer.toMiniMap();
        Map<String, Object> product_map = product == null ? null : product.toMiniMap();

        data.put("quantity", quantity);
        data.put("buyer", buyer_map);
        data.put("product", product_map);

        return data;
    }
}
