package com.rax.iaam.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.rax.iaam.Others.ApplicationClass;
import com.rax.iaam.R;
import com.rax.iaam.databinding.FragmentSpareConsumptionBinding;

import java.util.ArrayList;

public class SpareConsumptionFragment extends Fragment {
private ApplicationClass appClass;
private Context mContext;
private FragmentSpareConsumptionBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_spare_consumption,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appClass=(ApplicationClass)getActivity().getApplication();
        mContext=getContext();

        BarChart chart=binding.barChartSpareConsumption;
        ArrayList NoOfFloors = new ArrayList();
        NoOfFloors.add(new BarEntry(0.5f, 3));
        NoOfFloors.add(new BarEntry(1.5f, 1));
        NoOfFloors.add(new BarEntry(2.5f, 4));
        NoOfFloors.add(new BarEntry(3.5f, 7));
        NoOfFloors.add(new BarEntry(4.5f, 10));
        NoOfFloors.add(new BarEntry(5.5f, 15));
        NoOfFloors.add(new BarEntry(6.5f, 8));
        NoOfFloors.add(new BarEntry(7.5f, 8));
        NoOfFloors.add(new BarEntry(8.5f, 8));
        NoOfFloors.add(new BarEntry(9.5f, 8));

        final String[] floorName = {"spare1", "needle", "hook", "spare2", "spare3", "spare4", "Spare5", "Spare6", "spare7", "sapare6"};

        createBarChart(chart,NoOfFloors,floorName,"Spare");

        binding.bottomDateSpareConsumption.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.date_1:
                        createBarChart(chart,NoOfFloors,floorName,"Spare");
                        Toast.makeText(appClass, "date 1 selected", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.date_2:
                        createBarChart(chart,NoOfFloors,floorName,"Spare");
                        Toast.makeText(appClass, "date 2 selected", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.date_3:
                        createBarChart(chart,NoOfFloors,floorName,"Spare");
                        Toast.makeText(appClass, "date 3 selected", Toast.LENGTH_SHORT).show();
                        return true;
                }

                return false;
            }
        });
    }

    private void createBarChart(BarChart chart, ArrayList xValues, String[] yValues, String legendName){
        chart.setPinchZoom(false);
        chart.setDrawGridBackground(false);
        chart.getDescription().setEnabled(false);
        chart.setScaleEnabled(false);



        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(yValues));
        xAxis.setCenterAxisLabels(true);
        xAxis.setLabelCount(10);
        xAxis.setEnabled(true);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularityEnabled(true);
//        xAxis.setAxisMinimum(0f);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setDrawAxisLine(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setTextSize(10);


        YAxis yAxisRight = chart.getAxisRight();
        yAxisRight.setDrawLabels(false);

        Legend legend = chart.getLegend();
        legend.setEnabled(false);

        BarDataSet bardataset = new BarDataSet(xValues, legendName);

        //bar value label to int
        ValueFormatter vf = new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return ""+(int)value;
            }
        };

        bardataset.setValueFormatter(vf);
        bardataset.setValueTextSize(8);
        chart.animateY(1500);
        BarData data = new BarData(bardataset);
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
//        bardataset.setColors(getResources().getColor(R.color.color1));
        chart.setData(data);
        chart.setVisibleXRangeMaximum(7);
        chart.moveViewToX(1);

    }
}
