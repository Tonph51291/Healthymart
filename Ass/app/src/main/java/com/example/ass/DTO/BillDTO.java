package com.example.ass.DTO;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class BillDTO {
    @SerializedName("_id")
    private String id;
    private String userId;
    private List<BillDetalis> billDetails;
    private String soDienThoai;

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getDiaChi() {
        return diaChi;
    }
    public Double totalPrice;

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    private String diaChi;

    public List<BillDetalis> getBillDetails() {
        return billDetails;
    }

    public void setBillDetails(List<BillDetalis> billDetails) {
        this.billDetails = billDetails;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


}