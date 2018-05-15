package com.fan.slideimageview;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class SecondActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        SlideFinishImageView imageView = findViewById(R.id.image);
        ImageView img = new ImageView(this);
        img.setImageResource(R.mipmap.image1);
        imageView.setImageView(img);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        FrameLayout imageView = new FrameLayout(this);
        ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
        ViewGroup decorChild = (ViewGroup) decorView.getChildAt(0);
        decorView.removeView(decorChild);
        imageView.addView(decorChild);
        decorView.addView(imageView);

    }
}
