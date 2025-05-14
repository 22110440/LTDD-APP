package com.example.admin_comic.gui.QuanLyTruyen;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.admin_comic.R;
import com.example.admin_comic.gui.Chart.ChartActivity;
import com.example.admin_comic.gui.CommentManagement.CommentManagement;
import com.example.admin_comic.gui.Login.DangNhapActivity;
import com.example.admin_comic.gui.RatingManagement.RatingManagement;

public class HomeQuanLy extends AppCompatActivity {
    private CardView card_view_ql_truyen;
    private CardView card_view_ql_tl, card_view_ql_comment, card_view_ql_rating, card_view_ql_views;
    private Button button_dang_xuat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_quan_ly);
        card_view_ql_tl = findViewById(R.id.card_view_ql_tl);
        card_view_ql_truyen = findViewById(R.id.card_view_ql_truyen);
        card_view_ql_comment = findViewById(R.id.card_view_ql_comment);
        card_view_ql_rating = findViewById(R.id.card_view_ql_danh_gia);
        button_dang_xuat = findViewById(R.id.button_dang_xuat);
        card_view_ql_views= findViewById(R.id.card_view_ql_views);

        onClick();
    }

    private void onClick() {
        button_dang_xuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeQuanLy.this, DangNhapActivity.class));
                finish();
            }
        });
        card_view_ql_truyen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeQuanLy.this, QuanLyTruyen.class));
            }
        });
        card_view_ql_tl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeQuanLy.this, QuanLyTheLoai.class));
            }
        });

        card_view_ql_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeQuanLy.this, CommentManagement.class));
            }
        });

        card_view_ql_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeQuanLy.this, RatingManagement.class));
            }
        });

        card_view_ql_views.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeQuanLy.this, ChartActivity.class));
            }
        });
    }
}