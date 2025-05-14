package com.example.BTL_App_truyen_tranh.gui.RatingManagement;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.BTL_App_truyen_tranh.R;
import com.example.BTL_App_truyen_tranh.models.ApiResponse;
import com.example.BTL_App_truyen_tranh.models.Rating;
import com.example.BTL_App_truyen_tranh.models.Status;
import com.example.BTL_App_truyen_tranh.services.ApiService;
import com.example.BTL_App_truyen_tranh.services.ComicService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatingManagement extends AppCompatActivity {
    private ImageView ic_back;

    public static RecyclerView list_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_management);

        initUi();
        backEvent();
        GetRatings(this);
    }

    private void initUi () {

        ic_back = findViewById(R.id.image_back);
        list_item = findViewById(R.id.list_item);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(RatingManagement.this,
                LinearLayoutManager.VERTICAL,
                false);
        list_item.setLayoutManager(linearLayoutManager);

    }

    private void backEvent () {
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public static void GetRatings (Context context) {
        ComicService comicService = ApiService.createService(ComicService.class);
        Call<ApiResponse<List<Rating>>> call = comicService.getRatings();

        call.enqueue(new Callback<ApiResponse<List<Rating>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Rating>>> call, Response<ApiResponse<List<Rating>>> response) {

                if(response.body().getStatus() == Status.SUCCESS){
                    List<Rating> ratings = response.body().getData();
                    if(ratings !=null || ratings.size() != 0) {

                        RatingItemAdapter adapter = new RatingItemAdapter(ratings, context);
                        list_item.setAdapter(adapter);

                    }
                    else {
                        // do something

                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Rating>>> call, Throwable t) {

            }
        });

    }
}