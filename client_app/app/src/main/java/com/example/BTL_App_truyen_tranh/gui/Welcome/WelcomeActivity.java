package com.example.BTL_App_truyen_tranh.gui.Welcome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.BTL_App_truyen_tranh.gui.Login.DangNhapActivity;
import com.example.BTL_App_truyen_tranh.R;

public class WelcomeActivity extends AppCompatActivity {
    LottieAnimationView animationViewLogo;
    ImageView imageLogo;
    TextView textAppName;
    CharSequence charSequence;
    int index;
    long delay = 200;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        anhXa();
        //Khởi tạo animation
        Animation animationImageLogo = AnimationUtils.loadAnimation(this, R.anim.top_wave);
        //Bắt đầu animation
        imageLogo.setAnimation(animationImageLogo);
        //Khởi tạo animation
        Animation animationZoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        //Bắt đầu animation
        animationViewLogo.setAnimation(animationZoomIn);

        animatText("NetTruyen");
        // Khởi tạo handler
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Chuyển hướng tớ trang DangNhapActivity
                startActivity(new Intent(WelcomeActivity.this, DangNhapActivity.class));
                //Kết thúc activity
                finish();
            }
        }, 4000);
    }

    private void anhXa() {
        //Khai báo id
        animationViewLogo = findViewById(R.id.animationViewLogo);
        imageLogo = findViewById(R.id.imageLogo);
        textAppName = findViewById(R.id.textAppName);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //Khi được chạy
            //Set Text
            textAppName.setText(charSequence.subSequence(0, index++));
            //Kiểm tra tình trạng
            if (index <= charSequence.length()) {
                handler.postDelayed(runnable, delay);
            }
        }
    };

    //Tạo phương pháp văn bản động
    public void animatText(CharSequence cs) {
        //Set text
        charSequence = cs;
        //Đặt lại index về 0;
        index = 0;
        //Xóa văn bản
        textAppName.setText("");
        //Xóa cuộc gọi lại
        handler.removeCallbacks(runnable);
        //Chayk handler
        handler.postDelayed(runnable, delay);
    }
}