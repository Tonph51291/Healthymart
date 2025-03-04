package com.example.ass.Apdater;

import static android.content.Context.MODE_PRIVATE;
import static java.security.AccessController.getContext;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ass.DTO.BillDTO;
import com.example.ass.DTO.BillDetalis;
import com.example.ass.DTO.ProductDTO;
import com.example.ass.DTO.Respondata;
import com.example.ass.R;
import com.example.ass.Service.HttpRequest;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    List<ProductDTO> list;
    private Context context;

    public ProductAdapter(List<ProductDTO> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductDTO productDTO = list.get(position);

        // Load ảnh sản phẩm từ URL
        HttpRequest httpRequest = new HttpRequest();
        String imageUrl = httpRequest.getApiService().BASE_URL + productDTO.getProductImg();
        Log.d("ImageUrl", "ImageUrl: " + imageUrl);
        Picasso.get().load(imageUrl).into(holder.img_product);

        // Gán thông tin sản phẩm
        holder.txt_name.setText(productDTO.getProductName());
        holder.txt_price.setText(String.valueOf(productDTO.getPrice()));

        // Cập nhật trạng thái yêu thích (heart icon)
        updateHeartIconState(holder.img_heart, productDTO.isFavorite());

        // Sự kiện click vào icon trái tim
        holder.img_heart.setOnClickListener(v -> {
            boolean isFavorite = productDTO.isFavorite();
            productDTO.setFavorite(!isFavorite);
            updateHeartIconState(holder.img_heart, !isFavorite);
        });
        SharedPreferences sharedPreferences =context.getSharedPreferences("user_prefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("user_id", null);
        if (userId != null) {
            Log.d("USER_ID", "User ID: " + userId);
        }

        // Xử lý sự kiện thêm vào giỏ hàng
        holder.btn_add.setOnClickListener(v->getBillByUserId(userId, productDTO.get_id()));
    }

    private void getBillByUserId(String userId, String productId) {
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.getApiService().getBillByUserId(userId).enqueue(new Callback<Respondata<List<BillDTO>>>() {
            @Override
            public void onResponse(Call<Respondata<List<BillDTO>>> call, Response<Respondata<List<BillDTO>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<BillDTO> bills = response.body().getData();
                    String existingBillId = null;

                    // Kiểm tra bill nào có total == 0
                    for (BillDTO bill : bills) {
                        if (bill.getTotalPrice() == 0) {
                            existingBillId = bill.getId();
                            break;
                        }
                    }

                    if (existingBillId != null) {
                        // Nếu có bill có total == 0, sử dụng bill đó
                        saveBillIdToPreferences(existingBillId);
                        addToCart(productId, userId, existingBillId);
                    } else {
                        // Nếu tất cả bill đều có total != 0, tạo bill mới
                        addBill(userId, productId);
                    }
                } else {
                    Log.e("Bills", "Lỗi lấy dữ liệu");
                }
            }

            @Override
            public void onFailure(Call<Respondata<List<BillDTO>>> call, Throwable t) {
                Toast.makeText(context, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                Log.d("API_ERROR", "onFailure: " + t.getMessage());
            }
        });
    }

    // Hàm lưu billId vào SharedPreferences
    private void saveBillIdToPreferences(String billId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("billId", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("billId", billId);
        editor.apply();
    }


    private void addBill(String userId, String productId) {
        HttpRequest httpRequest = new HttpRequest();
        BillDTO newBill = new BillDTO();
        newBill.setUserId(userId);

        httpRequest.getApiService().createBill(newBill).enqueue(new Callback<Respondata<BillDTO>>() {
            @Override
            public void onResponse(Call<Respondata<BillDTO>> call, Response<Respondata<BillDTO>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getStatus() == 200) {
                    BillDTO createdBill = response.body().getData();
                    if (createdBill != null) {
                        String billId = createdBill.getId();
                        SharedPreferences sharedPreferences = context.getSharedPreferences("billId", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("billId", billId);
                        editor.apply();
                        Log.d("ADD_BILL", "Bill created with ID: " + billId);
                        addToCart(productId, userId, billId);
                    }
                } else {
                    Log.e("ADD_BILL", "Lỗi khi tạo hóa đơn");
                }
            }

            @Override
            public void onFailure(Call<Respondata<BillDTO>> call, Throwable t) {
                Log.e("ADD_BILL", "Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    private void addToCart( String productId, String userId, String billId) {
        BillDetalis billDetails = new BillDetalis();
        billDetails.setBillId(billId);
        billDetails.setUserId(userId);
        billDetails.setProductId(productId);
        billDetails.setQuantity(1);

        HttpRequest httpRequest = new HttpRequest();
        httpRequest.getApiService().createDonHang(billDetails).enqueue(new Callback<Respondata<BillDetalis>>() {
            @Override
            public void onResponse(Call<Respondata<BillDetalis>> call, Response<Respondata<BillDetalis>> response) {
                if (response.isSuccessful() && response.code() == 201) {
                    Toast.makeText(context, "Thêm vào giỏ hàng thành công!", Toast.LENGTH_SHORT).show();
                    Log.d("ADD_TO_CART", "Sản phẩm đã thêm vào hóa đơn ID: " + billId);
                } else {
                    Log.e("ADD_TO_CART", "Lỗi khi thêm sản phẩm vào giỏ hàng - Mã lỗi: " + response.code());

                    if (response.errorBody() != null) {
                        try {
                            String errorBody = response.errorBody().string();
                            Log.e("ADD_TO_CART", "Lỗi từ server: " + errorBody);
                        } catch (IOException e) {
                            Log.e("ADD_TO_CART", "Không thể đọc lỗi từ server", e);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Respondata<BillDetalis>> call, Throwable t) {
                Log.e("ADD_TO_CART", "Lỗi kết nối: " + t.getMessage(), t);
            }

        });
    }




    // Update heart icon state (filled or empty)
    private void updateHeartIconState(ImageView heartIcon, boolean isFavorite) {
        if (isFavorite) {
            heartIcon.setImageResource(R.drawable.heart); // Heart filled (favorite)
        } else {
          //  heartIcon.setImageResource(R.drawable.heart_outline); // Heart empty (not favorite)
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView img_product;
        ImageView img_heart;
        TextView txt_name;
        TextView txt_price;
        ImageButton btn_add;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            img_product = itemView.findViewById(R.id.img_product);
            img_heart = itemView.findViewById(R.id.btn_heart);
            txt_name = itemView.findViewById(R.id.txt_name_product);
            txt_price = itemView.findViewById(R.id.txt_price);
            btn_add = itemView.findViewById(R.id.btn_add);
        }
    }
}
