package com.example.admin_comic.gui.QuanLyTruyen;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admin_comic.BUS.Utils;
import com.example.admin_comic.R;
import com.example.admin_comic.models.ApiResponse;
import com.example.admin_comic.models.Comic;
import com.example.admin_comic.models.Genre;
import com.example.admin_comic.models.Status;
import com.example.admin_comic.services.ApiService;
import com.example.admin_comic.services.ComicService;
import com.example.admin_comic.services.GenreService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuanLyTruyen extends AppCompatActivity {
    private FloatingActionButton floating_add;
    private static RecyclerView list_item_truyen;
    private ImageView image_back;
    public static final int chonphoto = 321;
    public static Uri selectImageUri;
    public static ImageView imageView, icSort;
    private EditText timkiem;

    public List<Genre> genres;

    public String sorted = "desc";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_truyen);
        floating_add = findViewById(R.id.floating_add);
        list_item_truyen = findViewById(R.id.list_item_truyen);
        image_back = findViewById(R.id.image_back);
        timkiem = findViewById(R.id.timkiem);
        icSort = findViewById(R.id.ic_sort);



        icSortOnClick();
        getGenres();

        GetListTruyen(this);
        floating_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTheTruyen();
            }
        });
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //Khởi tạo LinearLayoutManager cuộn dọc
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(QuanLyTruyen.this, LinearLayoutManager.VERTICAL, false);
        //Chuyền linearLayoutManager cho list_item_truyen
        list_item_truyen.setLayoutManager(linearLayoutManager);

        timkiem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (timkiem.getText().toString().trim().isEmpty()) {
                    GetListTruyen(QuanLyTruyen.this);
                } else {

                    ComicService comicService = ApiService.createService(ComicService.class);
                    Call<ApiResponse<List<Comic>>> call = comicService.searchComic(timkiem.getText().toString());

                    call.enqueue(new Callback<ApiResponse<List<Comic>>>() {
                        @Override
                        public void onResponse(Call<ApiResponse<List<Comic>>> call, Response<ApiResponse<List<Comic>>> response) {
                            List<Comic> comics = response.body().getData();

                            HomeQlItemTruyen homeQlItemTruyen = new HomeQlItemTruyen(comics, QuanLyTruyen.this);
                            //Chuyền Adapter homeQlItemTruyen cho list_item_truyen
                            list_item_truyen.setAdapter(homeQlItemTruyen);
                        }

                        @Override
                        public void onFailure(Call<ApiResponse<List<Comic>>> call, Throwable t) {

                        }
                    });

//                    //Khởi tạo HomeQlItemTruyen
//                    HomeQlItemTruyen homeQlItemTruyen = new HomeQlItemTruyen(timkiem_tt(timkiem.getText().toString().trim(),sqLiteDAO), QuanLyTruyen.this);
//                    //Chuyền Adapter homeQlItemTruyen cho list_item_truyen
//                    list_item_truyen.setAdapter(homeQlItemTruyen);
                }
            }
        });
    }

    private  void icSortOnClick(){
        icSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("desc".equals(sorted)) {
                    sorted = "asc";
                    icSort.setImageResource(R.drawable.sort_ascending);
                    GetListTruyen(QuanLyTruyen.this);
                } else if ("asc".equals(sorted)) {
                    sorted = "desc";
                    icSort.setImageResource(R.drawable.sort_descending);
                    GetListTruyen(QuanLyTruyen.this);
                }

            }
        });
    }

    private void addTheTruyen() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_truyentranh);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        window.setAttributes(layoutParams);
        dialog.setCancelable(true);
        TextView ten = dialog.findViewById(R.id.ten);
        imageView = dialog.findViewById(R.id.imageViewAnh);
        EditText editTextTenTruyen = dialog.findViewById(R.id.editTextTenTruyen);
        RadioButton radioButton = dialog.findViewById(R.id.radioButton);
        RadioButton radioButton2 = dialog.findViewById(R.id.radioButton2);
        Spinner spinner = dialog.findViewById(R.id.spinner);
        EditText editTextGioiThieu = dialog.findViewById(R.id.editTextGioiThieu);

        Button button_huy = dialog.findViewById(R.id.button_huy);
        Button button_them = dialog.findViewById(R.id.button_them);

        ten.setText("Thêm truyện tranh");


//        final ArrayAdapter adapter = new ArrayAdapter(QuanLyTruyen.this, R.layout.dropdown_item, getall_tl(sqLiteDAO));

        List<String> genreTitles = new ArrayList<>();
        for (Genre genre : genres) {
            String title = genre.getTitle();
            genreTitles.add(title);
        }

        final ArrayAdapter adapter = new ArrayAdapter(QuanLyTruyen.this,
                R.layout.dropdown_item, genreTitles);
        spinner.setAdapter(adapter);
//        spinner.setSelection(0);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (hasStoragePermission(QuanLyTruyen.this)) {
                    openImageChooser();
                } else {
                    ActivityCompat.requestPermissions(QuanLyTruyen.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, chonphoto);
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
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                try {

                    Integer selectedGenrePosition = spinner.getSelectedItemPosition();
                    Genre selectedGenre = genres.get(selectedGenrePosition);
                    int selectedGenreId = selectedGenre.getId();

                    String tenTruyenTranh = editTextTenTruyen.getText().toString().trim();
                    String gioiThieu = editTextGioiThieu.getText().toString().trim();
                    String trangThai = "";
                    if (radioButton.isChecked()) {
                        trangThai = "process";
                    }
                    if (radioButton2.isChecked()) {
                        trangThai = "end";
                    }
                    if (tenTruyenTranh.isEmpty() && gioiThieu.isEmpty() && trangThai.isEmpty()) {
                        Toast.makeText(QuanLyTruyen.this, "Vui lòng điền đủ thông tin!", Toast.LENGTH_SHORT).show();
                    } else {
                        if (selectImageUri != null) {
                            InputStream inputStream = getContentResolver().openInputStream(selectImageUri);


                            String base64Image1 = convertBitmapToBase64(((BitmapDrawable) imageView.getDrawable()).getBitmap());

                            ComicService comicService = ApiService.createService(ComicService.class);
                            Call<ApiResponse> call = comicService.createComic(tenTruyenTranh,
                                    trangThai, base64Image1, gioiThieu, selectedGenreId);

                            call.enqueue(new Callback<ApiResponse>() {
                                @Override
                                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                    ApiResponse res = response.body();
                                    if (res.getStatus() == Status.SUCCESS) {
                                        Toast.makeText(QuanLyTruyen.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                                        GetListTruyen(QuanLyTruyen.this);
                                        dialog.dismiss();
                                    } else {
                                        Toast.makeText(QuanLyTruyen.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ApiResponse> call, Throwable t) {

                                }
                            });
//                            if (them_truyentranh(truyenTranh, sqLiteDAO)) {
//                                Toast.makeText(QuanLyTruyen.this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
//                                GetListTruyen(QuanLyTruyen.this);
//                                dialog.dismiss();
//                            }


                        } else {
                            Toast.makeText(QuanLyTruyen.this, "Vui thêm ảnh!", Toast.LENGTH_SHORT).show();

                        }

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        dialog.show();
    }

    public static String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return "data:image/png;base64," + base64Image;
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, chonphoto);
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

    public static boolean hasStoragePermission(Context context) {
        int read = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
        return read == PackageManager.PERMISSION_GRANTED;

    }

    public static void GetListTruyen(QuanLyTruyen context) {
//        //Khởi tạo HomeQlItemTruyen
//        HomeQlItemTruyen homeQlItemTruyen = new HomeQlItemTruyen(getall_tt(sqLiteDAO), context);
//        //Chuyền Adapter homeQlItemTruyen cho list_item_truyen
//        list_item_truyen.setAdapter(homeQlItemTruyen);

        ComicService comicService = ApiService.createService(ComicService.class);
        Call<ApiResponse<List<Comic>>> call = comicService.getComics(context.timkiem.getText().toString().trim(), context.sorted);

        call.enqueue(new Callback<ApiResponse<List<Comic>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Comic>>> call, Response<ApiResponse<List<Comic>>> response) {
                List<Comic> comics = response.body().getData();
                if (comics != null) {
                    // do something
                    //Khởi tạo HomeQlItemTruyen
                    HomeQlItemTruyen homeQlItemTruyen = new HomeQlItemTruyen(comics, context);
                    //Chuyền Adapter homeQlItemTruyen cho list_item_truyen
                    list_item_truyen.setAdapter(homeQlItemTruyen);
                } else {
                    // do something
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Comic>>> call, Throwable t) {

            }
        });

    }

    private void getGenres() {
        GenreService genreService = ApiService.createService(GenreService.class);
        Call<ApiResponse<List<Genre>>> call = genreService.getGenres();

        call.enqueue(new Callback<ApiResponse<List<Genre>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Genre>>> call, Response<ApiResponse<List<Genre>>> response) {
                ApiResponse<List<Genre>> res = response.body();

                if (res.getData() != null) {
                    genres = res.getData();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Genre>>> call, Throwable t) {

            }
        });

    }
}