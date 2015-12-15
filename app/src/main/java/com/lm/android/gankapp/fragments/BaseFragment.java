package com.lm.android.gankapp.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;

import icepick.Icepick;
import icepick.State;

/**
 * Created by liumeng on 2015/12/15.
 */
public class BaseFragment extends Fragment {
    @State
    String username; // TODO This will be automatically saved and restored

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }
}
