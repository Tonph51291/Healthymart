package com.example.ass;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DangNhap extends AppCompatActivity {
    TextView txt_dangky;
    Button btn_dangnhap;
    EditText edt_email_dn, edt_password_dn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dang_nhap);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_dang_nhap), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
      init();
        txt_dangky.setOnClickListener(v -> {
            startActivity(new Intent(DangNhap.this, DangKy.class));
        });
        btn_dangnhap.setOnClickListener(v -> {
            dangnhap();

        });


    }
    private void init () {
        txt_dangky = findViewById(R.id.txt_dangky);
        btn_dangnhap = findViewById(R.id.btn_dangnhap);
        edt_email_dn = findViewById(R.id.edt_email_dn);
        edt_password_dn = findViewById(R.id.edt_password_dn);

    }


    private void dangnhap() {
        String email = edt_email_dn.getText().toString().trim();

        String password = edt_password_dn.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(DangNhap.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(email);
        userDTO.setPassword(password);
        dangnhaphttp(userDTO);


    }

    private void dangnhaphttp (UserDTO userDTO) {
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.getApiService().dangnhap(userDTO).enqueue(callbackdangnhap);
    }


  Callback<Respondata<UserDTO>> callbackdangnhap = new Callback<Respondata<UserDTO>>() {

      @Override
      public void onResponse(Call<Respondata<UserDTO>> call, Response<Respondata<UserDTO>> response) {
          if (response.isSuccessful() && response.code() == 200 && response.body() != null) {
              Log.d("user", "onResponse: "+new Gson().toJson(response.body().getUser()));
              UserDTO user = response.body().getUser(); // Lấy dữ liệu user từ API
              if (user != null) {
                  String userId = user.getId(); // Giả sử UserDTO có phương thức getId()

                  Log.d("user i", "onResponse: "+userId);

                  // Lưu userId vào SharedPreferences
                  SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                  SharedPreferences.Editor editor = sharedPreferences.edit();
                  editor.putString("user_id", userId);
                  editor.apply();
              Toast.makeText(DangNhap.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

           startActivity(new Intent(DangNhap.this,Home.class));}
          } else {
              Toast.makeText(DangNhap.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", "Failure: " + response.body().getMessage());
          }

      }

      @Override
      public void onFailure(Call<Respondata<UserDTO>> call, Throwable t) {
          Toast.makeText(DangNhap.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();

      }
  };
}