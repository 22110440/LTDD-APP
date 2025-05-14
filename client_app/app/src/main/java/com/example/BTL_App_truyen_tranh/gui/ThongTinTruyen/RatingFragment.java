package com.example.BTL_App_truyen_tranh.gui.ThongTinTruyen;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.BTL_App_truyen_tranh.R;
import com.example.BTL_App_truyen_tranh.models.ApiResponse;
import com.example.BTL_App_truyen_tranh.models.Rating;
import com.example.BTL_App_truyen_tranh.models.Status;
import com.example.BTL_App_truyen_tranh.services.ApiService;
import com.example.BTL_App_truyen_tranh.services.ComicService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RatingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RatingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ImageView star_1, star_2, star_3, star_4, star_5;
    private Button btn_send_rate;
    private int comicId;
    private int STAR = 0, TOTAL_RATING = 0;

    private int STAR_COUNT = 0;

    public RatingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RatingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RatingFragment newInstance(String param1, String param2) {
        RatingFragment fragment = new RatingFragment();
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
//        return inflater.inflate(R.layout.fragment_rating, container, false);

        View view = inflater.inflate(R.layout.fragment_rating, container, false);
        star_1 = view.findViewById(R.id.star_1);
        star_2 = view.findViewById(R.id.star_2);
        star_3 = view.findViewById(R.id.star_3);
        star_4 = view.findViewById(R.id.star_4);
        star_5 = view.findViewById(R.id.star_5);
        btn_send_rate = view.findViewById(R.id.btn_send_rate);
        comicId = getArguments().getInt("id", 0);
        getStars();
        onClickStars();

        btn_send_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (STAR_COUNT == 0) {
                    Toast.makeText(getActivity(), "Vui lòng đánh giá trước khi gửi!", Toast.LENGTH_SHORT).show();
                } else {
                    ComicService comicService = ApiService.createService(ComicService.class);
                    Call<ApiResponse> call = comicService.ratingComic(comicId, STAR_COUNT);

                    call.enqueue(new Callback<ApiResponse>() {
                        @Override
                        public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                            if (response.body().getStatus() == Status.SUCCESS) {
                                Toast.makeText(getActivity(), "Đánh giá thành công!", Toast.LENGTH_SHORT).show();
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
            }
        });


        return  view;
    }

    private void getStars () {
        setStar0();


        ComicService comicService = ApiService.createService(ComicService.class);

        Call<ApiResponse<Rating>> call = comicService.getUserRatingByComic(comicId);

        call.enqueue(new Callback<ApiResponse<Rating>>() {
            @Override
            public void onResponse(Call<ApiResponse<Rating>> call, Response<ApiResponse<Rating>> response) {
                if(response.body().getStatus() == Status.SUCCESS){
                    Rating rate = response.body().getData();

                    if (rate != null) {
                        STAR_COUNT = rate.getRating();
                        switch (STAR_COUNT) {
                            case 1:
                                setStar1();
                                break;
                            case 2:
                                setStar2();
                                break;
                            case 3:
                                setStar3();
                                break;
                            case 4:
                                setStar4();
                                break;
                            case 5:
                                setStar5();
                                break;
                            default:
                                setStar0();
                                break;
                        }
                    }
                }
                else {
                    // do something
                    setStar0();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Rating>> call, Throwable t) {
                setStar0();
            }
        });

    }

    private void onClickStars() {
        star_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                STAR_COUNT = 1;
                setStar1();
            }
        });
        star_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                STAR_COUNT = 2;
                setStar2();
            }
        });
        star_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                STAR_COUNT = 3;
                setStar3();
            }
        });
        star_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                STAR_COUNT = 4;
                setStar4();
            }
        });
        star_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                STAR_COUNT = 5;
                setStar5();
            }
        });
    }

    private void setStar5() {
        star_1.setBackgroundResource(R.drawable.ic_round_star_24);
        star_2.setBackgroundResource(R.drawable.ic_round_star_24);
        star_3.setBackgroundResource(R.drawable.ic_round_star_24);
        star_4.setBackgroundResource(R.drawable.ic_round_star_24);
        star_5.setBackgroundResource(R.drawable.ic_round_star_24);
    }

    private void setStar4() {
        star_1.setBackgroundResource(R.drawable.ic_round_star_24);
        star_2.setBackgroundResource(R.drawable.ic_round_star_24);
        star_3.setBackgroundResource(R.drawable.ic_round_star_24);
        star_4.setBackgroundResource(R.drawable.ic_round_star_24);
        star_5.setBackgroundResource(R.drawable.ic_round_star_border_24);
    }

    private void setStar3() {
        star_1.setBackgroundResource(R.drawable.ic_round_star_24);
        star_2.setBackgroundResource(R.drawable.ic_round_star_24);
        star_3.setBackgroundResource(R.drawable.ic_round_star_24);
        star_4.setBackgroundResource(R.drawable.ic_round_star_border_24);
        star_5.setBackgroundResource(R.drawable.ic_round_star_border_24);
    }

    private void setStar2() {
        star_1.setBackgroundResource(R.drawable.ic_round_star_24);
        star_2.setBackgroundResource(R.drawable.ic_round_star_24);
        star_3.setBackgroundResource(R.drawable.ic_round_star_border_24);
        star_4.setBackgroundResource(R.drawable.ic_round_star_border_24);
        star_5.setBackgroundResource(R.drawable.ic_round_star_border_24);
    }

    private void setStar1() {
        star_1.setBackgroundResource(R.drawable.ic_round_star_24);
        star_2.setBackgroundResource(R.drawable.ic_round_star_border_24);
        star_3.setBackgroundResource(R.drawable.ic_round_star_border_24);
        star_4.setBackgroundResource(R.drawable.ic_round_star_border_24);
        star_5.setBackgroundResource(R.drawable.ic_round_star_border_24);
    }

    private void setStar0() {
        star_1.setBackgroundResource(R.drawable.ic_round_star_border_24);
        star_2.setBackgroundResource(R.drawable.ic_round_star_border_24);
        star_3.setBackgroundResource(R.drawable.ic_round_star_border_24);
        star_4.setBackgroundResource(R.drawable.ic_round_star_border_24);
        star_5.setBackgroundResource(R.drawable.ic_round_star_border_24);
    }
}