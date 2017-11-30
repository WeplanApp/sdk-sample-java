package com.cumberland.sample.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.cumberland.weplansdk.domain.permissions.model.WeplanPermission;
import com.cumberland.weplansdk.domain.permissions.model.WeplanPermissionAskListener;
import com.cumberland.weplansdk.domain.permissions.usecase.WeplanPermissionChecker;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  @Override
  protected void onResume() {
    super.onResume();

    checkLocationPermission();
  }

  private void checkLocationPermission() {
    WeplanPermissionChecker.INSTANCE.withActivity(this)
        .withPermission(WeplanPermission.ACCESS_FINE_LOCATION.INSTANCE)
        .withListener(new PermissionListener())
        .check();
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
      requestPermission(weplanPermission);
    }

    @Override
    public void onPermissionGranted(WeplanPermission weplanPermission) {
      // TODO
    }

    @Override
    public void onPermissionNotOSCompatible(WeplanPermission weplanPermission) {
      // TODO
    }

    private void requestPermission(WeplanPermission weplanPermission) {
      WeplanPermissionChecker.INSTANCE.requestPermission(MainActivity.this, weplanPermission);
    }
  }
}
