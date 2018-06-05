package com.cumberland.sample.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import com.cumberland.weplansdk.WeplanSdk;
import com.cumberland.weplansdk.WeplanSdkKt;
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

    checkPhonePermission();
    refreshUserId();
  }

  private void checkPhonePermission() {
    WeplanPermissionChecker.INSTANCE.withActivity(this)
        .withPermission(WeplanPermission.READ_PHONE_STATE.INSTANCE)
        .withListener(new PermissionListener())
        .check();
  }

  private void refreshUserId() {
    ((TextView)findViewById(R.id.id_text_view))
        .setText("UserId: " + WeplanSdk.INSTANCE.getUserId(this));
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
      refreshUserId();
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
