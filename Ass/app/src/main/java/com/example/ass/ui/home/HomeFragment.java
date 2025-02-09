package com.example.ass.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ass.Apdater.CatetogyAdapter;
import com.example.ass.Apdater.ProductAdapter;
import com.example.ass.DTO.CatetogyDTO;
import com.example.ass.DTO.ProductDTO;
import com.example.ass.DTO.Respondata;
import com.example.ass.R;
import com.example.ass.Service.HttpRequest;
import com.example.ass.ui.Frag_SanPham;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    RecyclerView rcv_catetogy;
    RecyclerView rcv_product;

    CatetogyAdapter catetogyAdapter;

    ProductAdapter productAdapter;

    TextView all_sp;
    FragmentManager fragmentManager;
    Toolbar toolbar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        fragmentManager = getParentFragmentManager();
        rcv_catetogy = view.findViewById(R.id.rcv_catetogy);
        rcv_product = view.findViewById(R.id.rcv_product);

        all_sp = view.findViewById(R.id.all_sp);

        // Đặt LinearLayoutManager cho RecyclerView trước khi gán Adapter

        rcv_catetogy.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rcv_product.setLayoutManager(new GridLayoutManager(getContext(), 2));

        loadDateCategory();
        loadDateProduct();
        all_sp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.fragment_container, new Frag_SanPham()).commit();
               // toolbar.setTitle("Sản phẩm");
            }
        });

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
                    rcv_product.setAdapter(productAdapter);
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

    private void loadDateCategory() {
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.getApiService().getCatetogy().enqueue(new Callback<Respondata<ArrayList<CatetogyDTO>>>() {

            @Override
            public void onResponse(Call<Respondata<ArrayList<CatetogyDTO>>> call, Response<Respondata<ArrayList<CatetogyDTO>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getStatus() == 200) {
                    ArrayList<CatetogyDTO> list = response.body().getData();
                    catetogyAdapter = new CatetogyAdapter(getContext(), list);

                    // Gán adapter cho RecyclerView sau khi khởi tạo nó
                    rcv_catetogy.setAdapter(catetogyAdapter);

                } else {
                    System.out.println("Lỗi");
                    Log.d("API_ERROR", "Failure: " + response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<Respondata<ArrayList<CatetogyDTO>>> call, Throwable t) {
                Log.d("API_ERROR", "Failure: " + t.getMessage());
            }
        });
    }
}
