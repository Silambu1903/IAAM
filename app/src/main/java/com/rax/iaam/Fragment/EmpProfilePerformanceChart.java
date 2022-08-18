package com.rax.iaam.Fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.rax.iaam.Callbacks.VolleyCallback;
import com.rax.iaam.Others.ApplicationClass;
import com.rax.iaam.Others.YearDialogbox;
import com.rax.iaam.R;
import com.rax.iaam.databinding.FragmentEmpProfilePerformanceBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.rax.iaam.Others.ApplicationClass.accessToken;
import static com.rax.iaam.Others.ApplicationClass.employee_profile;
import static com.rax.iaam.Others.ApplicationClass.years;

public class EmpProfilePerformanceChart extends Fragment {
    private ApplicationClass appClass;
    private Context mContext;
    private static final String TAG = "EmployeePerformance";
    private FragmentEmpProfilePerformanceBinding binding;
    private String[] oldPerformanceGraphName;
    private ArrayList<Double> oldPerformanceGraphValue;
    ArrayList NoOfFloors;
    String[] lineName;
    int empId;
    private Chip chip1, chip2, chip3, chip4, chip5, chipDate;
    private ChipGroup chipGroup;
    private String[] newPerformanceGraphName;
    private ArrayList<Double> newPerformanceGraphValue;
    private String monthYearStr;
    private DateFormat dateFormat;
TextView textView;
    public EmpProfilePerformanceChart(String[] performanceGraphName, ArrayList<Double> performanceGraphValue, int empId) {
        this.oldPerformanceGraphName = performanceGraphName;
        this.oldPerformanceGraphValue = performanceGraphValue;
        this.empId = empId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_emp_profile_performance, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appClass = (ApplicationClass) getActivity().getApplication();
        mContext = getContext();
        init(view);
    }

    private void init(View view) {
        chipGroup = view.findViewById(R.id.chipGroup_EmpProfile_performance);
        chip1 = view.findViewById(R.id.year1);
        chip2 = view.findViewById(R.id.year2);
        chip3 = view.findViewById(R.id.year3);
        chip4 = view.findViewById(R.id.year4);
        chip5 = view.findViewById(R.id.year5);
        chipDate = view.findViewById(R.id.year);
        textView= view.findViewById(R.id.perfomanceDataEmpty);
        chip1.setChecked(true);

        loadLineChart(oldPerformanceGraphName, oldPerformanceGraphValue);
        dateFormat = new SimpleDateFormat("yyyy");
        Calendar calendar = Calendar.getInstance();
        String today = dateFormat.format(calendar.getTime());
        int myYear = Integer.parseInt(today);
        int[] yearArr = new int[5];
        yearArr[0] = myYear;
        chip1.setText(String.valueOf(myYear));
        for (int i = 1; i <= 4; i++) {
            myYear = myYear - 1;
            yearArr[i] = myYear;
        }
        chip2.setText(String.valueOf(yearArr[1]));
        chip3.setText(String.valueOf(yearArr[2]));
        chip4.setText(String.valueOf(yearArr[3]));
        chip5.setText(String.valueOf(yearArr[4]));


        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.year1:
                        getPerformanceByYear((String) chip1.getText());
                        break;
                    case R.id.year2:
                        getPerformanceByYear((String) chip2.getText());
                        break;
                    case R.id.year3:
                        getPerformanceByYear((String) chip3.getText());
                        break;
                    case R.id.year4:
                        getPerformanceByYear((String) chip4.getText());
                        break;
                    case R.id.year5:
                        getPerformanceByYear((String) chip5.getText());
                        break;
                }
            }
        });

        chipDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yearPicker();
            }
        });

    }

    private void getPerformanceByYear(String newYear) {
        try {
            String perforamnaceByYear = employee_profile + empId + "/performance_details?" + "access_token=" + accessToken + years + newYear;
            appClass.httpRequestNewNightHacks(mContext, perforamnaceByYear, null, Request.Method.GET, new VolleyCallback() {
                @Override
                public void OnSuccess(JSONObject object) {
                    try {
                        newPerformanceGraphValue = new ArrayList<>();
                        JSONArray performanceDetails = object.getJSONArray("performance_details");
                        if (performanceDetails.length() != 0) {
                            newPerformanceGraphName = new String[performanceDetails.length()];
                            for (int i = 0; i < performanceDetails.length(); i++) {
                                String monthyear = performanceDetails.getJSONObject(i).getString("month");
                                double value = performanceDetails.getJSONObject(i).getDouble("value");
                                newPerformanceGraphName[i] = monthyear;
                                newPerformanceGraphValue.add(value);
                            }

                            loadLineChart(newPerformanceGraphName, newPerformanceGraphValue);
                        } else {
                            binding.lineChartEmpProfilePerformance.clear();
                            binding.lineChartEmpProfilePerformance.invalidate();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void OnFailure(VolleyError error) {
                    error.printStackTrace();
                }
            }, 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadLineChart(String[] PerformanceGraphName, ArrayList<Double> PerformanceGraphValue) {
        LineChart lineChart = binding.lineChartEmpProfilePerformance;
        if(PerformanceGraphName!=null){
            binding.lineChartEmpProfilePerformance.setVisibility(View.VISIBLE);
            binding.perfomanceDataEmpty.setVisibility(View.GONE);
            lineName = new String[PerformanceGraphName.length];
            NoOfFloors = new ArrayList();
            for (float i = (float) 0.5f; i <= PerformanceGraphValue.size(); i = i + 1.0f) {
                try {
                    double value = PerformanceGraphValue.get((int) i);
                    float finalvalue = (float) value;
                    NoOfFloors.add(new BarEntry(i, finalvalue));
                    //float convert int
                    int j = (int) i;
                    lineName[j] = PerformanceGraphName[j];

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            createLineChart(lineChart, NoOfFloors, lineName, "Floor");
           }
        else{
            ArrayList NoOfFloors = new ArrayList();
            NoOfFloors.add(new BarEntry(0.5f, 3));
            final String[] floorName = {"Jan"};
            createLineChart(lineChart, NoOfFloors, floorName, "Floor");
            binding.lineChartEmpProfilePerformance.setVisibility(View.GONE);
            binding.perfomanceDataEmpty.setVisibility(View.VISIBLE);
        }

    }

    public void yearPicker() {
        YearDialogbox pickerDialog = new YearDialogbox();
        pickerDialog.setListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int i, int i2) {
                monthYearStr = year + "-" + i + "-" + i2;
                chipDate.setText(formatMonthYear(monthYearStr));
                getPerformanceByYear((String) chipDate.getText());
            }

        }, mContext);
        pickerDialog.show(getFragmentManager(), "YearPicker");

    }

    String formatMonthYear(String str) {
        Date date = null;
        try {
            date = dateFormat.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateFormat.format(date);
    }

    private void createLineChart(LineChart chart, ArrayList xValues, String[] yValues, String legendName) {

        chart.invalidate();
        chart.clear();
        chart.setPinchZoom(false);
        chart.setDrawGridBackground(false);
        chart.getDescription().setText("Years Wise performances");
        chart.setScaleEnabled(false);


        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(yValues));
        xAxis.setCenterAxisLabels(true);
        xAxis.setLabelCount(10);
        xAxis.setEnabled(true);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularityEnabled(true);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setDrawAxisLine(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(8);


        YAxis yAxisRight = chart.getAxisRight();
        yAxisRight.setDrawLabels(false);

        Legend legend = chart.getLegend();
        legend.setEnabled(false);

        LineDataSet bardataset = new LineDataSet(xValues, legendName);


        ValueFormatter vf = new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "" + (float) value;
            }
        };

        bardataset.setValueFormatter(vf);
        bardataset.setValueTextSize(8);
        chart.animateY(1500);
        LineData data = new LineData(bardataset);
        bardataset.setColors(getResources().getColor(R.color.color1));
        chart.setData(data);
        chart.setVisibleXRangeMaximum(7);
        chart.moveViewToX(1);

    }
}
