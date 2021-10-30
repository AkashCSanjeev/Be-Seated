package com.example.beseated;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.airbnb.lottie.LottieAnimationView;
import com.example.beseated.Auth.SignUp;
import com.example.beseated.databinding.ActivitySplashScreenBinding;

public class SplashScreen extends AppCompatActivity {

    ActivitySplashScreenBinding binding;

    Animation fromBottom,toTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());

        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(binding.getRoot());

        LottieAnimationView lottieAnimationView = findViewById(R.id.animationView);
        lottieAnimationView.setSpeed(0.5f);

        fromBottom= AnimationUtils.loadAnimation(this,R.anim.from_bottom);
        toTop= AnimationUtils.loadAnimation(this,R.anim.to_top);

        binding.teamName.setAnimation(fromBottom);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, SignUp.class);
                startActivity(intent);
                overridePendingTransition(R.anim.from_bottom,R.anim.to_top);

                finish();
            }
        },6000);
    }
}