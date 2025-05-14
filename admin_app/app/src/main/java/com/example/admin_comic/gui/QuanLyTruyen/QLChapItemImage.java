package com.example.admin_comic.gui.QuanLyTruyen;

import static com.example.admin_comic.gui.QuanLyTruyen.QuanLyTruyen.chonphoto;
import static com.example.admin_comic.gui.QuanLyTruyen.addChap.GetListImgChap;
import static com.example.admin_comic.gui.QuanLyTruyen.addChap.chonphoto2;
import static com.example.admin_comic.gui.QuanLyTruyen.addChap.hasStoragePermissionImg;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.admin_comic.R;
import com.example.admin_comic.models.ImageChapter;

import java.util.List;

public class QLChapItemImage extends RecyclerView.Adapter<QLChapItemImage.ImageViewHolder> {
    private  List<ImageChapter> imageList;
    private  addChap context;

    public QLChapItemImage(addChap context, List<ImageChapter> imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ImageChapter imageItem = imageList.get(position);

        Glide.with(context)
                .load(imageItem.getImagePath())
                .into(holder.imageView);


        holder.button_sua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hasStoragePermissionImg(context)){
                    chonphoto2=position;
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    context.startActivityForResult(intent, chonphoto2);
                }
                else {
                    ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, chonphoto);
                }
            }
        });

        holder.button_xoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Bạn có muốn xóa hình này không!")
                        .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                imageList.remove(position);
                                GetListImgChap(context);
                                Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });

                builder.create();
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        Button button_xoa, button_sua;

        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            button_xoa = itemView.findViewById(R.id.button_xoa);
            button_sua = itemView.findViewById(R.id.button_sua);
        }
    }
}
