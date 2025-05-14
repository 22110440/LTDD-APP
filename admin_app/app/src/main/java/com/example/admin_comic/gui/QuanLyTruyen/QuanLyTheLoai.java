package com.example.admin_comic.gui.QuanLyTruyen;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admin_comic.R;
import com.example.admin_comic.models.ApiResponse;
import com.example.admin_comic.models.Genre;
import com.example.admin_comic.models.Status;
import com.example.admin_comic.services.ApiService;
import com.example.admin_comic.services.GenreService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuanLyTheLoai extends AppCompatActivity {
    private ImageView img_back;
    public static RecyclerView list_item_ql_tl;
    private FloatingActionButton floating_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_the_loai);
        floating_add = findViewById(R.id.floating_add);
        list_item_ql_tl = findViewById(R.id.list_item_ql_tl);
        img_back = findViewById(R.id.img_back);
        GetList(this);
        floating_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTheloai();
            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //Khởi tạo LinearLayoutManager cuộn dọc
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(QuanLyTheLoai.this, LinearLayoutManager.VERTICAL, false);
        //Chuyền linearLayoutManager cho list_item_ql_tl
        list_item_ql_tl.setLayoutManager(linearLayoutManager);
    }

    public static void GetList(Context context) {

//        //Khởi tạo homeQlItemTheLoai
//        HomeQlItemTheLoai homeQlItemTheLoai = new HomeQlItemTheLoai(getall_tl(sqLiteDAO), context);
//        //Chuyền Adapter homeQlItemTheLoai cho list_item_ql_tl
//        list_item_ql_tl.setAdapter(homeQlItemTheLoai);

        GenreService genreService = ApiService.createService(GenreService.class);
        Call<ApiResponse<List<Genre>>> call = genreService.getGenres();
        call.enqueue(new Callback<ApiResponse<List<Genre>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Genre>>> call, Response<ApiResponse<List<Genre>>> response) {
                List<Genre> genres = response.body().getData();
                if (genres != null) {
                    // do something
                    HomeQlItemTheLoai homeQlItemTheLoai = new HomeQlItemTheLoai(genres, context);
                    list_item_ql_tl.setAdapter(homeQlItemTheLoai);
                } else {
                    // do something
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Genre>>> call, Throwable t) {

            }
        });

    }


    private void addTheloai() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_tl);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        window.setAttributes(layoutParams);
        dialog.setCancelable(true);

        EditText edit_addtl = dialog.findViewById(R.id.edit_addtl);
        TextView text_view = dialog.findViewById(R.id.text_view);
        Button button_huy = dialog.findViewById(R.id.button_huy);
        Button button_them = dialog.findViewById(R.id.button_them);
        text_view.setText("Thêm thể loại");
        button_them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tentl = edit_addtl.getText().toString().trim();
                if (tentl.isEmpty()) {
                    Toast.makeText(dialog.getContext(), "Vui lòng nhập tên thể loại!", Toast.LENGTH_SHORT).show();
                } else {
                    GenreService genreService = ApiService.createService(GenreService.class);
                    Call<ApiResponse<Genre>> call = genreService.createGenre(tentl);
                    call.enqueue(new Callback<ApiResponse<Genre>>() {
                        @Override
                        public void onResponse(Call<ApiResponse<Genre>> call, Response<ApiResponse<Genre>> response) {
                            ApiResponse<Genre> res = response.body();
                            if (res.getStatus() == Status.SUCCESS) {
                                Toast.makeText(QuanLyTheLoai.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                                GetList(QuanLyTheLoai.this);
                                dialog.dismiss();
                            } else {
                                Toast.makeText(QuanLyTheLoai.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse<Genre>> call, Throwable t) {

                        }
                    });
                }
            }
        });
        button_huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }


}