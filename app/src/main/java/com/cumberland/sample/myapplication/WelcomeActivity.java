package com.cumberland.sample.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.cumberland.weplansdk.WeplanSdk;

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

    if (!hasUserAcceptedTaC()) askTaC();
  }

  private void askTaC() {
    new MaterialDialog.Builder(this).negativeText(getString(R.string.cancel))
        .title("Terms and Conditions")
        .content("Your Terms and Consitions including WeplanSdk")
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
  }
}
