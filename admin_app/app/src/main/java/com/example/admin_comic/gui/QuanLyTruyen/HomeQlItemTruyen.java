package com.example.admin_comic.gui.QuanLyTruyen;

import static com.example.admin_comic.gui.QuanLyTruyen.QuanLyTruyen.GetListTruyen;
import static com.example.admin_comic.gui.QuanLyTruyen.QuanLyTruyen.chonphoto;
import static com.example.admin_comic.gui.QuanLyTruyen.QuanLyTruyen.convertBitmapToBase64;
import static com.example.admin_comic.gui.QuanLyTruyen.QuanLyTruyen.hasStoragePermission;
import static com.example.admin_comic.gui.QuanLyTruyen.QuanLyTruyen.imageView;
import static com.example.admin_comic.gui.QuanLyTruyen.QuanLyTruyen.selectImageUri;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.admin_comic.R;
import com.example.admin_comic.models.ApiResponse;
import com.example.admin_comic.models.Comic;
import com.example.admin_comic.models.Genre;
import com.example.admin_comic.models.Status;
import com.example.admin_comic.services.ApiService;
import com.example.admin_comic.services.ComicService;
import com.example.admin_comic.services.GenreService;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeQlItemTruyen extends RecyclerView.Adapter<HomeQlItemTruyen.HomeQlItemTruyenHolder> {
    private final List<Comic> listTruyen;
    private final QuanLyTruyen context;



    public HomeQlItemTruyen(List<Comic> listTruyen, QuanLyTruyen context) {
        this.listTruyen = listTruyen;
        this.context = context;
    }

    @NonNull
    @Override
    public HomeQlItemTruyenHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ql_truyen, parent, false);
        return new HomeQlItemTruyenHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeQlItemTruyenHolder holder, int position) {
        Comic truyenTranh = listTruyen.get(position);
        holder.textid.setText(truyenTranh.getId() + "");
        holder.text_ten_truyen.setText(truyenTranh.getTitle());



        if (truyenTranh.getGenre() != null) {
            holder.text_theLoai.setText(truyenTranh.getGenre().getTitle());
        } else {

        }

        Glide.with(context)
                .load(truyenTranh.getPoster())
                .into(holder.image_truyen);

        holder.image_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GenreService genreService = ApiService.createService(GenreService.class);
                Call<ApiResponse<List<Genre>>> call = genreService.getGenres();

                call.enqueue(new Callback<ApiResponse<List<Genre>>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<List<Genre>>> call, Response<ApiResponse<List<Genre>>> response) {
                        ApiResponse<List<Genre>> res = response.body();
                        List<Genre> genres;
                        if(res.getData() !=null) {
                            genres = res.getData();
                            suatruyentranh(truyenTranh.getId(), truyenTranh, genres);
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<List<Genre>>> call, Throwable t) {

                    }
                });


            }
        });
        holder.image_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xoaTruyen(truyenTranh.getId());
            }
        });
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, QlChapTruyen.class);
                intent.putExtra("Key_idTruyen", truyenTranh.getId());
                context.startActivity(intent);
            }
        });
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        context.startActivityForResult(intent, chonphoto);
    }

    private void suatruyentranh(int id, Comic truyenTranhs, List<Genre> genres) {
        Dialog dialog = new Dialog(context);
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

        List<String> genreTitles = new ArrayList<>();
        for (Genre genre : genres) {
            String title = genre.getTitle();
            genreTitles.add(title);
        }


        Genre currentGenre = truyenTranhs.getGenre();

        final ArrayAdapter adapter = new ArrayAdapter(context, R.layout.dropdown_item, genreTitles);
        spinner.setAdapter(adapter);
        int giatri = -1;

        if(truyenTranhs.getGenre() != null){
            int currentGenreId= currentGenre.getId();
            for(int i=0; i< genres.size(); i++) {
                if(genres.get(i).getId() == currentGenreId){
                    giatri=i;
                    break;
                }
            }
        }
//        for (int i = 0; i < getall_tl(sqLiteDAO).size(); i++) {
//            if (getall_tl(sqLiteDAO).get(i).getTenTheLoai().equalsIgnoreCase(truyenTranhs.getTheLoai())) {
//                giatri = i;
//                break;
//            }
//        }
        spinner.setSelection(giatri);
        ten.setText("Sửa truyện tranh");
        button_them.setText("Sửa");
        editTextTenTruyen.setText(truyenTranhs.getTitle());
        editTextGioiThieu.setText(truyenTranhs.getDescription());
        if ("process".equals(truyenTranhs.getStatus())) {
            radioButton.setChecked(true);
        } else {
            radioButton2.setChecked(true);
        }


//        imageView.setImageBitmap(Utils.getImage(truyenTranhs.getPoster()));
        Glide.with(context)
                .load(truyenTranhs.getPoster())
                .into(imageView);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (hasStoragePermission(context)) {
                    openImageChooser();
                } else {
                    ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, chonphoto);
                }
            }
        });
        button_them.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                try {
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
                        Toast.makeText(context, "Vui lòng điền đủ thông tin!", Toast.LENGTH_SHORT).show();
                    } else {

                        int comicId = truyenTranhs.getId();
                        Integer selectedGenrePosition = spinner.getSelectedItemPosition();
                        Genre selectedGenre = genres.get(selectedGenrePosition);
                        int selectedGenreId = selectedGenre.getId();

                        String base64Image1 = convertBitmapToBase64(((BitmapDrawable) imageView.getDrawable()).getBitmap());

                        ComicService comicService = ApiService.createService(ComicService.class);
                        Call<ApiResponse> call = comicService.updateComic(comicId,
                                tenTruyenTranh,
                                gioiThieu,
                                base64Image1,
                                trangThai,
                                selectedGenreId );
                        call.enqueue(new Callback<ApiResponse>() {
                            @Override
                            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                ApiResponse res = response.body();

                                if (res.getStatus() == Status.SUCCESS) {
                                    Toast.makeText(context, res.getMessage(), Toast.LENGTH_SHORT).show();
                                    GetListTruyen(context);
                                    dialog.dismiss();
                                }
                                else {
                                    Toast.makeText(context, res.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ApiResponse> call, Throwable t) {

                            }
                        });

                        if (selectImageUri != null) {
                            InputStream inputStream = context.getContentResolver().openInputStream(selectImageUri);




//                            TruyenTranh truyenTranh = new TruyenTranh(truyenTranhs.get(),
//                                    tenTruyenTranh, truyenTranhs.getNgayDang(),
//                                    trangThai, spinner.getSelectedItem().toString(), gioiThieu, Utils.getBytes(inputStream));
//                            if (sua_truyentranh(truyenTranh, sqLiteDAO)) {
//                                Toast.makeText(context, "Sửa thành công!", Toast.LENGTH_SHORT).show();
//                                GetListTruyen(context);
//                                dialog.dismiss();
//                            }




                        } else {
                            Log.e("DNNFDNF","cc");
//                            TruyenTranh truyenTranh = new TruyenTranh(truyenTranhs.getIdTruyen(),
//                                    tenTruyenTranh, truyenTranhs.getNgayDang(), trangThai,
//                                    spinner.getSelectedItem().toString(), gioiThieu, truyenTranhs.getImg());
//                            if (sua_truyentranh(truyenTranh, sqLiteDAO)) {
//                                Toast.makeText(context, "Sửa thành công!", Toast.LENGTH_SHORT).show();
//                                GetListTruyen(context);
//                                dialog.dismiss();
//                            }

                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
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

    private void xoaTruyen(int ids) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Bạn có muốn xóa truyện này không!")
                .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                        ComicService comicService = ApiService.createService(ComicService.class);
                        Call<ApiResponse> call = comicService.deleteComic(ids);
                        call.enqueue(new Callback<ApiResponse>() {
                            @Override
                            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                ApiResponse res = response.body();
                                if (res.getStatus() == Status.SUCCESS) {
                                    Toast.makeText(context, res.getMessage(), Toast.LENGTH_SHORT).show();
                                    GetListTruyen(context);
                                } else {
                                    Toast.makeText(context, res.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ApiResponse> call, Throwable t) {

                            }
                        });
//                        if (xoa_truyentranh(ids)) {
//                            GetListTruyen(context);
//                            Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
//
//                        } else {
//                            Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
//                        }
                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        builder.create();
        builder.show();
    }

    @Override
    public int getItemCount() {
        if (listTruyen != null) {
            return listTruyen.size();
        }
        return 0;
    }

    public class HomeQlItemTruyenHolder extends RecyclerView.ViewHolder {
        private final TextView textid;
        private final TextView text_ten_truyen;
        private final TextView text_theLoai;
        private final ImageView image_edit;
        private final ImageView image_delete;
        private final ImageView image_truyen;
        private final LinearLayout linearLayout;

        public HomeQlItemTruyenHolder(@NonNull View itemView) {
            super(itemView);
            textid = itemView.findViewById(R.id.textid);
            text_ten_truyen = itemView.findViewById(R.id.text_ten_truyen);
            text_theLoai = itemView.findViewById(R.id.text_theLoai);
            image_edit = itemView.findViewById(R.id.image_edit);
            image_delete = itemView.findViewById(R.id.image_delete);
            image_truyen = itemView.findViewById(R.id.image_truyen);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }
}
