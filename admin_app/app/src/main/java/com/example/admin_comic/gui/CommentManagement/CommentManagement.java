package com.example.admin_comic.gui.CommentManagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.admin_comic.R;
import com.example.admin_comic.models.ApiResponse;
import com.example.admin_comic.models.Comment;
import com.example.admin_comic.models.Status;
import com.example.admin_comic.services.ApiService;
import com.example.admin_comic.services.ComicService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentManagement extends AppCompatActivity {

    private ImageView ic_back;

    public static RecyclerView list_item;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_management2);

        initUi();
        GetComments(this);
        backEvent(); // trigger when user click icon back



    }

    private void initUi () {

        ic_back = findViewById(R.id.image_back);
        list_item = findViewById(R.id.list_item);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CommentManagement.this,
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

    public static void GetComments (Context context) {
        ComicService comicService = ApiService.createService(ComicService.class);
        Call<ApiResponse<List<Comment>>> call = comicService.getComments();

        call.enqueue(new Callback<ApiResponse<List<Comment>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Comment>>> call, Response<ApiResponse<List<Comment>>> response) {

                if(response.body().getStatus() == Status.SUCCESS){
                    List<Comment> comments = response.body().getData();
                    if(comments !=null || comments.size() != 0) {

                        CommentItemAdapter adapter = new CommentItemAdapter(comments, context);
                        list_item.setAdapter(adapter);
                        Log.d("COMMENT_TT",comments.size() + "") ;
                    }
                    else {
                        // do something

                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Comment>>> call, Throwable t) {

            }
        });

    }
}