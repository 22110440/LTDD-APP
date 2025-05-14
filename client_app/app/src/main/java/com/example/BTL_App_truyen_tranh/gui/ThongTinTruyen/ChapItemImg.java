package com.example.BTL_App_truyen_tranh.gui.ThongTinTruyen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.BTL_App_truyen_tranh.R;
import com.example.BTL_App_truyen_tranh.models.ImageChapter;

import java.util.List;

public class ChapItemImg extends RecyclerView.Adapter<ChapItemImg.ChapItemImgHoldes> {
    private List<ImageChapter> listchap;

    private Context context;

    public ChapItemImg(Context context, List<ImageChapter> listchap) {
        this.context = context;
        this.listchap = listchap;
    }

    @NonNull
    @Override
    public ChapItemImgHoldes onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chapimg, parent, false);
        return new ChapItemImgHoldes(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapItemImgHoldes holder, @SuppressLint("RecyclerView") int position) {
        ImageChapter imgChap = listchap.get(position);
//        holder.image_chap.setImageBitmap(Utils.getImage(imgChap.getImagePath()));

        Glide.with(context)
                .load(imgChap.getImagePath())
                .into(holder.image_chap);
    }


    @Override
    public int getItemCount() {
        if (listchap != null) {
            return listchap.size();
        }
        return 0;
    }

    public class ChapItemImgHoldes extends RecyclerView.ViewHolder {
        private ImageView image_chap;

        public ChapItemImgHoldes(@NonNull View itemView) {
            super(itemView);
            image_chap = itemView.findViewById(R.id.image_chap);
        }
    }
}
