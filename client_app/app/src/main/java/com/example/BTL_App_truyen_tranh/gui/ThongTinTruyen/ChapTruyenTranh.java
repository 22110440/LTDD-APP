package com.example.BTL_App_truyen_tranh.gui.ThongTinTruyen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.BTL_App_truyen_tranh.R;
import com.example.BTL_App_truyen_tranh.models.ApiResponse;
import com.example.BTL_App_truyen_tranh.models.Chapter;
import com.example.BTL_App_truyen_tranh.models.ImageChapter;
import com.example.BTL_App_truyen_tranh.models.Status;
import com.example.BTL_App_truyen_tranh.services.ApiService;
import com.example.BTL_App_truyen_tranh.services.ComicService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChapTruyenTranh extends AppCompatActivity {
    private TextView tenchap;
    private ImageView img_back;
    private RecyclerView list_item_img;

    private Button button_truoc, button_sau;
    private int position = 0;
    Intent intent;

    public static List<ImageChapter> imageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chap_truyentranh);
        list_item_img = findViewById(R.id.list_item_img);
        tenchap = findViewById(R.id.tenchap);
        img_back = findViewById(R.id.img_back);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        intent = getIntent();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChapTruyenTranh.this, LinearLayoutManager.VERTICAL, false);
        list_item_img.setLayoutManager(linearLayoutManager);
        if (intent.getStringExtra("Key_tenChap") != null) {

            tenchap.setText(intent.getStringExtra("Key_tenChap"));
        }

        getChapter();


    }

    private void ListImgChap() {
        ChapItemImg chapItemImg = new ChapItemImg(this, imageList);
        list_item_img.setAdapter(chapItemImg);
    }

    private void getChapter() {
        ComicService comicService = ApiService.createService(ComicService.class);
        Call<ApiResponse<Chapter>> call = comicService.getChapter(intent.getIntExtra("Key_idTruyen", 0));

        call.enqueue(new Callback<ApiResponse<Chapter>>() {
            @Override
            public void onResponse(Call<ApiResponse<Chapter>> call, Response<ApiResponse<Chapter>> response) {

                ApiResponse res = response.body();
                if (res.getStatus() == Status.SUCCESS) {
                    Chapter chapter = response.body().getData();
                    for (int i = 0; i < chapter.getImage().size(); i++) {
                        String imagePath = chapter.getImage().get(i);
                        ImageChapter imageItem = new ImageChapter(imagePath);
                        imageList.add(imageItem);
                    }
                    ListImgChap();

                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Chapter>> call, Throwable t) {

            }
        });
    }
}