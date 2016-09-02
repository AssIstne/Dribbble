package com.assistne.dribbble.bezierindicator;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.assistne.dribbble.R;

/**
 * Created by assistne on 16/9/2.
 */
public class BezierFragment extends Fragment {

    public static BezierFragment newInstance(@DrawableRes int background) {
        BezierFragment fragment = new BezierFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("background", background);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_bezier, container, false);
        root.findViewById(R.id.background).setBackgroundResource(getArguments().getInt("background"));
        return root;
    }
}
