package com.assistne.dribbble.dashboardnavigation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.assistne.dribbble.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DNavigationActivity extends AppCompatActivity {

    @BindView(R.id.chart)
    PieChartView mPieChartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dnavigation);
        ButterKnife.bind(this);

        mPieChartView.setData(new float[]{5, 4.5f, 2, 4, 7});
    }
}
