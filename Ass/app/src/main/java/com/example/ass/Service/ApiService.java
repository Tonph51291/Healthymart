package com.example.ass.Service;

import com.example.ass.DTO.BillDTO;
import com.example.ass.DTO.BillDetalis;
import com.example.ass.DTO.CatetogyDTO;
import com.example.ass.DTO.ProductDTO;
import com.example.ass.DTO.Respondata;
import com.example.ass.DTO.UserDTO;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
        String BASE_URL = "http://192.168.1.2:3000/";
        @POST("account/dangky")
        Call<Respondata<UserDTO>> dangky(@Body UserDTO userDTO);

        @POST("account/dangnhap")
        Call<Respondata<UserDTO>> dangnhap(@Body UserDTO userDTO);

        @GET("category/get_list_category")
        Call<Respondata<ArrayList<CatetogyDTO>>> getCatetogy();
        @GET("product/get_list_product")
        Call<Respondata<ArrayList<ProductDTO>>> getProduct();

        @POST ("billdetails")
        Call<Respondata<BillDetalis>> createDonHang(@Body BillDetalis billDetalis);

        @GET("billdetails/get_list_bill_details")
        Call<Respondata<List<BillDetalis>>> getBillDetails();

        @GET("bill/bills")
        Call<Respondata<List<BillDTO>>> getBill();

        @POST("bill/bills")
        Call<Respondata<BillDTO>> createBill(@Body BillDTO billDTO);

        @GET("biil/{id}")
        Call<Respondata<BillDTO>> getBillById(@Path("id") String id);










}
