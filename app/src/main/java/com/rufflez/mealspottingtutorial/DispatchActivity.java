package com.rufflez.mealspottingtutorial;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;

/**
 * Created by rufflez on 1/8/16.
 */
public class DispatchActivity extends Activity{
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ParseUser user = ParseUser.getCurrentUser();
        if (user != null){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            ParseLoginBuilder builder = new ParseLoginBuilder(DispatchActivity.this);
            startActivityForResult(builder.build(), 0);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == 0){
            if (ParseUser.getCurrentUser() != null){
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        }
    }
}
