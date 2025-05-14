package com.example.BTL_App_truyen_tranh.gui.TheLoaiTruyen;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.BTL_App_truyen_tranh.R;
import com.example.BTL_App_truyen_tranh.gui.Home.HomeItemTruyen;
import com.example.BTL_App_truyen_tranh.models.ApiResponse;
import com.example.BTL_App_truyen_tranh.models.Comic;
import com.example.BTL_App_truyen_tranh.services.ApiService;
import com.example.BTL_App_truyen_tranh.services.ComicService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TheLoaiTruyen extends AppCompatActivity {
    private ImageView img_back;
    private TextView textTenTheLoai;
    private RecyclerView list_item_truyen;

    int genreId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_loai);
        img_back = findViewById(R.id.img_back);
        textTenTheLoai = findViewById(R.id.textTenTheLoai);
        list_item_truyen = findViewById(R.id.list_item_truyen);


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        Intent intent = getIntent();

        genreId = intent.getIntExtra("Key_idTheLoai", 0);
        if (intent.getStringExtra("Key_tentheloai") != null) {
            textTenTheLoai.setText(intent.getStringExtra("Key_tentheloai"));
            //Khởi tạo GridLayoutManager Có số cột là 2
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
            //Chuyền Layout Manager cho list_item_truyen
            list_item_truyen.setLayoutManager(gridLayoutManager);
            //Khởi tạo HomeItemTruyen
//            HomeItemTruyen homeItemTruyen = new HomeItemTruyen(gettruyentranhtl(intent.getStringExtra("Key_tentheloai"), sqLiteDAO1), this);
//            //Chuyền adapter cho list_item_truyen
//            list_item_truyen.setAdapter(homeItemTruyen);
        }
        getListTruyen();



    }
    public  void getListTruyen() {
        //Khởi tạo HomeItemTruyen
//        HomeItemTruyen homeItemTruyen = new HomeItemTruyen(getall_tt(sqLiteDAO1), this);
//        //Chuyền adapter cho list_item_truyen
//        list_item_truyen.setAdapter(homeItemTruyen);


        ComicService comicService = ApiService.createService(ComicService.class);
        Call<ApiResponse<List<Comic>>> call = comicService.getComicByGenre(genreId);

        call.enqueue(new Callback<ApiResponse<List<Comic>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Comic>>> call, Response<ApiResponse<List<Comic>>> response) {
                List<Comic> comics = response.body().getData();
                if (comics != null) {
                    // do something

                    HomeItemTruyen homeQlItemTruyen = new HomeItemTruyen(comics, TheLoaiTruyen.this);
                    list_item_truyen.setAdapter(homeQlItemTruyen);
//                    HomeItemTruyen homeQlItemTruyen = new HomeItemTruyen(comics, HomePage.this);
//                    list_item_truyen.setAdapter(homeQlItemTruyen);
                } else {
                    // do something
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Comic>>> call, Throwable t) {

            }
        });
    }
}