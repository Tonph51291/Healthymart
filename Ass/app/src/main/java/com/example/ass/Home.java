package com.example.ass;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

import com.example.ass.ui.cart.CartFragment;
import com.example.ass.ui.fravorite.FavortiteFragment;
import com.example.ass.ui.home.HomeFragment;
import com.example.ass.ui.profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Home extends AppCompatActivity {

    BottomNavigationView navView;
    HomeFragment homeFragment;
    FavortiteFragment favoriteFragment;
    CartFragment cartFragment;
    ProfileFragment profileFragment;
    FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        navView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); // Thiết lập Toolbar cho Activity

        // Đảm bảo là có thể thay đổi các Fragment và quản lý Toolbar
        if (savedInstanceState == null) {
            // Nếu là lần đầu tiên vào Activity, thêm HomeFragment
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
        }

        fragmentManager = getSupportFragmentManager();
        homeFragment = new HomeFragment();
        favoriteFragment = new FavortiteFragment();
        cartFragment = new CartFragment();
        profileFragment = new ProfileFragment();

        fragmentManager.beginTransaction().replace(R.id.fragment_container, homeFragment).commit();

        navView.setOnItemReselectedListener(new NavigationBarView.OnItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navigation_home) {
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, homeFragment).commit();
                    toolbar.setTitle("Home");


                } else if (item.getItemId() == R.id.navigation_favorite) {
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, favoriteFragment).commit();
                    toolbar.setTitle("Sản phẩm yêu thích");

                } else if (item.getItemId() == R.id.navigation_cart) {
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, cartFragment).commit();
                    toolbar.setTitle("Giỏ hàng");

                } else if (item.getItemId() == R.id.navigation_profile) {
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, profileFragment).commit();
                    toolbar.setTitle("Thông tin cá nhân");
                }

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Xử lý quay lại nếu có nút Home
                getSupportFragmentManager().popBackStack();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}