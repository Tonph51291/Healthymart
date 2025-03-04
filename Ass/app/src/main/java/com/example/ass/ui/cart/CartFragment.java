package com.example.ass.ui.cart;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView txt_totalPrice;

    private BillAdapter billAdapter;
    private Button btn_dathang;
    ArrayList<BillDetalis> billDetalis = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        btn_dathang = view.findViewById(R.id.btn_order);
        txt_totalPrice = view.findViewById(R.id.txt_totalPrice);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("billId", MODE_PRIVATE);
        String billId = sharedPreferences.getString("billId", null);
        if (billId != null) {
            Log.d("BILL_ID", "Bill ID: " + billId);
        }
        SharedPreferences sharedPreferences1 = getContext().getSharedPreferences("user_prefs", MODE_PRIVATE);
        String userId = sharedPreferences1.getString("user_id", null);
        if (userId != null) {
            Log.d("USER_ID", "User ID: " + userId);
        }
        loadDataBill(billId);

        btn_dathang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogThemThongTin(userId, billId);

            }
        });
        return view;
    }


    private void showDialogThemThongTin(String userId, String billId) {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_dathang, null);
        alertDialog.setView(view);
        alertDialog.show();
        EditText edt_sdt_kh = view.findViewById(R.id.edt_sdt_kh);
        EditText edt_diachi_kh = view.findViewById(R.id.edt_diachi_kh);

        Button btn_add_dh = view.findViewById(R.id.btn_dathang);

        btn_add_dh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sdt = edt_sdt_kh.getText().toString();
                String diachi = edt_diachi_kh.getText().toString();
                if (sdt.isEmpty() || diachi.isEmpty()) {
                    Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (sdt.length() != 10) {
                    Toast.makeText(getContext(), "Số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }
                BillDTO billDTO = new BillDTO();
                billDTO.setSoDienThoai(sdt);
                billDTO.setDiaChi(diachi);
                billDTO.setUserId(userId);
                billDTO.setBillDetails(billDetalis);
                billDTO.setTotalPrice(Double.parseDouble(txt_totalPrice.getText().toString().replace(" VND", "").trim()));
                updateBill(billId, billDTO);
                alertDialog.dismiss();

            }

        });
    }


    private void updateBill(String id, BillDTO billDTO) {
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.getApiService().updateBill(id, billDTO).enqueue(new Callback<BillDTO>() {
            @Override
            public void onResponse(Call<BillDTO> call, Response<BillDTO> response) {
                if (response.isSuccessful()) {
                    BillDTO updatedBill = response.body();
                    if (updatedBill != null) {
                        Toast.makeText(getContext(), "Đơn hàng đã được cập nhật", Toast.LENGTH_SHORT).show();

                        // Xóa bill khỏi giao diện (Không xóa trong database)
                        SharedPreferences sharedPreferences = getContext().getSharedPreferences("billId", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("billId", null); // Xóa billId khỏi SharedPreferences
                        editor.apply();

                        // Xóa dữ liệu trên giao diện
                        billDetalis.clear();
                        billAdapter.notifyDataSetChanged();
                        txt_totalPrice.setText("0 VND");

                    } else {
                        Toast.makeText(getContext(), "Cập nhật đơn hàng thất bại", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("UPDATE_BILL_ERROR", "Lỗi từ server: " + errorBody);
                        Toast.makeText(getContext(), "Lỗi từ server: " + errorBody, Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        Log.e("UPDATE_BILL_ERROR", "Không thể đọc lỗi từ server", e);
                        Toast.makeText(getContext(), "Lỗi không xác định khi cập nhật đơn hàng", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<BillDTO> call, Throwable t) {
                Log.e("UPDATE_BILL_ERROR", "Lỗi khi gọi API: " + t.getMessage(), t);
                Toast.makeText(getContext(), "Lỗi khi cập nhật đơn hàng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loadDataBill(String billId) {
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.getApiService().getBillDetailsByBillId(billId).enqueue(new Callback<List<BillDetalis>>() {
            @Override
            public void onResponse(Call<List<BillDetalis>> call, Response<List<BillDetalis>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    billDetalis = new ArrayList<>(response.body());

                    if (billDetalis.isEmpty()) {
                        Log.d("BillDetalis", "Danh sách rỗng");
                    } else {
                        Log.d("BillDetalis", "Số lượng: " + billDetalis.size());
                    }

                    billAdapter = new BillAdapter(getContext(), billDetalis,totalPrice -> txt_totalPrice.setText(totalPrice + " VND"));
                    recyclerView.setAdapter(billAdapter);
                } else {
                    Log.d("API_RESPONSE", "Dữ liệu bill null hoặc không hợp lệ");
                }
            }

            @Override
            public void onFailure(Call<List<BillDetalis>> call, Throwable t) {
                Log.d("Cart", "Lỗi API: " + t.getMessage());
            }
        });
    }



}