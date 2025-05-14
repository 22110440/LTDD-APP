package com.example.BTL_App_truyen_tranh.gui.ThongTinTruyen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.BTL_App_truyen_tranh.models.ApiResponse;
import com.example.BTL_App_truyen_tranh.models.Chapter;
import com.example.BTL_App_truyen_tranh.models.Comic;
import com.example.BTL_App_truyen_tranh.R;
import com.example.BTL_App_truyen_tranh.models.Status;
import com.example.BTL_App_truyen_tranh.services.ApiService;
import com.example.BTL_App_truyen_tranh.services.ComicService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThongTinTuyen extends AppCompatActivity {
    private TextView text_ten_truyen, text_time, text_tinhtrang, text_slchap, textTenTheLoai, text_gioithieu;
    private ImageView image_truyen,img_back, icLiked, icShare;
    private Comic comic;
    private RecyclerView list_item;

    Intent intent;

    int comicId;

    List<Chapter> chapters ;

    private LinearLayout tab_1, tab_2;

    private TextView  textView_tab_1, textView_tab_2, viewCounts;

    Boolean hasLiked = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_tuyen);
         intent = getIntent();
        text_ten_truyen = findViewById(R.id.text_ten_truyen);
        text_time = findViewById(R.id.text_time);
        text_tinhtrang = findViewById(R.id.text_tinhtrang);
        text_slchap = findViewById(R.id.text_slchap);
        textTenTheLoai = findViewById(R.id.textTenTheLoai);
        text_gioithieu = findViewById(R.id.text_gioithieu);
        image_truyen = findViewById(R.id.image_truyen);
        list_item = findViewById(R.id.list_item);
        img_back = findViewById(R.id.img_back);
        icLiked = findViewById(R.id.ic_liked);
        viewCounts = findViewById(R.id.view_counts);
        icShare = findViewById(R.id.ic_share);

        textView_tab_1 = findViewById(R.id.textView_tab_1);
        textView_tab_2 = findViewById(R.id.textView_tab_2);

        tab_1 = findViewById(R.id.tab_1);
        tab_2 = findViewById(R.id.tab_2);

        comicId = intent.getIntExtra("Key_idTruyen", 0);

        defaultTab();

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        tab_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                textView_tab_1.setTextColor(getResources().getColor(R.color.purple_200));
                textView_tab_2.setTextColor(getResources().getColor(R.color.black));

                defaultTab();

            }
        });

        tab_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                textView_tab_1.setTextColor(getResources().getColor(R.color.black));
                textView_tab_2.setTextColor(getResources().getColor(R.color.purple_200));

                CommentFragment commentFragment = new CommentFragment();
                replaceFragment(commentFragment, "id", comicId);


            }
        });


//        list_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Chap chap=list.get(i);
//                Intent intent = new Intent(ThongTinTuyen.this, ChapTruyenTranh.class);
//                intent.putExtra("Key_tenChap", chap.getTenChap());
//                intent.putExtra("Key_idTruyen", chap.getIdtt());
//                startActivity(intent);
//            }
//        });
        getComic();
        getListChap();
        icLikedOnClick();
        icShareOnClick();

//        list_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Chapter chap=chapters.get(i);
//                Intent intent = new Intent(ThongTinTuyen.this, ChapTruyenTranh.class);
//                intent.putExtra("Key_tenChap", chap.getTitle());
//                intent.putExtra("Key_idTruyen", chap.getId());
//                startActivity(intent);
//            }
//        });

    }

    private void icShareOnClick () {
        icShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);

                shareIntent.setType("text/plain");

                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "myshare");

                String shareText = "Net truyện, hãy tham gia để đọc truyện tại";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);

                startActivity(Intent.createChooser(shareIntent, "Chọn ứng dụng để chia sẻ"));
            }
        });
    }

    private void defaultTab () {
        RatingFragment ratingFragment = new RatingFragment();
        replaceFragment(ratingFragment, "id", comicId);
    }

    private void liked () {
        hasLiked = true;
        icLiked.setBackgroundResource(R.drawable.baseline_favorite_24);
    }

    private void unLiked () {
        hasLiked = false;
        icLiked.setBackgroundResource(R.drawable.baseline_favorite_border_24);
    }

    private void icLikedOnClick () {
        icLiked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComicService comicService = ApiService.createService(ComicService.class);
                if(hasLiked == true){
                    Call<ApiResponse> call = comicService.removeFavorite(comicId);
                    call.enqueue(new Callback<ApiResponse>() {
                        @Override
                        public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                            if (response.body().getStatus() == Status.SUCCESS) {
                                Toast.makeText(ThongTinTuyen.this, "Xoá yêu thích thành công", Toast.LENGTH_SHORT).show();
                                unLiked();
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse> call, Throwable t) {

                        }
                    });
                }
                else {
                    Call<ApiResponse> call = comicService.addFavorite(comicId);
                    call.enqueue(new Callback<ApiResponse>() {
                        @Override
                        public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                            if (response.isSuccessful()) {
                                if (response.body().getStatus() == Status.SUCCESS) {
                                    Toast.makeText(ThongTinTuyen.this, "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
                                    liked();

                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse> call, Throwable t) {

                        }
                    });
                }
            }
        });
    }
    private void getComic (){

        ComicService comicService = ApiService.createService(ComicService.class);
        Call<ApiResponse<Comic>> call = comicService.getComic(comicId);
        call.enqueue(new Callback<ApiResponse<Comic>>() {
            @Override
            public void onResponse(Call<ApiResponse<Comic>> call, Response<ApiResponse<Comic>> response) {

                Comic comic = response.body().getData();
                if(comic!= null){
                    text_ten_truyen.setText(comic.getTitle());
                    text_time.setText(comic.getCreatedAt());
                    text_gioithieu.setText(comic.getDescription());
                    textTenTheLoai.setText(comic.getGenre().getTitle());
                    viewCounts.setText(comic.getViewCounts() + " lượt xem");
                    Glide.with(ThongTinTuyen.this).load(comic.getPoster()).into(image_truyen);

                    if(comic.getHasFavorite() == true){
                        liked();
                    }
                    else {
                        unLiked();
                    }

                    if(comic.getStatus() =="process"){
                        text_tinhtrang.setText("Đang tiến hành");
                    }
                    else {
                        text_tinhtrang.setText("Kết thúc");
                    }

                }

            }

            @Override
            public void onFailure(Call<ApiResponse<Comic>> call, Throwable t) {

            }
        });

    }

    private void replaceFragment(Fragment fragment, String key, int value) {
        Bundle args = new Bundle();
        args.putInt(key, value);
        fragment.setArguments(args);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment, fragment);
        ft.commit();
    }

    public  void getListChap( ) {

        ComicService comicService = ApiService.createService(ComicService.class);
        Call<ApiResponse<List<Chapter>>> call = comicService.getChaptersByComic(comicId);
        call.enqueue(new Callback<ApiResponse<List<Chapter>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Chapter>>> call, Response<ApiResponse<List<Chapter>>> response) {


                chapters = response.body().getData();;

                if(chapters != null){
                    text_slchap.setText(chapters.size() + " Chap");

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ThongTinTuyen.this, LinearLayoutManager.HORIZONTAL, false);
                    list_item.setLayoutManager(linearLayoutManager);

                    ThongTinTruyenAdapter adapter = new ThongTinTruyenAdapter(chapters, ThongTinTuyen.this);

                    list_item.setAdapter(adapter);


                }




//                HomeQLItemChap homeQLItemChap = new HomeQLItemChap(chapters, context);
//                list_item_ql_ct.setAdapter(homeQLItemChap);

                //        List<Chap> list = getall_chap(truyenTranh.getIdTruyen(), sqLiteDAO1);


//        ArrayAdapter<Chap> arrayAdapter
//                = new ArrayAdapter<Chap>(this, android.R.layout.simple_list_item_1, list);
//
//        list_item.setAdapter(arrayAdapter);
            }
            @Override
            public void onFailure(Call<ApiResponse<List<Chapter>>> call, Throwable t) {
                // do something
            }
        });

    }
}