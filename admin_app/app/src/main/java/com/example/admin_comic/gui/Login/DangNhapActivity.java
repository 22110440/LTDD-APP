package com.example.admin_comic.gui.Login;

import static com.example.admin_comic.BUS.XuLySuKien.ANIMATION;
import static com.example.admin_comic.BUS.XuLySuKien.ANIMATIONDOWN;
import static com.example.admin_comic.BUS.XuLySuKien.ANIMATIONUP;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.admin_comic.MyApplication;
import com.example.admin_comic.R;
import com.example.admin_comic.gui.QuanLyTruyen.HomeQuanLy;
import com.example.admin_comic.models.ApiResponse;
import com.example.admin_comic.models.Status;
import com.example.admin_comic.models.User;
import com.example.admin_comic.services.ApiService;
import com.example.admin_comic.services.UserService;
import com.example.admin_comic.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DangNhapActivity extends AppCompatActivity {
    LottieAnimationView animationViewLogo;
    ImageView imageLogo;
    TextView textAppName, text_dang_ky;
    Button button_dang_nhap;
    EditText edit_taikhoan, edit_mat_khau;

    CharSequence charSequence;
    int index;
    long delay = 200;
    Handler handler = new Handler();

    private ProgressDialog progressDialog;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
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
        button_dang_nhap.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //Kiểm tra xem sự kiện bằng DOWN
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //Bắt hiệu ứng cho text
                    button_dang_nhap.startAnimation(ANIMATIONUP);
                } else {
                    //Kiểm tra xem sự kiện bằng UP
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        //Bắt hiệu ứng cho text
                        button_dang_nhap.startAnimation(ANIMATIONDOWN);
                        signIn();
                    }
                }
                return true;
            }
        });
    }

    private void signIn() {
        String tk = edit_taikhoan.getText().toString().trim();
        String mk = edit_mat_khau.getText().toString().trim();

        if (tk.isEmpty() || mk.isEmpty()) {
            Toast.makeText(DangNhapActivity.this, "Vui lòng điền đủ thông tin", Toast.LENGTH_SHORT).show();
        } else {

            progressDialog.setMessage("loading...");
            progressDialog.show();
            UserService userService = ApiService.createService(UserService.class);
            Call<ApiResponse<User>> call = userService.login(tk, mk);

            call.enqueue(new Callback<ApiResponse<User>>() {
                @Override
                public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                    progressDialog.dismiss();
                    ApiResponse<User> res = response.body();
                    if (res.getStatus() == Status.SUCCESS) {

                        Toast.makeText(DangNhapActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();

                        String username = edit_taikhoan.getText().toString().trim();
                        String fullname = res.getData().getFullname();
                        String token = res.getAccessToken();

                        saveDataLogin(username, fullname, token);
                        if (res.getData().getAdmin() == true) {
                            Intent intent = new Intent(DangNhapActivity.this, HomeQuanLy.class);
                            startActivity(intent);
                        } else {
                            // do something
                            Toast.makeText(DangNhapActivity.this,"Quyền truy cập không hợp lệ", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(DangNhapActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }

                @Override
                public void onFailure(Call<ApiResponse<User>> call, Throwable t) {

                }
            });
        }


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
        //Khai báo hoạt ảnh
        ANIMATION(this);
        //Khai báo id
        animationViewLogo = findViewById(R.id.animationViewLogo);
        imageLogo = findViewById(R.id.imageLogo);
        textAppName = findViewById(R.id.textAppName);
        edit_taikhoan = findViewById(R.id.edit_taikhoan);
        edit_mat_khau = findViewById(R.id.edit_mat_khau);
        button_dang_nhap = findViewById(R.id.button_dang_nhap);


        progressDialog = new ProgressDialog(this);

        sharedPreferences = getSharedPreferences("remember_login", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
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

    public void saveDataLogin(String username, String fullname, String token) {
//        editor.putString("username", username);
//        editor.putString("fullname", fullname);
        editor.putString(Constants.ACCESS_TOKEN, token);
        MyApplication.saveToken(token);
        editor.commit();
    }

}