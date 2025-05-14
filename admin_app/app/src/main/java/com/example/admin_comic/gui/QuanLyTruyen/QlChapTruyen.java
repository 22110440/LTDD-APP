package com.example.admin_comic.gui.QuanLyTruyen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.admin_comic.R;
import com.example.admin_comic.models.ApiResponse;
import com.example.admin_comic.models.Chapter;
import com.example.admin_comic.services.ApiService;
import com.example.admin_comic.services.ComicService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QlChapTruyen extends AppCompatActivity {
    FloatingActionButton floating_add;
    static RecyclerView list_item_ql_ct;
    ImageView image_back;
    Intent intent2 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ql_chap_truyen);
        floating_add = findViewById(R.id.floating_add);
        list_item_ql_ct = findViewById(R.id.list_item_ql_ct);
        image_back = findViewById(R.id.img_back);
        intent2=getIntent();
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        floating_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QlChapTruyen.this, addChap.class);
                intent.putExtra("Key_idTruyen", intent2.getIntExtra("Key_idTruyen", 0));
                startActivity(intent);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(QlChapTruyen.this, LinearLayoutManager.VERTICAL, false);
        list_item_ql_ct.setLayoutManager(linearLayoutManager);
        if (intent2.getIntExtra("Key_idTruyen", 0)!=0){
            GetListChap( QlChapTruyen.this,intent2.getIntExtra("Key_idTruyen", 0));
        }
    }

 

    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    public static void GetListChap(Context context,int idtt) {

        ComicService comicService = ApiService.createService(ComicService.class);
        Call<ApiResponse<List<Chapter>>> call = comicService.getChaptersByComic(idtt);
        call.enqueue(new Callback<ApiResponse<List<Chapter>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Chapter>>> call, Response<ApiResponse<List<Chapter>>> response) {
                List<Chapter> chapters = response.body().getData();
                HomeQLItemChap homeQLItemChap = new HomeQLItemChap(chapters, context);
                list_item_ql_ct.setAdapter(homeQLItemChap);
            }
            @Override
            public void onFailure(Call<ApiResponse<List<Chapter>>> call, Throwable t) {
                // do something
            }
        });

    }

}