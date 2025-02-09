package com.example.ass.DTO;

import com.google.gson.annotations.SerializedName;

public class ProductDTO {
    @SerializedName("_id")
   private String _id;
    private String ProductName;
    private String ProductImg;
    private String Price;
    private String CateId;
    private String Quantity;
    private boolean isFavorite;

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductImg() {
        return ProductImg;
    }

    public void setProductImg(String productImg) {
        ProductImg = productImg;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getCateId() {
        return CateId;
    }

    public void setCateId(String cateId) {
        CateId = cateId;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }
}
