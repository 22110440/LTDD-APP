package com.example.BTL_App_truyen_tranh.gui.Home;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.BTL_App_truyen_tranh.R;
import com.example.BTL_App_truyen_tranh.gui.ThongTinTruyen.ThongTinTuyen;
import com.example.BTL_App_truyen_tranh.models.Comic;

import java.util.List;

public class HomeItemTruyen extends RecyclerView.Adapter<HomeItemTruyen.HomeItemTruyenHolder> {
    private List<Comic> listTruyen;
    private Context context;

    public HomeItemTruyen(List<Comic> listTruyen, Context context) {
        this.listTruyen = listTruyen;
        this.context = context;
    }

    @NonNull
    @Override
    public HomeItemTruyenHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_truyen, parent, false);
        return new HomeItemTruyenHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeItemTruyenHolder holder, int position) {
        Comic truyenTranh = listTruyen.get(position);
//        List<Chap>list=getall_chap(truyenTranh.getId(),sqLiteDAO1);
        holder.textName.setText(truyenTranh.getTitle());
//        holder.textChap.setText(list.get(list.size()-1).getTenChap());
//        holder.imageViewAnh.setImageBitmap(Utils.getImage(truyenTranh.getPoster()));


        Log.d("TEKNE", truyenTranh.getPoster());
        Glide.with(context)
                .load(truyenTranh.getPoster())
                .into(holder.imageViewAnh);
        holder.card_item_truyen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ThongTinTuyen.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("Key_idTruyen", truyenTranh.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (listTruyen != null) {
            return listTruyen.size();
        }
        return 0;
    }

    public class HomeItemTruyenHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewAnh;
        private TextView textName;
        private TextView textChap;
        private CardView card_item_truyen;

        public HomeItemTruyenHolder(@NonNull View itemView) {
            super(itemView);
            imageViewAnh = itemView.findViewById(R.id.imageViewAnh);
            textName = itemView.findViewById(R.id.textName);
            textChap = itemView.findViewById(R.id.textChap);
            card_item_truyen = itemView.findViewById(R.id.card_item_truyen);
        }
    }
}
