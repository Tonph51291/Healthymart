package com.example.ass.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ass.Apdater.ProductAdapter;
import com.example.ass.DTO.ProductDTO;
import com.example.ass.DTO.Respondata;
import com.example.ass.R;
import com.example.ass.Service.HttpRequest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Frag_SanPham extends Fragment {
    RecyclerView rcv_sanpham;
    ArrayList<ProductDTO> listProduct;
    ProductAdapter productAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sanpham, container, false);
        rcv_sanpham = view.findViewById(R.id.rcv_sanpham);
       rcv_sanpham.setLayoutManager(new GridLayoutManager(getContext(), 2));
//        listProduct = new ArrayList<>();
//        listProduct.add(new ProductDTO(R.drawable.quabo, "Quả bơ", "500000", false));
//        listProduct.add(new ProductDTO(R.drawable.quabo, "Quả bơ", "500000", true));
//        listProduct.add(new ProductDTO(R.drawable.quabo, "Quả bơ", "500000", true));



        rcv_sanpham.setAdapter(productAdapter);
        loadDateProduct();

        return view;
    }
    private void loadDateProduct() {
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.getApiService().getProduct().enqueue(new Callback<Respondata<ArrayList<ProductDTO>>>() {

            @Override
            public void onResponse(Call<Respondata<ArrayList<ProductDTO>>> call, Response<Respondata<ArrayList<ProductDTO>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getStatus() == 200) {
                    ArrayList<ProductDTO> list = response.body().getData();
                    productAdapter = new ProductAdapter(list, getContext());
                    rcv_sanpham.setAdapter(productAdapter);
                } else {
                    System.out.println("Lỗi");
                    Log.d("API_ERROR", "Failure: " + response.body().getMessage());
                }

            }

            @Override
            public void onFailure(Call<Respondata<ArrayList<ProductDTO>>> call, Throwable t) {
                Log.d("API_ERROR", "Failure: " + t.getMessage());

            }
        });
    }

}
