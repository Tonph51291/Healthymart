package com.example.ass.Apdater;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ass.DTO.CatetogyDTO;
import com.example.ass.R;
import com.example.ass.Service.HttpRequest;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;

public class CatetogyAdapter extends RecyclerView.Adapter<CatetogyAdapter.CatetogyViewHolder> {

    Context context;
    ArrayList<CatetogyDTO> list;

    public CatetogyAdapter(Context context, ArrayList<CatetogyDTO> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CatetogyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_catetory,null);
        return new CatetogyViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull CatetogyViewHolder holder, int position) {
        CatetogyDTO catetogyDTO = list.get(position);
        holder.txt_category.setText(catetogyDTO.getCateName());
        HttpRequest httpRequest = new HttpRequest();
        String imageUrl =   httpRequest.getApiService().BASE_URL.toString() + catetogyDTO.getCateImg();
        Log.d("zzzzzzz","ImageUrl: " + imageUrl);
        Picasso.get()
                .load(imageUrl)
                .into(holder.img_category);



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class  CatetogyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_category;
        ImageView img_category;
        public CatetogyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_category = itemView.findViewById(R.id.txt_category);
            img_category = itemView.findViewById(R.id.img_category);
        }
    }
}

