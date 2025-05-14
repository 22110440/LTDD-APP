package com.example.BTL_App_truyen_tranh.gui.Login;

import static com.example.BTL_App_truyen_tranh.BUS.XuLySuKien.ANIMATION;
import static com.example.BTL_App_truyen_tranh.BUS.XuLySuKien.ANIMATIONDOWN;
import static com.example.BTL_App_truyen_tranh.BUS.XuLySuKien.ANIMATIONUP;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.BTL_App_truyen_tranh.models.ApiResponse;
import com.example.BTL_App_truyen_tranh.models.Status;
import com.example.BTL_App_truyen_tranh.models.User;
import com.example.BTL_App_truyen_tranh.services.ApiService;
import com.example.BTL_App_truyen_tranh.R;
import com.example.BTL_App_truyen_tranh.services.UserService;
import com.example.BTL_App_truyen_tranh.utils.Validate;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DangKyActivity extends AppCompatActivity {
    LottieAnimationView animationViewLogo;
    ImageView imageLogo;
    TextView textAppName,text_dang_nhap;
    EditText edit_ho_ten,edit_mat_khau,edit_taikhoan, email;
    Button button_dang_ky;
    CharSequence charSequence;
    int index;
    long delay = 200;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
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

        onClick();
        //Tạo hoạn ảnh cho nút ấn


    }

    private void onClick() {

        text_dang_nhap.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //Kiểm tra xem sự kiện bằng DOWN
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //Bắt hiệu ứng cho text
                    text_dang_nhap.startAnimation(ANIMATIONUP);
                } else {
                    //Kiểm tra xem sự kiện bằng UP
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        //Bắt hiệu ứng cho text
                        text_dang_nhap.startAnimation(ANIMATIONDOWN);

                        startActivity(new Intent(DangKyActivity.this, DangNhapActivity.class));
                        finish();
                    }
                }
                return true;
            }
        });



        button_dang_ky.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //Kiểm tra xem sự kiện bằng DOWN
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //Bắt hiệu ứng cho text
                    button_dang_ky.startAnimation(ANIMATIONUP);
                } else {
                    //Kiểm tra xem sự kiện bằng UP
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        //Bắt hiệu ứng cho text
                        button_dang_ky.startAnimation(ANIMATIONDOWN);
                        String tk = edit_taikhoan.getText().toString().trim();
                        String hoten = edit_ho_ten.getText().toString().trim();
                        String matkhau = edit_mat_khau.getText().toString().trim();
                        if (tk.isEmpty() && hoten.isEmpty() && matkhau.isEmpty() && email.getText().toString().trim().isEmpty()) {
                            Toast.makeText(DangKyActivity.this, "Vui lòng ko để trống thông tin", Toast.LENGTH_SHORT).show();
                        }
                        else if(Validate.isValidEmail(email.getText().toString()) == false){
                            Toast.makeText(DangKyActivity.this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
                        }

                             else {

                                UserService userService = ApiService.createService(UserService.class);
                                Call<ApiResponse> call = userService.signup(tk, matkhau, hoten, email.getText().toString().trim());

                                call.enqueue(new Callback<ApiResponse>() {
                                    @Override
                                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                                        ApiResponse<User> res = response.body();
                                        if (res.getStatus() == Status.SUCCESS) {
                                            Toast.makeText(getApplication(), res.getMessage(), Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(DangKyActivity.this, DangNhapActivity.class);
                                            startActivity(intent);
                                            finishAffinity();
                                        } else {
                                            Toast.makeText(getApplication(), res.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ApiResponse> call, Throwable t) {

                                    }
                                });




                            }

                    }
                }
                return true;
            }
        });
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

    private void anhXa() {
        ANIMATION(this);
        animationViewLogo = findViewById(R.id.animationViewLogo);
        imageLogo = findViewById(R.id.imageLogo);
        textAppName = findViewById(R.id.textAppName);
        text_dang_nhap = findViewById(R.id.text_dang_nhap);
        button_dang_ky = findViewById(R.id.button_dang_ky);
        edit_ho_ten = findViewById(R.id.edit_ho_ten);
        edit_mat_khau = findViewById(R.id.edit_mat_khau);
        edit_taikhoan = findViewById(R.id.edit_taikhoan);
        email = findViewById(R.id.email);

    }

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