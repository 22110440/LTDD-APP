package com.example.admin_comic.gui.Chart;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.admin_comic.R;

import com.example.admin_comic.gui.QuanLyTruyen.QlChapTruyen;
import com.example.admin_comic.models.Comic;

import java.util.List;

public class ChartItemAdapter extends RecyclerView.Adapter<ChartItemAdapter.ViewHolder> {
    private List<Comic> comics;
    private Context context;

    public  ChartItemAdapter(List<Comic> comics, Context context){
        this.comics = comics;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comic comic = comics.get(position);
        holder.title.setText(comic.getTitle());
        holder.viewCounts.setText(comic.getViewCounts() + " lượt xem");
        Glide.with(context)
                .load(comic.getPoster())
                .into(holder.poster);
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(context, ThongTinTuyen.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("Key_idTruyen", comic.getId());
//                context.startActivity(intent);

                Intent intent = new Intent(context, QlChapTruyen.class);
                intent.putExtra("Key_idTruyen", comic.getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (comics != null) {
            return comics.size();
        }
        return 0;
    }

    public class ViewHolder extends  RecyclerView.ViewHolder {

        private ImageView poster;

        private TextView title, viewCounts;

        private CardView card;

        public  ViewHolder(@NonNull View itemView){
            super(itemView);

            poster= itemView.findViewById(R.id.poster);
            title = itemView.findViewById(R.id.title);
            viewCounts = itemView.findViewById(R.id.view_counts);
            card = itemView.findViewById(R.id.card);


        }

    }
}
