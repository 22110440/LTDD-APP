package com.example.BTL_App_truyen_tranh.gui.ChangePassword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.BTL_App_truyen_tranh.R;
import com.example.BTL_App_truyen_tranh.gui.ForgotPasword.ForgotPassword;
import com.example.BTL_App_truyen_tranh.gui.Login.DangNhapActivity;
import com.example.BTL_App_truyen_tranh.models.ApiResponse;
import com.example.BTL_App_truyen_tranh.models.Status;
import com.example.BTL_App_truyen_tranh.services.ApiService;
import com.example.BTL_App_truyen_tranh.services.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    ImageView icBack;
    public EditText currentPass,newPass;
    Button btnSumit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        initUI();
        handleBackPressed();
        btnSubmitOnClick();
    }

    private void initUI() {
        icBack = findViewById(R.id.ic_back);
        btnSumit = findViewById(R.id.submit_change);
        currentPass= findViewById(R.id.current_pass);
        newPass= findViewById(R.id.new_pass);
    }

    private void handleBackPressed() {
        icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void navigateToLogin(){
        Intent intent = new Intent(this, DangNhapActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private  void btnSubmitOnClick(){

       btnSumit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(currentPass.getText().toString().isEmpty() || newPass.getText().toString().isEmpty() ){
                   Toast.makeText(ChangePasswordActivity.this, "Vui lòng nhập đủ trường", Toast.LENGTH_SHORT).show();
               }
               else {
                   UserService userService = ApiService.createService(UserService.class);
                   Call<ApiResponse> call = userService.changePassword(currentPass.getText().toString().trim(),
                           newPass.getText().toString().trim());

                   call.enqueue(new Callback<ApiResponse>() {
                       @Override
                       public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                           if (response.body().getStatus() == Status.SUCCESS) {
                               Toast.makeText(ChangePasswordActivity.this,
                                       response.body().getMessage(),
                                       Toast.LENGTH_SHORT).show();
                               navigateToLogin();
                           }
                           else {
                               Toast.makeText(ChangePasswordActivity.this,
                                       response.body().getMessage(),
                                       Toast.LENGTH_SHORT).show();
                           }
                       }

                       @Override
                       public void onFailure(Call<ApiResponse> call, Throwable t) {

                       }
                   });
               }
           }
       });

    }

}