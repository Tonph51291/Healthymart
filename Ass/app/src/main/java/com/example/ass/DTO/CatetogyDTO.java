package com.example.ass.DTO;

import com.google.gson.annotations.SerializedName;

public class CatetogyDTO {
    @SerializedName("_id")
   private String id;
    private String CateName;
    private String CateImg;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCateName() {
        return CateName;
    }

    public void setCateName(String cateName) {
        CateName = cateName;
    }

    public String getCateImg() {
        return CateImg;
    }

    public void setCateImg(String cateImg) {
        CateImg = cateImg;
    }
}
