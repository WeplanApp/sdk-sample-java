package com.cumberland.sample.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import com.cumberland.weplansdk.WeplanSdk;
import com.cumberland.weplansdk.WeplanSdkCallback;
import com.cumberland.weplansdk.init.WeplanSdkException;

public class WelcomeActivity extends AppCompatActivity {

  private static final int PERMISSION_REQUEST_CODE = 1987;
  private static final String TAG = "WeplanSDK";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_welcome);

    checkPermissions();
    initLoginButton();
  }

  private void checkPermissions() {
    ActivityCompat.requestPermissions(this, new String[] {
        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE
    }, PERMISSION_REQUEST_CODE);
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {

    if (requestCode == PERMISSION_REQUEST_CODE) {
      for (int i = 0; i < permissions.length; i++) {
        checkSinglePermission(permissions[i], grantResults[i]);
      }
    }
  }

  private void checkSinglePermission(String permission, int grantResult) {
    boolean isGranted = grantResult == PackageManager.PERMISSION_GRANTED;

    if (permission.equals(Manifest.permission.ACCESS_FINE_LOCATION) && isGranted) {
      initWeplanSdk();  // Init WeplanSDK immediately after Location permission is granted
    }
  }

  private void initWeplanSdk() {
    Log.i(TAG, "Initializing WeplanSdk");
    WeplanSdk.INSTANCE.withContext(this)
        .withClientId("YOUR_CLIENT_ID")
        .withClientSecret("YOUR_CLIENT_SECRET")
        .listening(
            new WeplanSdkListener()) // Show if WeplanSdk is initialized properly. SIM card is required
        .enable();
  }

  private void initLoginButton() {
    findViewById(R.id.enableButton).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // Simulate user logging in app just opening another activity
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
      }
    });
  }

  private class WeplanSdkListener implements WeplanSdkCallback {

    @Override
    public void onSdkError(WeplanSdkException e) {
      WeplanSdk.INSTANCE.disable(getApplicationContext());
      Log.e(TAG, "Error in WeplanSDK init", e);
    }

    @Override
    public void onSdkInit() {
      Log.i(TAG, "WeplanSDK Init OK");
    }
  }
}
