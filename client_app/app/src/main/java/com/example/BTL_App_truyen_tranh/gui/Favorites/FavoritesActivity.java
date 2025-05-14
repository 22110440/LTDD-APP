package com.example.BTL_App_truyen_tranh.gui.Favorites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.BTL_App_truyen_tranh.R;
import com.example.BTL_App_truyen_tranh.gui.chart.ChartActivity;
import com.example.BTL_App_truyen_tranh.gui.chart.ChartItemAdapter;
import com.example.BTL_App_truyen_tranh.models.ApiResponse;
import com.example.BTL_App_truyen_tranh.models.Comic;
import com.example.BTL_App_truyen_tranh.models.Favorite;
import com.example.BTL_App_truyen_tranh.services.ApiService;
import com.example.BTL_App_truyen_tranh.services.ComicService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoritesActivity extends AppCompatActivity {

    ImageView icBack;

    List<Favorite> favorites;

    FavoriteItemAdapter adapter;

    RecyclerView recyclerView;

    GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        initUI();
        handleBackPressed();
        callApi();

    }

    @Override
    protected void onResume() {
        super.onResume();
        callApi();
    }

    private void initUI(){
        icBack = findViewById(R.id.ic_back);
        recyclerView = findViewById(R.id.list_item);
        gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    private void handleBackPressed(){
        icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void callApi() {
        ComicService comicService = ApiService.createService(ComicService.class);
        Call<ApiResponse<List<Favorite>>> call = comicService.getFavorites();

        call.enqueue(new Callback<ApiResponse<List<Favorite>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Favorite>>> call, Response<ApiResponse<List<Favorite>>> response) {
                favorites = response.body().getData();
                if (favorites != null && favorites.size() != 0) {

                    adapter = new FavoriteItemAdapter(favorites, FavoritesActivity.this);
                    recyclerView.setAdapter(adapter);

                } else {
                    recyclerView.setAdapter(null);

                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Favorite>>> call, Throwable t) {
                // do something
            }
        });
    }
}