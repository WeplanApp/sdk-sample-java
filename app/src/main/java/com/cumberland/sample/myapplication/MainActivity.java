package com.cumberland.sample.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

    //checkAppUsagePermission();
    showUserId();
    initDisableButton();
  }

  private void checkAppUsagePermission() {
    WeplanPermissionChecker.INSTANCE.withActivity(this)
        .withPermission(WeplanPermission.APPS_USAGE.INSTANCE)
        .withListener(new PermissionListener())
        .check();
  }

  private void showUserId() {
    ((TextView)findViewById(R.id.id_text_view)).setText("UserId: " + WeplanSdk.INSTANCE.getUserId(this));
  }

  private void initDisableButton() {
    findViewById(R.id.disableButton).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        WeplanSdk.INSTANCE.disable(getApplicationContext());
        startActivity(new Intent(getApplicationContext(), WelcomeActivity.class));
        finish();
      }
    });
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
      WeplanPermissionChecker.INSTANCE.requestPermission(MainActivity.this, weplanPermission);
    }
  }
}
