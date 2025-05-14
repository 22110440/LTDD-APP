
package com.example.BTL_App_truyen_tranh.gui.RatingManagement;

import static com.example.BTL_App_truyen_tranh.gui.RatingManagement.RatingManagement.GetRatings;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.BTL_App_truyen_tranh.R;
import com.example.BTL_App_truyen_tranh.models.ApiResponse;
import com.example.BTL_App_truyen_tranh.models.Rating;
import com.example.BTL_App_truyen_tranh.models.Status;
import com.example.BTL_App_truyen_tranh.services.ApiService;
import com.example.BTL_App_truyen_tranh.services.ComicService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatingItemAdapter extends RecyclerView.Adapter<RatingItemAdapter.ViewHolder> {
    private List<Rating> list;
    private Context context;

    public RatingItemAdapter(List<Rating> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rating_admin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Rating rating = list.get(position);
        holder.textComment.setText(rating.getRating() + " sao");
        holder.fullname.setText(rating.getUser().getFullname());
        Glide.with(context).load(rating.getUser().getPhotoURL()).into(holder.avatar);


        holder.icDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteComment(rating.getId());

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

    private void deleteComment(int ratingId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Bạn có muốn xóa đánh giá này không!")
                .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        ComicService comicService = ApiService.createService(ComicService.class);
                        Call<ApiResponse> call = comicService.deleteRating(ratingId);
                        call.enqueue(new Callback<ApiResponse>() {
                            @Override
                            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                ApiResponse res = response.body();
                                if (res.getStatus() == Status.SUCCESS) {
                                    Toast.makeText(context, res.getMessage(), Toast.LENGTH_SHORT).show();
                                    GetRatings(context);
                                } else {
                                    Toast.makeText(context, res.getMessage(), Toast.LENGTH_SHORT).show();
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
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textComment, fullname;
        ImageView avatar, icDelete;


        public ViewHolder(View itemView) {
            super(itemView);
            textComment = itemView.findViewById(R.id.text_name_tl);
            avatar = itemView.findViewById(R.id.user_avatar);
            icDelete = itemView.findViewById(R.id.image_delete);
            fullname = itemView.findViewById(R.id.fullname);


        }
    }
}

