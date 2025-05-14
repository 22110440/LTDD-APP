package com.example.BTL_App_truyen_tranh.gui.Home;

import com.squareup.picasso.Picasso;
import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.view.animation.AnimationUtils;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.BTL_App_truyen_tranh.R;
import com.example.BTL_App_truyen_tranh.gui.ChangePassword.ChangePasswordActivity;
import com.example.BTL_App_truyen_tranh.gui.Favorites.FavoritesActivity;
import com.example.BTL_App_truyen_tranh.gui.chart.ChartActivity;
import com.example.BTL_App_truyen_tranh.models.ApiResponse;
import com.example.BTL_App_truyen_tranh.models.Comic;
import com.example.BTL_App_truyen_tranh.models.Genre;
import com.example.BTL_App_truyen_tranh.models.Status;
import com.example.BTL_App_truyen_tranh.models.User;
import com.example.BTL_App_truyen_tranh.services.ApiService;
import com.example.BTL_App_truyen_tranh.services.ComicService;
import com.example.BTL_App_truyen_tranh.services.GenreService;
import com.example.BTL_App_truyen_tranh.services.UserService;
import com.example.BTL_App_truyen_tranh.utils.Convert;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePage extends AppCompatActivity {
    private RecyclerView list_item_truyen;

    private RecyclerView list_the_loai;

    private TextView text_name;
    public EditText timkiem;
    private ViewFlipper viewFlipper;
    public static final int chonphoto = 321;

    public List<Genre> genres;

    ImageView avatar;

    User user;

    ImageView imageView;

    public static Uri selectImageUri;

    FloatingActionButton floatingActionButton, fab2;

    ImageView icSetting, icFilter;

    public String sorted = "desc";

    HomeItemTruyen homeQlItemTruyen;

    List<Comic> comics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        list_item_truyen = findViewById(R.id.list_item_truyen);
        list_the_loai = findViewById(R.id.list_the_loai);
        text_name = findViewById(R.id.text_name);
        avatar = findViewById(R.id.profile_image);
        floatingActionButton = findViewById(R.id.fab);
        fab2= findViewById(R.id.fab2);
        icSetting = findViewById(R.id.ic_setting);
        icFilter = findViewById(R.id.ic_filter);
        viewFlipper=findViewById(R.id.viewflipper);
        Intent intent = getIntent();
        text_name.setText(intent.getStringExtra("Key_hoten"));
        timkiem = findViewById(R.id.timkiem);


        fabOnClick();


        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(HomePage.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_edit_profile);
                Window window = dialog.getWindow();
                if (window == null) {
                    return;
                }
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                WindowManager.LayoutParams layoutParams = window.getAttributes();
                window.setAttributes(layoutParams);
                dialog.setCancelable(true);

                EditText fullname = dialog.findViewById(R.id.fullname);
                imageView = dialog.findViewById(R.id.imageViewAnh);
                Button button_huy = dialog.findViewById(R.id.button_huy);
                Button button_them = dialog.findViewById(R.id.button_save);

                fullname.setText(user.getFullname());

                if (user.getPhotoURL() != null) {
                    Glide.with(HomePage.this).load(user.getPhotoURL()).into(imageView);
                }

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (hasStoragePermission(HomePage.this)
                        ) {
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            startActivityForResult(intent, chonphoto);
                        } else {

                            ActivityCompat.requestPermissions(HomePage.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, chonphoto);
                        }

                    }
                });

                button_huy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                button_them.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (fullname.getText().toString().trim().isEmpty()) {
                            Toast.makeText(HomePage.this,
                                    "Vui lòng nhập tên",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            String base64Image1 = Convert.convertBitmapToBase64(((BitmapDrawable) imageView.getDrawable()).getBitmap());

                            UserService userService = ApiService.createService(UserService.class);
                            Call<ApiResponse<User>> call = userService.updateUser(fullname.getText().toString(),
                                    base64Image1
                            );

                            call.enqueue(new Callback<ApiResponse<User>>() {
                                @Override
                                public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                                    if (response.body().getStatus() == Status.SUCCESS) {

                                        Toast.makeText(HomePage.this, response.body().getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                        getProfile();
                                        dialog.dismiss();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ApiResponse<User>> call, Throwable t) {

                                }
                            });


                        }


                    }
                });

                dialog.show();
            }
        });

        //Khởi tạo GridLayoutManager Có số cột là 2
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        //Chuyền Layout Manager cho list_item_truyen
        list_item_truyen.setLayoutManager(gridLayoutManager);
        timkiem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                GetListTruyen();
                if (!timkiem.getText().toString().trim().isEmpty()) {
                    GetListTruyen();
                } else {
//                    ComicService comicService = ApiService.createService(ComicService.class);
//                    Call<ApiResponse<List<Comic>>> call = comicService.searchComic(timkiem.getText().toString());
//
//                    call.enqueue(new Callback<ApiResponse<List<Comic>>>() {
//                        @Override
//                        public void onResponse(Call<ApiResponse<List<Comic>>> call, Response<ApiResponse<List<Comic>>> response) {
//                            List<Comic> comics = response.body().getData();
//
//                            HomeItemTruyen homeQlItemTruyen = new HomeItemTruyen(comics, HomePage.this);
//                            //Chuyền Adapter homeQlItemTruyen cho list_item_truyen
//                            list_item_truyen.setAdapter(homeQlItemTruyen);
//                        }
//
//                        @Override
//                        public void onFailure(Call<ApiResponse<List<Comic>>> call, Throwable t) {
//
//                        }
//                    });

                }
            }
        });


        GetListTruyen();
        getGenres();
        getProfile();
        filterOnClick();
        icSettingOnClick();
        ActionViewFlipper();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == chonphoto) {

                selectImageUri = data.getData();
                if (selectImageUri != null) {
                    Uri uri = data.getData();
                    imageView.setImageURI(uri);
                }
            }
        }

    }

    private void icSettingOnClick() {
        icSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

    }

    public static boolean hasStoragePermission(Context context) {
        int read = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
        return read == PackageManager.PERMISSION_GRANTED;

    }

    private  void filterOnClick () {
        icFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("desc".equals(sorted)) {
                    sorted = "asc";
                    icFilter.setImageResource(R.drawable.sort_ascending);
                    GetListTruyen();
                } else if ("asc".equals(sorted)) {
                    sorted = "desc";
                    icFilter.setImageResource(R.drawable.sort_descending);
                    GetListTruyen();
                }

            }
        });
    }

    private void fabOnClick () {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, ChartActivity.class);
                startActivity(intent);
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, FavoritesActivity.class);
                startActivity(intent);
            }
        });
    }



    public void GetListTruyen() {
        ComicService comicService = ApiService.createService(ComicService.class);
        Call<ApiResponse<List<Comic>>> call = comicService.getComics(timkiem.getText().toString().trim(), sorted);

        call.enqueue(new Callback<ApiResponse<List<Comic>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Comic>>> call, Response<ApiResponse<List<Comic>>> response) {
                comics = response.body().getData();
                if (comics != null && comics.size() != 0) {
                    // do something
                    homeQlItemTruyen = new HomeItemTruyen(comics, HomePage.this);
                    list_item_truyen.setAdapter(homeQlItemTruyen);
                } else {
                    // do something
                    list_item_truyen.setAdapter(null);

                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Comic>>> call, Throwable t) {

            }
        });
    }

    public void getGenres() {
        GenreService genreService = ApiService.createService(GenreService.class);
        Call<ApiResponse<List<Genre>>> call = genreService.getGenres();

        call.enqueue(new Callback<ApiResponse<List<Genre>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Genre>>> call, Response<ApiResponse<List<Genre>>> response) {
                ApiResponse<List<Genre>> res = response.body();

                if (res.getData() != null) {
                    genres = res.getData();
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HomePage.this, LinearLayoutManager.HORIZONTAL, false);
                    list_the_loai.setLayoutManager(linearLayoutManager);
                    HomeItemTheloai homeItemTheloai = new HomeItemTheloai(genres, HomePage.this);
                    list_the_loai.setAdapter(homeItemTheloai);
                } else {
                    // do something
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Genre>>> call, Throwable t) {

            }
        });

    }
    private void ActionViewFlipper() {
        ArrayList<String> mangquangcao = new ArrayList<>();
        mangquangcao.add("https://sachsuthattphcm.com.vn/wp-content/uploads/2020/04/banner-sach-T4.2020-3.jpg");
        mangquangcao.add("https://bookish.vn/wp-content/uploads/2020/04/Banner-o-nha-doc-sach-925x412px-01.jpg");
        mangquangcao.add("https://toquoc.mediacdn.vn/280518851207290880/2022/7/26/banner141200x628-16587434648621017256903-1658811001657-16588110017811422256770.png");

        for (int i = 0; i < mangquangcao.size(); i++) {
            ImageView imageView = new ImageView(this); // Sử dụng "this" để lấy context của Activity
            Picasso.get().load(mangquangcao.get(i)).into(imageView);

            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            viewFlipper.addView(imageView);
        }

        viewFlipper.setFlipInterval(1900);
        viewFlipper.setAutoStart(true);

        Animation animation_slide_in = AnimationUtils.loadAnimation(this, R.anim.slide_in_right); // Sử dụng "this" để lấy context của Activity
        Animation animation_slide_out = AnimationUtils.loadAnimation(this, R.anim.slide_out_right); // Sử dụng "this" để lấy context của Activity

        viewFlipper.setInAnimation(animation_slide_in);
        viewFlipper.setInAnimation(animation_slide_out);
    }

    public void getProfile() {
        UserService userService = ApiService.createService(UserService.class);
        Call<ApiResponse<User>> call = userService.getUser();

        call.enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                if (response.body().getStatus() == Status.SUCCESS) {
                    user = response.body().getData();
                    if (user.getPhotoURL() != null) {
                        Glide.with(HomePage.this).load(user.getPhotoURL()).into(avatar);
                    }
                    text_name.setText(user.getFullname());

                }
            }

            @Override
            public void onFailure(Call<ApiResponse<User>> call, Throwable t) {

            }
        });
    }


}