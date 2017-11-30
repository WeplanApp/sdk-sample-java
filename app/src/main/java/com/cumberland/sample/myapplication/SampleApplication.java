package com.cumberland.sample.myapplication;

import android.app.Application;
import com.cumberland.weplansdk.WeplanSdk;

public class SampleApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    WeplanSdk.INSTANCE.init(this);
  }
}
