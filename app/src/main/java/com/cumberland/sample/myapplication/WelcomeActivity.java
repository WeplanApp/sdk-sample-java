package com.cumberland.sample.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.cumberland.weplansdk.WeplanSdk;
import com.cumberland.weplansdk.domain.permissions.model.WeplanPermission;
import com.cumberland.weplansdk.domain.permissions.model.WeplanPermissionAskListener;
import com.cumberland.weplansdk.domain.permissions.usecase.WeplanPermissionChecker;

public class WelcomeActivity extends AppCompatActivity {

  private static final String PREFERENCE = "sample";
  private static final String TAC_PREFERENCE = "TaC";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  @Override
  protected void onResume() {
    super.onResume();

    if (!hasUserAcceptedTaC()) {
      askTaC();
      checkLocationPermission();
    }
    else goToMain();
  }

  private void askTaC() {
    new MaterialDialog.Builder(this).negativeText(getString(R.string.cancel))
        .title("Terms and Conditions")
        .content("Your Terms and Conditions including WeplanSdk")
        .positiveText(getString(R.string.accept))
        .onPositive(new MaterialDialog.SingleButtonCallback() {
          @Override
          public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            acceptTaC();
          }
        })
        .onNegative(new MaterialDialog.SingleButtonCallback() {
          @Override
          public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            finish();
          }
        }).show();
  }

  private boolean hasUserAcceptedTaC() {
    return getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE).getBoolean(TAC_PREFERENCE, false);
  }

  private void acceptTaC() {
    WeplanSdk.INSTANCE.withContext(this)
        .withClientId("YOUR_CLIENT_ID")
        .withClientSecret("YOUR_CLIENT_SECRET")
        .enable();

    getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE).edit().putBoolean(TAC_PREFERENCE, true).apply();

    goToMain();
  }

  private void checkLocationPermission() {
    WeplanPermissionChecker.INSTANCE.withActivity(this)
        .withPermission(WeplanPermission.ACCESS_FINE_LOCATION.INSTANCE)
        .withListener(new PermissionListener())
        .check();
  }

  private void goToMain() {
    startActivity(new Intent(this, MainActivity.class));
    finish();
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
      WeplanPermissionChecker.INSTANCE.requestPermission(WelcomeActivity.this, weplanPermission);
    }
  }
}
