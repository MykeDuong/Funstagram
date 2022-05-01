package com.minhsoumay.funstagram.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.minhsoumay.funstagram.R;

public class HelpFragment extends Fragment {
    public View.OnClickListener containerActivity = null;


    public HelpFragment() {
    }

    public void setContainerActivity(View.OnClickListener containerActivity) {
        this.containerActivity = containerActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_help, container, false);
    }
}