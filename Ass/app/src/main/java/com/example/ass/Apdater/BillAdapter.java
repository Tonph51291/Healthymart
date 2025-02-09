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
import com.example.ass.R;
import com.example.ass.Service.HttpRequest;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.BillViewHolder> {

    private Context context;
    private List<BillDetalis> billLDetalisList;
    int quantity = 1;

    public BillAdapter(Context context, List<BillDetalis> billLDetalisList) {
        this.context = context;
        this.billLDetalisList = billLDetalisList;
    }

    @NonNull
    @Override
    public BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_shop, parent, false); // Use your item layout XML
        return new BillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillViewHolder holder, int position) {
        BillDetalis billDTO = billLDetalisList.get(position);

        Log.d("BillAdapter", "onBindViewHolder: " + billDTO.get_id());

        ProductDTO productDTO = billDTO.getProductID(); // Get the ProductDTO object
        HttpRequest httpRequest = new HttpRequest();
        String imageUrl = httpRequest.getApiService().BASE_URL + productDTO.getProductImg();
        Log.d("zzzzzzz", "ImageUrl: " + imageUrl);
        Picasso.get().load(imageUrl).into(holder.ivProductImage);

        // Set the product name from ProductDTO
        holder.tvProductName.setText(productDTO.getProductName());  // Assuming getProductName() is a method in ProductDTO

        // Set the product details (Quantity)
        holder.tvProductDetail.setText("Quantity: " + billDTO.getQuantity());
        holder.tvProductPrice.setText(productDTO.getPrice());



        // Set the quantity
        holder.tvQuantity.setText(String.valueOf(billDTO.getQuantity()));

        // Load the product image if available
       // Assuming getProductImg() returns the image URL
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Picasso.get().load(imageUrl).into(holder.ivProductImage);
        } else {
            // Set a default image if no image URL is available
          //  holder.ivProductImage.setImageResource(R.drawable.default_product_image);
        }
        holder.btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Giảm số lượng nếu giá trị > 0
                if (quantity > 0) {
                    quantity--;
                    holder.tvQuantity.setText(String.valueOf(quantity));
                   holder.tvProductPrice.setText( (Double.parseDouble(productDTO.getPrice())*quantity)+"");
                } else {
                    // Nếu số lượng đã ở mức 0, không giảm nữa
                    Toast.makeText(context, "Số lượng không thể giảm thêm", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity++;
                holder.tvQuantity.setText(String.valueOf(quantity));
                holder.tvProductPrice.setText( (Double.parseDouble(productDTO.getPrice())*quantity)+"");


            }
        });

    }

    @Override
    public int getItemCount() {

        Log.d("BillAdapter", "getItemCount: " + billLDetalisList.size());
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
