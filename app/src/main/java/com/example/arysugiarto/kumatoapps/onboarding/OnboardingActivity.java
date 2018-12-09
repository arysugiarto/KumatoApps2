package com.example.arysugiarto.kumatoapps.onboarding;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.arysugiarto.kumatoapps.MainActivity;
import com.example.arysugiarto.kumatoapps.R;
import com.hololo.tutorial.library.Step;
import com.hololo.tutorial.library.TutorialActivity;

public class OnboardingActivity extends TutorialActivity {



        @Override
        protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



//slide1
        addFragment(new Step.Builder().setTitle("Welcome To EatNjoy!!")
                .setSummary("It seems like you are a New User!!\nWe welcome you to the official app of EatNjoy.We are happy to have you here!!")
                .setBackgroundColor(Color.parseColor("#FFFFD900")) // int background color
//                .setDrawable(R.drawable.burger) // int top drawable
                .build());

//slide2
            addFragment(new Step.Builder().setTitle("Welcome To EatNjoy!!")
                    .setSummary("It seems like you are a New User!!\nWe welcome you to the official app of EatNjoy.We are happy to have you here!!")
                    .setBackgroundColor(Color.parseColor("#FFFFD900")) // int background color
//                .setDrawable(R.drawable.burger) // int top drawable
                    .build());


        //slide3

            addFragment(new Step.Builder().setTitle("Welcome To EatNjoy!!")
                    .setSummary("It seems like you are a New User!!\nWe welcome you to the official app of EatNjoy.We are happy to have you here!!")
                    .setBackgroundColor(Color.parseColor("#FFFFD900")) // int background color
//                .setDrawable(R.drawable.burger) // int top drawable
                    .build());
    }
    @Override
    public void currentFragmentPosition(int i) {

    }
        @Override
        public void finishTutorial () {


        //without this method onboardind do not work
        SharedPreferences preferences =
                getSharedPreferences("my_preferences", MODE_PRIVATE);

        // Set onboarding_complete to true
        preferences.edit()
                .putBoolean("onboarding_complete",true).apply();

        // Launch the main Activity, called MainActivity
        Intent profile = new Intent(this, MainActivity.class);
        startActivity(profile);

        // Close the OnboardingActivity
        finish();
    }
}