package com.example.ass.DTO;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BillDTO {
    @SerializedName("_id")
    private String id;
    private String Email;
    private String Status;
    private List<BillDetalis> BillDetalis;

    public void setBillDetalis(List<com.example.ass.DTO.BillDetalis> billDetalis) {
        BillDetalis = billDetalis;
    }

    public List<com.example.ass.DTO.BillDetalis> getBillDetalis() {
        return BillDetalis;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
