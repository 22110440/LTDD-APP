package com.example.BTL_App_truyen_tranh.gui.ThongTinTruyen;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.BTL_App_truyen_tranh.models.ApiResponse;
import com.example.BTL_App_truyen_tranh.models.Chapter;
import com.example.BTL_App_truyen_tranh.R;
import com.example.BTL_App_truyen_tranh.models.Status;
import com.example.BTL_App_truyen_tranh.services.ApiService;
import com.example.BTL_App_truyen_tranh.services.ComicService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThongTinTruyenAdapter extends RecyclerView.Adapter<ThongTinTruyenAdapter.ViewHolder> {
    private List<Chapter> list;
    private Context context;

    public ThongTinTruyenAdapter(List<Chapter> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder( @NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_chapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Chapter chapter = list.get(position);
        holder.name.setText(chapter.getTitle());
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ComicService comicService = ApiService.createService(ComicService.class);
                Call<ApiResponse> call = comicService.addViewCount(chapter.getComicId());
                call.enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                       if(response.body().getStatus() == Status.SUCCESS){
                           Intent intent = new Intent(context, ChapTruyenTranh.class);
                           intent.putExtra("Key_tenChap", chapter.getTitle());
                           intent.putExtra("Key_idTruyen", chapter.getId());
                           context.startActivity(intent);
                       }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {

                    }
                });


            }
        });





    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name ;



        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textView);



        }
    }
}

