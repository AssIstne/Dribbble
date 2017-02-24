package com.assistne.dribbble.dashboardnavigation;

import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.assistne.dribbble.R;

/**
 * Created by assistne on 17/2/24.
 */

public class ContentFragment extends Fragment {
public static ContentFragment newInstance(@ColorRes int color) {

        Bundle args = new Bundle();
        args.putInt("color", color);
        ContentFragment fragment = new ContentFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dn_content, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        @ColorRes int color = getArguments().getInt("color");
        ((ViewGroup)view).getChildAt(0).setBackgroundColor(getResources().getColor(color));
    }
}
