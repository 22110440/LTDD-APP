package com.example.BTL_App_truyen_tranh.gui.Home;

import static com.example.BTL_App_truyen_tranh.BUS.XuLySuKien.ANIMATIONDOWN;
import static com.example.BTL_App_truyen_tranh.BUS.XuLySuKien.ANIMATIONUP;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.BTL_App_truyen_tranh.models.Genre;
import com.example.BTL_App_truyen_tranh.gui.TheLoaiTruyen.TheLoaiTruyen;
import com.example.BTL_App_truyen_tranh.R;

import java.util.List;


public class HomeItemTheloai extends RecyclerView.Adapter<HomeItemTheloai.HomeItemTheloaiHolder> {
    private List<Genre> listTheLoai;
    private Context context;

    public HomeItemTheloai(List<Genre> listTheLoai, Context context) {
        this.listTheLoai = listTheLoai;
        this.context = context;
    }

    @NonNull
    @Override
    public HomeItemTheloaiHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_theloai, parent, false);
        return new HomeItemTheloaiHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeItemTheloaiHolder holder, int position) {
        Genre theLoai = listTheLoai.get(position);
        holder.textTenTheLoai.setText(theLoai.getTitle());
        holder.card_item_theloai.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //Kiểm tra xem sự kiện bằng DOWN
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //Bắt hiệu ứng cho text
                    holder.card_item_theloai.startAnimation(ANIMATIONUP);
                } else {
                    //Kiểm tra xem sự kiện bằng UP
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        //Bắt hiệu ứng cho text
                        holder.card_item_theloai.startAnimation(ANIMATIONDOWN);

                        Intent intent = new Intent(context, TheLoaiTruyen.class);
                        intent.putExtra("Key_tentheloai", theLoai.getTitle());
                        intent.putExtra("Key_idTheLoai", theLoai.getId());
                        context.startActivity(intent);
                    }
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        if (listTheLoai != null) {
            return listTheLoai.size();
        }
        return 0;
    }

    public class HomeItemTheloaiHolder extends RecyclerView.ViewHolder {
        private TextView textTenTheLoai;
        private CardView card_item_theloai;

        public HomeItemTheloaiHolder(@NonNull View itemView) {
            super(itemView);
            textTenTheLoai = itemView.findViewById(R.id.textTenTheLoai);
            card_item_theloai = itemView.findViewById(R.id.card_item_theloai);
        }
    }
}
