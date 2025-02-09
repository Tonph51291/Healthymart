package com.example.ass;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ass.DTO.Respondata;
import com.example.ass.DTO.UserDTO;
import com.example.ass.Service.HttpRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DangKy extends AppCompatActivity {
    Button btn_dangky;
    TextView txt_dangnhap;
    EditText edt_username, edt_password, edt_repassword, edt_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dang_ky);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_dang_ky), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();


        txt_dangnhap.setOnClickListener(view -> {
            startActivity(new Intent(DangKy.this, DangNhap.class));
        });
        btn_dangky.setOnClickListener(view -> {
            dangky();


        });
    }



    private void init() {
        btn_dangky = findViewById(R.id.btn_dangky);
        txt_dangnhap = findViewById(R.id.txt_dangnhap);
        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);
        edt_repassword = findViewById(R.id.edt_repassword);
        edt_username = findViewById(R.id.edt_username);

    }
    private void dangky() {
        String username = edt_username.getText().toString().trim();
        String password = edt_password.getText().toString().trim();
        String repassword = edt_repassword.getText().toString().trim();
        String email = edt_email.getText().toString().trim();
        if (username.isEmpty() || password.isEmpty() || repassword.isEmpty() || email.isEmpty()) {
            Toast.makeText(DangKy.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(repassword)) {
            Toast.makeText(DangKy.this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setName(username);
        userDTO.setPassword(password);
        userDTO.setEmail(email);
        dangkyhttp(userDTO);



    }

    private void dangkyhttp(UserDTO userDTO) {
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.getApiService().dangky(userDTO).enqueue(callbackdangky);
    }


    Callback<Respondata<UserDTO>> callbackdangky = new Callback<Respondata<UserDTO>>() {
        @Override
        public void onResponse(Call<Respondata<UserDTO>> call, Response<Respondata<UserDTO>> response) {
            if (response.isSuccessful() && response.code() == 201 && response.body() != null) {
                Toast.makeText(DangKy.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
            } else {
                Log.d("API_ERROR", "Response not successful: " + response.message());
                if (response.body() != null) {
                    Log.d("API_ERROR", "Error message: " + response.body().getMessage());
                } else {
                    Log.d("API_ERROR", "Response body is null");
                }
                Toast.makeText(DangKy.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<Respondata<UserDTO>> call, Throwable t) {
            Log.d("API_ERROR", "Failure: " + t.getMessage());
            Toast.makeText(DangKy.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
        }
    };



}


