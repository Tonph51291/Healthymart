package com.example.ass.ui.fravorite;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ass.Apdater.ProductAdapter;
import com.example.ass.DTO.ProductDTO;
import com.example.ass.R;
import com.example.ass.databinding.FragmentHomeBinding;

import java.util.ArrayList;

public class FavortiteFragment extends Fragment {
    RecyclerView rcv_favorite;
    ArrayList<ProductDTO> list;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_fravortite, container, false);
        rcv_favorite = view.findViewById(R.id.rcv_sanpham_tim);

        rcv_favorite.setLayoutManager(new GridLayoutManager(getContext(), 2));



        ProductAdapter productAdapter = new ProductAdapter(list, getContext());
        rcv_favorite.setAdapter(productAdapter);


        return view;
    }

}