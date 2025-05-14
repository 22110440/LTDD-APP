package com.example.admin_comic.gui.QuanLyTruyen;

import static com.example.admin_comic.gui.QuanLyTruyen.QlChapTruyen.GetListChap;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admin_comic.models.ApiResponse;
import com.example.admin_comic.R;
import com.example.admin_comic.models.Chapter;
import com.example.admin_comic.models.Status;
import com.example.admin_comic.services.ApiService;
import com.example.admin_comic.services.ComicService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeQLItemChap extends RecyclerView.Adapter<HomeQLItemChap.HomeQLItemChapHolder> {
    private List<Chapter> listchap;
    private Context context;

    public HomeQLItemChap(List<Chapter> listchap, Context context) {
        this.listchap = listchap;
        this.context = context;
    }

    @NonNull
    @Override
    public HomeQLItemChapHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ql_tl, parent,
                false);
        return new HomeQLItemChapHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeQLItemChapHolder holder, int position) {
        Chapter chap = listchap.get(position);
        holder.text_name_tl.setText(chap.getTitle());
        holder.image_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Intent intent = new Intent(context, addChap.class);
                intent.putExtra("Key_tenChap", chap.getTitle());
                intent.putExtra("Key_idTruyen", chap.getId());
                context.startActivity(intent);
            }
        });
        holder.image_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xoaChap(chap.getId(), chap.getTitle(),chap.getComicId());
            }
        });
    }


    private void xoaChap(int ids, String tenchap, int comicId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Bạn có muốn xóa chap này không!")
                .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        ComicService comicService = ApiService.createService(ComicService.class);
                        Call<ApiResponse> call = comicService.deleteChapter(ids);
                        call.enqueue(new Callback<ApiResponse>() {
                            @Override
                            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                ApiResponse res = response.body();
                                if (res.getStatus() == Status.SUCCESS) {
                                    Toast.makeText(context, res.getMessage(), Toast.LENGTH_SHORT).show();
                                    GetListChap(context,comicId);
                                } else {
                                    Toast.makeText(context, res.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ApiResponse> call, Throwable t) {
                                // do something
                            }
                        });
//                        if (xoa_chap(ids)) {
//                            sua_img(tenchap, sqLiteDAO);
//                            GetListChap(context,idtt);
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
        if (listchap != null) {
            return listchap.size();
        }
        return 0;
    }

    public class HomeQLItemChapHolder extends RecyclerView.ViewHolder {
        private TextView text_name_tl;
        private ImageView image_edit, image_delete;

        public HomeQLItemChapHolder(@NonNull View itemView) {
            super(itemView);
            text_name_tl = itemView.findViewById(R.id.text_name_tl);
            image_edit = itemView.findViewById(R.id.image_edit);
            image_delete = itemView.findViewById(R.id.image_delete);
        }
    }
}
