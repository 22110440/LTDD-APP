package com.example.BTL_App_truyen_tranh.gui.ThongTinTruyen;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.BTL_App_truyen_tranh.R;
import com.example.BTL_App_truyen_tranh.models.ApiResponse;
import com.example.BTL_App_truyen_tranh.models.Comment;
import com.example.BTL_App_truyen_tranh.models.Status;
import com.example.BTL_App_truyen_tranh.services.ApiService;
import com.example.BTL_App_truyen_tranh.services.ComicService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CommentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommentFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private EditText editText_comment;
    private LinearLayout layout_set_comments;
    private TextView textView_comments_count;
    private Button btn_send_comment;
    private int comicId, userID;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CommentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CommentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CommentFragment newInstance(String param1, String param2) {
        CommentFragment fragment = new CommentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_comment, container, false);
//        return inflater.inflate(R.layout.fragment_comment, container, false);

        comicId = getArguments().getInt("id", 0);

        editText_comment = view.findViewById(R.id.editText_comment);
        layout_set_comments = view.findViewById(R.id.layout_set_comments);
        btn_send_comment = view.findViewById(R.id.btn_send_comment);
        textView_comments_count = view.findViewById(R.id.textView_comments_count);

        getComments();
        btn_send_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = editText_comment.getText().toString().trim();

                if(!comment.isEmpty()){
                addComment(comment);
                }
                reload();
            }
        });

        return view;

    }

    private void addComment(String comment) {
        ComicService commentService = ApiService.createService(ComicService.class);
        Call<ApiResponse> call = commentService.createComment(Integer.valueOf(comicId),comment);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if(response.body().getStatus() == Status.SUCCESS){
                    Toast.makeText(getActivity(), "Đã bình luận", Toast.LENGTH_SHORT).show();
                    getComments();
                }
                else {
                    // do something
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });
    }

    private void getComments() {

        ComicService comicService = ApiService.createService(ComicService.class);
        Call<ApiResponse<List<Comment>>> call = comicService.getCommentsByComic(comicId);

        call.enqueue(new Callback<ApiResponse<List<Comment>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Comment>>> call, Response<ApiResponse<List<Comment>>> response) {
                List<Comment> comments = response.body().getData();
                if (comments != null && !comments.isEmpty()) {
                    collectComments(comments);
                    textView_comments_count.setText(comments.size() + " bình luận");
                } else {
                    textView_comments_count.setText("0 bình luận");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Comment>>> call, Throwable t) {

            }
        });

    }

    private void collectComments(List<Comment> comments) {

        for(Comment comment: comments){
            String name = comment.getUser().getFullname();
            String time_= comment.getCreatedAt();
            String comment_ = comment.getText();
            String avatar =comment.getUser().getPhotoURL();
            setLayoutComment(name, time_, comment_, avatar);

        }

    }

    private void setLayoutComment(String userid, String time, String comment, String avatar_link){
        LinearLayout parent = new LinearLayout(getActivity());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins( 0, 0, 0, 0);
        parent.setGravity(Gravity.CENTER);
        parent.setLayoutParams(params);
        parent.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout parent3 = new LinearLayout(getActivity());
        LinearLayout.LayoutParams params6 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params6.setMargins(dpToPx(10), 0, dpToPx(10), dpToPx(20));
        parent3.setLayoutParams(params6);
        parent3.setOrientation(LinearLayout.VERTICAL);


        LinearLayout parent2 = new LinearLayout(getActivity());
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params2.setMargins( dpToPx(10), 0,0, 0);
        parent2.setLayoutParams(params2);
        parent2.setOrientation(LinearLayout.VERTICAL);

        LinearLayout layout2 = new LinearLayout(getActivity());
        layout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout2.setOrientation(LinearLayout.HORIZONTAL);


        TextView USER_NAME = new TextView(getActivity());
        TextView COMMENT_TIME = new TextView(getActivity());
        TextView COMMENT = new TextView(getActivity());
        View view = new View(getActivity());

        USER_NAME.setText(userid);
        USER_NAME.setTextSize(16);

        USER_NAME.setLayoutParams(new TableLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

        COMMENT_TIME.setText(time);

        COMMENT.setText(comment);
        COMMENT.setTextColor(getResources().getColor(R.color.black));
        COMMENT.setTextSize(18);

        view.setLayoutParams(new TableLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,3));
        view.setBackgroundResource(R.color.purple_200);

        CardView.LayoutParams params3 = new CardView.LayoutParams(dpToPx(50), dpToPx(50), Gravity.CENTER);
        CardView cardView = new CardView(getActivity());
        cardView.setLayoutParams(params3);
        cardView.setRadius(dpToPx(15));

        ImageView imageView = new ImageView(getActivity());
        Glide.with(getActivity()).load(avatar_link).into(imageView);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        cardView.addView(imageView);

        layout2.addView(USER_NAME);
        layout2.addView(COMMENT_TIME);

        parent.addView(cardView);
        parent.addView(parent2);


        parent2.addView(layout2);
        parent2.addView(COMMENT);

        parent3.addView(parent);
        parent3.addView(view);

        layout_set_comments.addView(parent3);

    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    private void reload(){

        editText_comment.setText("");
        layout_set_comments.removeAllViewsInLayout();

    }


}