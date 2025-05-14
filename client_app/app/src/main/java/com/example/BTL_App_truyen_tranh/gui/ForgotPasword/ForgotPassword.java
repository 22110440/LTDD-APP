package com.example.BTL_App_truyen_tranh.gui.ForgotPasword;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.BTL_App_truyen_tranh.R;
import com.example.BTL_App_truyen_tranh.models.ApiResponse;
import com.example.BTL_App_truyen_tranh.models.Status;
import com.example.BTL_App_truyen_tranh.services.ApiService;
import com.example.BTL_App_truyen_tranh.services.UserService;
import com.example.BTL_App_truyen_tranh.utils.Validate;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPassword extends AppCompatActivity {

    ImageView icBack;
    EditText email;
    Button btnSumit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        initUI();
        handleBackPressed();
        btnSubmitOnClick();

    }

    private void initUI() {
        icBack = findViewById(R.id.ic_back);
        btnSumit = findViewById(R.id.submit);
        email = findViewById(R.id.email);
    }

    private void handleBackPressed() {
        icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void btnSubmitOnClick() {
        btnSumit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Validate.isValidEmail(email.getText().toString().trim()) == false) {
                    Toast.makeText(ForgotPassword.this, "Địa chỉ email không hợp lệ", Toast.LENGTH_SHORT).show();
                }
                else  {
                    UserService userService = ApiService.createService(UserService.class);
                    Call<ApiResponse> call = userService.forgotPassword(email.getText().toString().trim());
                    call.enqueue(new Callback<ApiResponse>() {
                        @Override
                        public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                            if (response.body().getStatus() == Status.SUCCESS) {
                                Toast.makeText(ForgotPassword.this,
                                        response.body().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse> call, Throwable t) {

                            // do something

                        }
                    });

                }
            }
        });


    }
}