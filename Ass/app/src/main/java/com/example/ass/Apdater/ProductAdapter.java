package com.example.ass.Apdater;

import android.app.AlertDialog;
import android.content.Context;
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
import com.squareup.picasso.Picasso;

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

        // Image URL and Picasso loading
        HttpRequest httpRequest = new HttpRequest();
        String imageUrl = httpRequest.getApiService().BASE_URL + productDTO.getProductImg();
        Log.d("ImageUrl", "ImageUrl: " + imageUrl);
        Picasso.get().load(imageUrl).into(holder.img_product);

        // Set product name and price
        holder.txt_name.setText(productDTO.getProductName());
        holder.txt_price.setText(productDTO.getPrice());

        // Set heart icon state (add favorite or not)
        updateHeartIconState(holder.img_heart, productDTO.isFavorite());

        // Heart button click listener to toggle favorite
        holder.img_heart.setOnClickListener(v -> {
            boolean isFavorite = productDTO.isFavorite();

            productDTO.setFavorite(!isFavorite);

            updateHeartIconState(holder.img_heart, !isFavorite);
        });

        holder.btn_add.setOnClickListener(v -> {
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_dathang, null);
            alertDialog.setView(view);
            alertDialog.show();
            EditText edt_sdt_kh = view.findViewById(R.id.edt_sdt_kh);
            EditText edt_diachi_kh = view.findViewById(R.id.edt_diachi_kh);
            EditText edt_sl_sp = view.findViewById(R.id.edt_sl_sp);
            Button btn_add_dh = view.findViewById(R.id.btn_dathang);

            btn_add_dh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String sdt_kh = edt_sdt_kh.getText().toString().trim();
                    String diachi_kh = edt_diachi_kh.getText().toString().trim();
                    String sl_sp = edt_sl_sp.getText().toString().trim();

                    // Validate inputs
                    if (sdt_kh.isEmpty() || diachi_kh.isEmpty() || sl_sp.isEmpty()) {
                        Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int quantity;
                    try {
                        quantity = Integer.parseInt(sl_sp);
                    } catch (NumberFormatException e) {
                        Toast.makeText(context, "Số lượng phải là số hợp lệ", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Tính tổng tiền
                    int price;
                    try {
                        price = Integer.parseInt(productDTO.getPrice()); // Chuyển price từ String sang int
                    } catch (NumberFormatException e) {
                        Toast.makeText(context, "Giá sản phẩm không hợp lệ", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int tongTien = price * quantity;

                    // Create BillDetalis object
                    BillDetalis billDetalis = new BillDetalis();

                    billDetalis.setProductID(productDTO);
                    billDetalis.setSoDienThoai(sdt_kh);
                    billDetalis.setDiaChi(diachi_kh);
                    billDetalis.setQuantity(quantity);
                    billDetalis.setTongTien(tongTien); // Set tổng tiền

                    // Call API to add to cart
                    addToCart(billDetalis);

                    // Dismiss the dialog
                    alertDialog.dismiss();
                }
            });






        });
    }
    private void addToCart(BillDetalis billDetalis) {
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.getApiService().createDonHang(billDetalis).enqueue(new Callback<Respondata<BillDetalis>>() {

            @Override
            public void onResponse(Call<Respondata<BillDetalis>> call, Response<Respondata<BillDetalis>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getStatus() == 200) {
                    Toast.makeText(context, "Thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                    Log.d("API_RESPONSE", "Response: " + response.body());
                } else {
                 Log.d("loimoi", response.body().getMessage());


                }

            }

            @Override
            public void onFailure(Call<Respondata<BillDetalis>> call, Throwable t) {
                Toast.makeText(context, "Tạo thành công", Toast.LENGTH_SHORT).show();


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
