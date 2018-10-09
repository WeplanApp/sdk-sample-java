package com.cumberland.sample.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.cumberland.weplansdk.WeplanSdk;

public class SplashActivity extends AppCompatActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (WeplanSdk.INSTANCE.isEnabled(this)) {
      startActivity(new Intent(this, MainActivity.class));
    } else {
      startActivity(new Intent(this, WelcomeActivity.class));
    }

    finish();
  }
}
