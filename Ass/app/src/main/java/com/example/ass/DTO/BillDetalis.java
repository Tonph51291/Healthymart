package com.example.ass.DTO;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class BillDetalis {
    @SerializedName("_id")
    private String _id;

    @SerializedName("billId")
    private String billId;

    @SerializedName("userId")
    private String userId;

   private  String productId;

    @SerializedName("quantity")
    private int quantity;

    private double price;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    // Getters and Setters
    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
