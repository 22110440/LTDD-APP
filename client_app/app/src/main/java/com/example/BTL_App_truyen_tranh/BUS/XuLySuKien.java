package com.example.BTL_App_truyen_tranh.BUS;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.BTL_App_truyen_tranh.R;

public class XuLySuKien {
    public static Animation ANIMATIONUP;
    public static Animation ANIMATIONDOWN;
    public static void ANIMATION(Context context) {
        ANIMATIONUP = AnimationUtils.loadAnimation(context, R.anim.up);
        ANIMATIONDOWN = AnimationUtils.loadAnimation(context, R.anim.down);
    }
}
