package com.cumberland.sample.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.cumberland.weplansdk.WeplanSdk;
import com.cumberland.weplansdk.WeplanSdkCallback;
import com.cumberland.weplansdk.domain.permissions.model.WeplanPermission;
import com.cumberland.weplansdk.domain.permissions.model.WeplanPermissionAskListener;
import com.cumberland.weplansdk.domain.permissions.usecase.WeplanPermissionChecker;
import com.cumberland.weplansdk.init.WeplanSdkException;

public class WelcomeActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_welcome);

    checkLocationPermission();

    findViewById(R.id.enableButton).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        initSdk();
      }
    });
  }

  private void checkLocationPermission() {
    WeplanPermissionChecker.INSTANCE.withActivity(this)
        .withPermission(WeplanPermission.ACCESS_COARSE_LOCATION.INSTANCE)
        .withListener(new PermissionListener())
        .check();
  }

  private void initSdk() {
    findViewById(R.id.loading).setVisibility(View.VISIBLE);

    WeplanSdk.INSTANCE.withContext(this)
        .withClientId(
            "fGthbeJ9tru8CDlQUqhKmLXDxwXWzQLxfSjwEeoJ1iWTS9dLNRfIF295LEFwV3dGx53FhY2Oj9m1SDWDoeDhcA")
        .withClientSecret(
            "Youbgyj54SbfMjMcFSCWjAzSDKk8GzJc2VP7DTqWR8LhuqNhb29rOfDOb9GNS0FUhMFtBiKlNqmXVCjMjk3U8u")
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
          checkLocationPermission();
        }
      }).show();
    }

    @Override
    public void onSdkInit() {
      startActivity(new Intent(getApplicationContext(), MainActivity.class));
      finish();    }
  }

  private class PermissionListener implements WeplanPermissionAskListener {

    @Override
    public void onNeedPermission(WeplanPermission weplanPermission) {
      requestPermission(weplanPermission);
    }

    @Override
    public void onPermissionPreviouslyDenied(WeplanPermission weplanPermission) {
      requestPermission(weplanPermission);
    }

    @Override
    public void onPermissionDisabled(WeplanPermission weplanPermission) {

    }

    @Override
    public void onPermissionGranted(WeplanPermission weplanPermission) {

    }

    @Override
    public void onPermissionNotOSCompatible(WeplanPermission weplanPermission) {

    }

    private void requestPermission(WeplanPermission weplanPermission) {
      WeplanPermissionChecker.INSTANCE.requestPermission(WelcomeActivity.this, weplanPermission);
    }
  }
}
