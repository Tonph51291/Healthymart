package com.example.ass.DTO;

import com.google.gson.annotations.SerializedName;

public class BillDetalis {
    @SerializedName("id")
    private String _id ;
    private ProductDTO ProductID;
    private String SoDienThoai;
    private String DiaChi;
    private int Quantity;
    private String BillId;
    private  int TongTien;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public ProductDTO getProductID() {
        return ProductID;
    }

    public void setProductID(ProductDTO productID) {
        ProductID = productID;
    }

    public String getSoDienThoai() {
        return SoDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        SoDienThoai = soDienThoai;
    }

    public String getDiaChi() {
        return DiaChi;
    }

    public void setDiaChi(String diaChi) {
        DiaChi = diaChi;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public String getBillId() {
        return BillId;
    }

    public void setBillId(String billId) {
        BillId = billId;
    }

    public int getTongTien() {
        return TongTien;
    }

    public void setTongTien(int tongTien) {
        TongTien = tongTien;
    }
}
