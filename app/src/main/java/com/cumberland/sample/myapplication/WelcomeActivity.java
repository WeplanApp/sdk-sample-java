package com.cumberland.sample.myapplication;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.cumberland.weplansdk.WeplanSdk;
import com.cumberland.weplansdk.WeplanSdkCallback;
import com.cumberland.weplansdk.init.WeplanSdkException;

public class WelcomeActivity extends AppCompatActivity {

  private static final int PERMISSION_REQUEST_CODE = 1987;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_welcome);

    checkPermissions();

    findViewById(R.id.enableButton).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        initSdk();
      }
    });
  }

  private void checkPermissions() {
    ActivityCompat.requestPermissions(this, new String[] {
        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CALL_LOG
    }, PERMISSION_REQUEST_CODE);
  }

  private void initSdk() {
    findViewById(R.id.loading).setVisibility(View.VISIBLE);

    WeplanSdk.INSTANCE.withContext(this)
        .withClientId("YOUR_CLIENT_ID")
        .withClientSecret("YOUR_CLIENT_SECRET")
        .listening(new WeplanSdkListener())
        .enable();
  }

  private class WeplanSdkListener implements WeplanSdkCallback {

    @Override
    public void onSdkError(WeplanSdkException e) {
      WeplanSdk.INSTANCE.disable(getApplicationContext());
      findViewById(R.id.loading).setVisibility(View.GONE);

      Snackbar.make(findViewById(android.R.id.content), "Error: " + e.getMessage(),
          Snackbar.LENGTH_INDEFINITE).setAction("Ok", new View.OnClickListener() {

        @Override
        public void onClick(View v) {
          checkPermissions();
        }
      }).show();
    }

    @Override
    public void onSdkInit() {
      startActivity(new Intent(getApplicationContext(), MainActivity.class));
      finish();
    }
  }
}
