package com.example.BTL_App_truyen_tranh.gui.Favorites;

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
import com.example.BTL_App_truyen_tranh.models.Favorite;

import java.util.List;

public class FavoriteItemAdapter extends RecyclerView.Adapter<FavoriteItemAdapter.ViewHolder> {
    private List<Favorite> listTruyen;
    private Context context;

    public FavoriteItemAdapter(List<Favorite> listTruyen, Context context) {
        this.listTruyen = listTruyen;
        this.context = context;
    }


    @NonNull
    @Override
    public FavoriteItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_truyen, parent, false);
        return new FavoriteItemAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Favorite truyenTranh = listTruyen.get(position);

        holder.textName.setText(truyenTranh.getComic().getTitle());
        Glide.with(context)
                .load(truyenTranh.getComic().getPoster())
                .into(holder.imageViewAnh);
        holder.card_item_truyen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ThongTinTuyen.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("Key_idTruyen", truyenTranh.getComic().getId());
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewAnh;
        private TextView textName;
        private TextView textChap;
        private CardView card_item_truyen;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewAnh = itemView.findViewById(R.id.imageViewAnh);
            textName = itemView.findViewById(R.id.textName);
            textChap = itemView.findViewById(R.id.textChap);
            card_item_truyen = itemView.findViewById(R.id.card_item_truyen);
        }
    }

}
