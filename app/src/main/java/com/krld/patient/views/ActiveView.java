package com.krld.patient.views;

import android.view.View;

public interface ActiveView {
    void onPause();

    void onResume();

    View getView();

    void onShow();
}
