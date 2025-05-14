package com.example.admin_comic.gui.QuanLyTruyen;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admin_comic.BUS.Utils;
import com.example.admin_comic.R;
import com.example.admin_comic.models.ApiResponse;
import com.example.admin_comic.models.Chapter;
import com.example.admin_comic.models.ImageChapter;
import com.example.admin_comic.models.Status;
import com.example.admin_comic.services.ApiService;
import com.example.admin_comic.services.ComicService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class addChap extends AppCompatActivity {
    FloatingActionButton floating_add;
    public static RecyclerView list_item_ql_ct;
    ImageView img_back;


    public static final int chonphoto = 321;
    public static int chonphoto2 = 32122;
    TextView text_luu, textTieude;
    Intent intent;

    public static List<ImageChapter> imageList = new ArrayList<>();
    public static QLChapItemImage adapter;

    int comicId;
    int chapterId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_chap);
        img_back = findViewById(R.id.img_back);
        floating_add = findViewById(R.id.floating_add);
        list_item_ql_ct = findViewById(R.id.list_item_ql_ct);
        text_luu = findViewById(R.id.text_luu);
        textTieude = findViewById(R.id.textTieude);
        intent = getIntent();
        comicId = intent.getIntExtra("Key_idTruyen", 0);


        chapterId = intent.getIntExtra("Key_idTruyen", 0);


        if (intent.getStringExtra("Key_tenChap") != null) {
            textTieude.setText("Sửa chap truyện");


            ComicService comicService = ApiService.createService(ComicService.class);
            Call<ApiResponse<Chapter>> call = comicService.getChapter(chapterId);

            call.enqueue(new Callback<ApiResponse<Chapter>>() {
                @Override
                public void onResponse(Call<ApiResponse<Chapter>> call, Response<ApiResponse<Chapter>> response) {

                    ApiResponse res = response.body();
                    if (res.getStatus() == Status.SUCCESS) {
                        Chapter chapter = response.body().getData();
                        for (int i = 0; i < chapter.getImage().size(); i++) {
                            String imagePath = chapter.getImage().get(i);
                            ImageChapter imageItem = new ImageChapter(imagePath);
                            imageList.add(imageItem);
                        }
                        GetListImgChap(addChap.this);

                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<Chapter>> call, Throwable t) {

                }
            });

        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(addChap.this, LinearLayoutManager.VERTICAL, false);
        list_item_ql_ct.setLayoutManager(linearLayoutManager);
        adapter = new QLChapItemImage(this, imageList);
        list_item_ql_ct.setAdapter(adapter);
        floating_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasStoragePermissionImg(addChap.this)) {
                    openImageChooserImg();
                } else {
                    ActivityCompat.requestPermissions(addChap.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, chonphoto);
                }
            }
        });
        text_luu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Action Update
                if (imageList.size() == 0) {
                    Toast.makeText(addChap.this, "Vui lòng chọn ảnh chap!", Toast.LENGTH_SHORT).show();
                } else if (intent.getStringExtra("Key_tenChap") != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(addChap.this);
                    builder.setMessage("Bạn có muốn sửa chap này không!")
                            .setPositiveButton("sửa", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
//                                    sua_img(intent.getStringExtra("Key_tenChap"), sqLiteDAO);
//                                    for (int i = 0; i < list.size(); i++) {
//                                        imgChap imgChap = new imgChap(0, intent.getIntExtra("Key_idTruyen", 0), intent.getStringExtra("Key_tenChap"), list.get(i).getImg());
//                                        them_imgchaptruyen(imgChap, sqLiteDAO);
//                                        if (i == list.size() - 1) {
//                                            Toast.makeText(addChap.this, "Sửa thành công!", Toast.LENGTH_SHORT).show();
//                                            onBackPressed();
//                                            dialog.dismiss();
//                                        }
//                                    }

                                    ComicService comicService = ApiService.createService(ComicService.class);

                                    List<String> links = new ArrayList<>();
                                    for (int i = 0; i < imageList.size(); i++) {
                                        ImageChapter imageItem = imageList.get(i);
                                        String imagePath = imageItem.getImagePath();

                                        if (imagePath.startsWith("content://") || imagePath.startsWith("file://")) {
                                            // imagePath có thể là một Uri
                                            try {
                                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(addChap.this.getContentResolver(),
                                                        Uri.parse(imagePath));
                                                String base64Image = convertBitmapToBase64(bitmap);
                                                links.add(base64Image);
                                            } catch (IOException e) {

                                            }

                                        } else {
                                            try {
                                                links.add(imagePath);
                                            } catch (Exception e) {

                                            }
                                        }


                                    }


                                    String newTitle = intent.getStringExtra("Key_tenChap");
                                    Call<ApiResponse> call = comicService.updateChapter(chapterId, newTitle, links);

                                    call.enqueue(new Callback<ApiResponse>() {
                                        @Override
                                        public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                            ApiResponse res = response.body();
                                            if (res.getStatus() == Status.SUCCESS) {
                                                Toast.makeText(addChap.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                                                onBackPressed();
                                                dialog.dismiss();
                                            } else {
                                                // do something
                                                Toast.makeText(addChap.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ApiResponse> call, Throwable t) {

                                        }
                                    });
                                }


                            })
                            .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });

                    builder.create();
                    builder.show();
                } else {
                    // Action create
                    luuChap();
                }
            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                imageList.clear();
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Xóa tất cả các phần tử trong danh sách imageList
        imageList.clear();
        // Cập nhật RecyclerView (nếu cần)
        adapter.notifyDataSetChanged();
        // Gọi lại phương thức onBackPressed() của lớp cha
        super.onBackPressed();
    }

    private void luuChap() {
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

        EditText edit_tenchap = dialog.findViewById(R.id.edit_addtl);
        TextView text_view = dialog.findViewById(R.id.text_view);
        Button button_huy = dialog.findViewById(R.id.button_huy);
        Button button_them = dialog.findViewById(R.id.button_them);
        text_view.setText("Lưu chap truyện");
        edit_tenchap.setHint("Nhập tên chap");
        button_them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tenchap = edit_tenchap.getText().toString().trim();
                if (tenchap.isEmpty()) {
                    Toast.makeText(dialog.getContext(), "Vui lòng nhập tên chap!", Toast.LENGTH_SHORT).show();
                }
                if (imageList.size() == 0) {
                    Toast.makeText(dialog.getContext(), "Vui lòng chọn ảnh chap!", Toast.LENGTH_SHORT).show();
                } else {
                    ComicService comicService = ApiService.createService(ComicService.class);

                    List<String> links = new ArrayList<>();
                    for (int i = 0; i < imageList.size(); i++) {
                        ImageChapter imageItem = imageList.get(i);
                        String imagePath = imageItem.getImagePath();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(addChap.this.getContentResolver(),
                                    Uri.parse(imagePath));
                            String base64Image = convertBitmapToBase64(bitmap);
                            links.add(base64Image);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }


                    Call<ApiResponse> call = comicService.createChapter(comicId, tenchap, links);
                    call.enqueue(new Callback<ApiResponse>() {
                        @Override
                        public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                            if(response.code() == 200) {
                                ApiResponse res = response.body();
                                if (res.getStatus() == Status.SUCCESS) {
                                    Toast.makeText(dialog.getContext(), res.getMessage(), Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                    dialog.dismiss();


                                } else {
                                    // do something
                                }
                            }

                            else{
                                Toast.makeText(dialog.getContext(),"Tài nguyên quá tải", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }

                        }

                        @Override
                        public void onFailure(Call<ApiResponse> call, Throwable t) {
                            // do something
                        }
                    });
                }

//                else {
//
//
//                    if (kiem_tra_chap(tenchap,intent.getIntExtra("Key_idTruyen",0))) {
//                        Toast.makeText(dialog.getContext(), "Tên thể chap đã tồn tại!", Toast.LENGTH_SHORT).show();
//                    } else {
//                        if (list.size() > 0) {
//                            Chap chap = new Chap(0, intent.getIntExtra("Key_idTruyen", 0), tenchap);
//                            if (them_chaptruyen(chap, sqLiteDAO)) {
//                                for (int i = 0; i < list.size(); i++) {
//                                    imgChap imgChap = new imgChap(0,intent.getIntExtra("Key_idTruyen",0), tenchap, list.get(i).getImg());
//                                    them_imgchaptruyen(imgChap, sqLiteDAO);
//                                    if (i == list.size()-1) {
//                                        Toast.makeText(dialog.getContext(), "Thêm thành công!", Toast.LENGTH_SHORT).show();
//                                        onBackPressed();
//                                        dialog.dismiss();
//                                    }
//                                }
//
//                            }
//                        }
//                        else {
//                            Toast.makeText(dialog.getContext(), "vui lòng thêm ảnh !", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }
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

    public String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return "data:image/png;base64," + base64Image;
    }

    public static void GetListImgChap(addChap context) {
//        QLChapItemImg chapItemImg = new QLChapItemImg(list, context);
//        list_item_ql_ct.setAdapter(chapItemImg);
        adapter = new QLChapItemImage(context, imageList);
        list_item_ql_ct.setAdapter(adapter);
    }

    public static boolean hasStoragePermissionImg(Context context) {
        int read = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
        return read == PackageManager.PERMISSION_GRANTED;

    }

    private void openImageChooserImg() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, chonphoto);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == chonphoto) {
                try {
//                    Uri selectImageUri = data.getData();
//                    InputStream inputStream = getContentResolver().openInputStream(selectImageUri);
//
//                    GetLitImg(inputStream,addChap.this);
                    Uri selectedImageUri = data.getData();
                    String imagePath = selectedImageUri.toString();
                    ImageChapter imageItem = new ImageChapter(imagePath);
                    imageList.add(imageItem);
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (requestCode == chonphoto2) {
                try {
//                    Uri selectImageUri = data.getData();
//                    InputStream inputStream = getContentResolver().openInputStream(selectImageUri);
//                    GetUpdateImg(inputStream,addChap.this,chonphoto2);

                    Uri selectImageUri = data.getData();
                    String imagePath = selectImageUri.toString();
                    ImageChapter imageItem = new ImageChapter(imagePath);
                    imageList.set(chonphoto2, imageItem);
                    adapter.notifyDataSetChanged();


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

}