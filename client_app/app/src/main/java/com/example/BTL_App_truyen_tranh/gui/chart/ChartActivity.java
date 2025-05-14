package com.example.BTL_App_truyen_tranh.gui.chart;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.BTL_App_truyen_tranh.R;
import com.example.BTL_App_truyen_tranh.models.ApiResponse;
import com.example.BTL_App_truyen_tranh.models.ChartFilter;
import com.example.BTL_App_truyen_tranh.models.Comic;
import com.example.BTL_App_truyen_tranh.services.ApiService;
import com.example.BTL_App_truyen_tranh.services.ComicService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChartActivity extends AppCompatActivity {

    ImageView icBack, icFilter;
    List<Comic> comics;
    ChartItemAdapter adapter;

    RecyclerView recyclerView;

    GridLayoutManager gridLayoutManager;

    String chartFilter = ChartFilter.DAY.getValue();

    String[] filters = {"Lọc theo ngày", "Lọc theo tuần", "Lọc theo tháng"};

    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        initUI();
        handleBackPressed();
        spinnerOnClick();
        callApi();

    }

    private void initUI() {
        icBack = findViewById(R.id.ic_back);

        recyclerView = findViewById(R.id.list_item);

        gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, filters);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
    }

    private void spinnerOnClick() {

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        chartFilter = ChartFilter.DAY.getValue();
                        callApi();
                        break;
                    case 1:
                        chartFilter = ChartFilter.WEEK.getValue();
                        callApi();
                        break;
                    case 2:
                        chartFilter = ChartFilter.MONTH.getValue();
                        callApi();
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void handleBackPressed() {
        icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void callApi() {

        ComicService comicService = ApiService.createService(ComicService.class);
        Call<ApiResponse<List<Comic>>> call = comicService.getComicsChart(chartFilter);
        call.enqueue(new Callback<ApiResponse<List<Comic>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Comic>>> call, Response<ApiResponse<List<Comic>>> response) {

                comics = response.body().getData();
                if (comics != null && comics.size() != 0) {

                    adapter = new ChartItemAdapter(comics, ChartActivity.this);
                    recyclerView.setAdapter(adapter);

                } else {
                    recyclerView.setAdapter(null);

                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Comic>>> call, Throwable t) {
                // do something
            }
        });


    }
}