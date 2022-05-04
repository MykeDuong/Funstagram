/*
 * This is the HelpFragment which represents the help page of Funstagram.
 * It guides the user about the various features of the app.
 */
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

    /**
     * This method is the on create view method
     * which displays the content.
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_help, container, false);
    }
}