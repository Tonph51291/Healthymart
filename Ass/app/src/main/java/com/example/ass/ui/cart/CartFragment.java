package com.example.ass.ui.cart;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ass.Apdater.BillAdapter;
import com.example.ass.DTO.BillDTO;
import com.example.ass.DTO.BillDetalis;
import com.example.ass.DTO.Respondata;
import com.example.ass.R;
import com.example.ass.Service.HttpRequest;
import com.example.ass.databinding.FragmentHomeBinding;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartFragment extends Fragment {

    private RecyclerView recyclerView;

    private BillAdapter billAdapter;
    private Button btn_dathang;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        btn_dathang = view.findViewById(R.id.btn_order);


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        loadDataBill();
        btn_dathang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               BillDTO billDTO = new BillDTO();
               billDTO.setEmail("william.henry.harrison@example-pet-store.com");
               billDTO.setStatus("Đang chờ xác nhận");
               List<BillDetalis> billDetalisList = billAdapter.getBillLDetalisList();
               billDTO.setEmail("ton@gmail.com");
               Log.d("BillAdapter", "onBindViewHolder: Json " +new Gson().toJson(billDetalisList ));

               billDTO.setBillDetalis(billDetalisList);




                addBill(billDTO);
            }
        });


        return view;
    }

    private void addBill(BillDTO billDTO) {
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.getApiService().createBill(billDTO).enqueue(new Callback<Respondata<BillDTO>>() {
            @Override
            public void onResponse(Call<Respondata<BillDTO>> call, Response<Respondata<BillDTO>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getStatus() == 200) {
                    Toast.makeText(getContext(), "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
                    Log.d("API_RESPONSE", "Response: " + response.body());
                } else {
                    Toast.makeText(getContext(), "Đặt hàng thất bại", Toast.LENGTH_SHORT).show();
                    Log.d("API_RESPONSE", "Response: " + response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<Respondata<BillDTO>> call, Throwable t) {
                Log.d("zzzzzzzzzzz", "onFailure: Lỗi: "+t.getMessage());
            }


        });
    }

    public void loadDataBill() {
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.getApiService().getBillDetails().enqueue(new Callback<Respondata<List<BillDetalis>>>() {
            @Override
            public void onResponse(Call<Respondata<List<BillDetalis>>> call, Response<Respondata<List<BillDetalis>>> response) {
               if (response.isSuccessful()) {
                     ArrayList<BillDetalis> billDetalisList = (ArrayList<BillDetalis>) response.body().getData();
                     Log.d("API_RESPONSE", "Response: " + response.body());
                    billAdapter = new BillAdapter(getContext(), billDetalisList);
                    recyclerView.setAdapter(billAdapter);

                } else {
//                    Toast.makeText(getContext(), "Lỗi"+response.body().getStatus(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<Respondata<List<BillDetalis>>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi", Toast.LENGTH_SHORT).show();
                Log.d("API_ERROR", "Failure: " + t.getMessage());

            }

        });
    }
}