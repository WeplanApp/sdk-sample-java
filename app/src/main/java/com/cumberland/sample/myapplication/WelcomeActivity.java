package com.cumberland.sample.myapplication;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
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
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.READ_PHONE_STATE
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


  private void checkSdkError(WeplanSdkException ex) {
    if(ex instanceof WeplanSdkException.PermissionNotAvailable)
      ActivityCompat.requestPermissions(this, new String[] { ((WeplanSdkException.PermissionNotAvailable) ex).getWeplanPermission().getValue() }, PERMISSION_REQUEST_CODE);
    else if(ex instanceof WeplanSdkException.BackgroundLimitError)
      requestNotificationPermission();
  }

  private void requestNotificationPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
      Intent intent = new Intent();
      intent.setAction(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
      startActivity(intent);
    }
  }

  private class WeplanSdkListener implements WeplanSdkCallback {

    @Override
    public void onSdkError(final WeplanSdkException ex) {
      WeplanSdk.INSTANCE.disable(getApplicationContext());
      findViewById(R.id.loading).setVisibility(View.GONE);

      Snackbar.make(findViewById(android.R.id.content), "Error: " + ex.getMessage(),
          Snackbar.LENGTH_INDEFINITE).setAction("Ok", new View.OnClickListener() {

        @Override
        public void onClick(View v) {
          checkSdkError(ex);
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
