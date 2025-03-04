package com.example.ass.Apdater;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BillAdapter extends RecyclerView.Adapter<BillAdapter.BillViewHolder> {

    private Context context;
    private List<BillDetalis> billLDetalisList;
    int quantity = 1;
    double priceProduct = 0;

    private OnTotalPriceChangeListener totalPriceChangeListener;

    public interface OnTotalPriceChangeListener {
        void onTotalPriceChanged(double totalPrice);
    }

    public BillAdapter(Context context, List<BillDetalis> billLDetalisList, OnTotalPriceChangeListener listener) {
        this.context = context;
        this.billLDetalisList = billLDetalisList;
        this.totalPriceChangeListener = listener;
    }
    @NonNull
    @Override
    public BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_shop, parent, false); // Use your item layout XML
        return new BillViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull BillViewHolder holder, int position) {
        BillDetalis billDetalis = billLDetalisList.get(position);
        Log.d("BillAdapter", "Binding productId: " + billDetalis.get_id());

        HttpRequest httpRequest = new HttpRequest();

        httpRequest.getApiService().getProductById(billDetalis.getProductId()).enqueue(new Callback<Respondata<ProductDTO>>() {
            @Override
            public void onResponse(Call<Respondata<ProductDTO>> call, Response<Respondata<ProductDTO>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    ProductDTO product = response.body().getData();
                    holder.tvProductName.setText(product.getProductName());
                    holder.tvProductPrice.setText(product.getPrice() + " VND");
                    billDetalis.setPrice(Double.parseDouble(product.getPrice()));
                    updateTotalPrice();
                    String imageUrl = httpRequest.getApiService().BASE_URL + product.getProductImg();
                    Picasso.get().load(imageUrl).into(holder.ivProductImage);
                } else {
                    Log.e("BillAdapter", "Product data is null for productId: " + billDetalis.getProductId());
                }
            }

            @Override
            public void onFailure(Call<Respondata<ProductDTO>> call, Throwable t) {
                Log.e("BillAdapter", "Failed to fetch product: " + t.getMessage());
            }
        });

        // Kiểm tra giá trị số lượng hợp lệ
        int quantity = billDetalis.getQuantity();
        if (quantity < 1) {
            quantity = 1;
        }
        holder.tvQuantity.setText(String.valueOf(quantity));
        Log.d("pricxeProduct ngoai", priceProduct+"");


        // Xử lý tăng số lượng
        holder.btnIncrease.setOnClickListener(v -> {
            billDetalis.setQuantity(billDetalis.getQuantity() + 1);
            UpdateQuantity(billDetalis.get_id(), billDetalis.getQuantity());
            holder.tvQuantity.setText(String.valueOf(billDetalis.getQuantity()));
            Log.d("bill details id" , billDetalis.get_id());
            Log.d("BillAdapter", "Tổng giá bill chi tiết : vị trí"+position+" "+(priceProduct * billDetalis.getQuantity())+"");
        updateTotalPrice();
            notifyItemChanged(position);
        });

        // Xử lý giảm số lượng
        holder.btnDecrease.setOnClickListener(v -> {
            if (billDetalis.getQuantity() > 1) {
                billDetalis.setQuantity(billDetalis.getQuantity() - 1);
                UpdateQuantity(billDetalis.get_id(), billDetalis.getQuantity());
              //  Log.d("bill details id" , billDetalis.get_id());
                holder.tvQuantity.setText(String.valueOf(billDetalis.getQuantity()));
                updateTotalPrice();
                notifyItemChanged(position);
            } else {
                Toast.makeText(context, "Số lượng tối thiểu là 1", Toast.LENGTH_SHORT).show();
            }
        });


    }
private void UpdateQuantity(String id, int quantity) {
        HttpRequest httpRequest = new HttpRequest();
        Log.d("quantity quadate ", id+"");
    Map<String, Integer> body = new HashMap<>();
    body.put("quantity", quantity);
        httpRequest.getApiService().updateQuantityById(id, body).enqueue(new Callback<Respondata<BillDetalis>>() {
            @Override
            public void onResponse(Call<Respondata<BillDetalis>> call, Response<Respondata<BillDetalis>> response) {
                if (response.isSuccessful()) {
                    Log.d("BillAdapter", "Cập nhật số lượng thành công: " + quantity);
                } else {
                    try {
                        String errorBody = response.errorBody().string(); // Lấy lỗi chi tiết từ server
                        Log.e("BillAdapter", "Cập nhật thất bại, mã lỗi: " + response.code() + ", Nội dung lỗi: " + errorBody);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


            @Override
            public void onFailure(Call<Respondata<BillDetalis>> call, Throwable t) {
                Log.e("BillAdapter", "Lỗi khi cập nhật số lượng: " + t.getMessage());
            }
        });
}
    private void updateTotalPrice() {
        double totalPrice = 0;

        for (BillDetalis item : billLDetalisList) {
              totalPrice += item.getPrice()*item.getQuantity();


        }
        Log.d("BillAdater", "Kết quả :"+totalPrice);
        if (totalPriceChangeListener != null) {
            totalPriceChangeListener.onTotalPriceChanged(totalPrice);
        }
    }

    @Override
    public int getItemCount() {


        return billLDetalisList.size();
    }

    public List<BillDetalis> getBillLDetalisList() {
        return billLDetalisList;
    }


    public static class BillViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProductImage;
        TextView tvProductName, tvProductDetail, tvProductPrice, tvQuantity;
        ImageButton btnDecrease, btnIncrease;

        public BillViewHolder(View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.iv_product_image);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvProductDetail = itemView.findViewById(R.id.tv_product_detail);
            tvProductPrice = itemView.findViewById(R.id.tv_product_price);
            tvQuantity = itemView.findViewById(R.id.tv_quantity);
            btnDecrease = itemView.findViewById(R.id.btn_decrease);
            btnIncrease = itemView.findViewById(R.id.btn_increase);
        }
    }
}
