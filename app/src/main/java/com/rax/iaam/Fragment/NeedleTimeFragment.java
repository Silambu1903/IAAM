package com.rax.iaam.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.rax.iaam.Activity.BaseActivity;
import com.rax.iaam.Adapter.NeedleTimePageAdapter;
import com.rax.iaam.Others.ApplicationClass;
import com.rax.iaam.R;
import com.rax.iaam.databinding.FragmentNeedleTimeBinding;

import org.json.JSONObject;

import java.util.ArrayList;


public class NeedleTimeFragment extends Fragment {
    private ApplicationClass appClass;
    private Context context;
    private FragmentNeedleTimeBinding binding;
    private NeedleTimePageAdapter pageAdapter;
     private SharedPreferences preferences;
    ChipGroup chipGroup;
    String FilterChip;
    ArrayList<String> filterName;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_needle_time, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appClass = (ApplicationClass) getActivity().getApplication();
        context = getContext();
        setHasOptionsMenu(true);
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        chipGroup = binding.chipGroup;
        getFilterValue();
        tabConfig();
    }
    private void setTag(ArrayList<String> filterName) {
        if (filterName != null) {
            String selected_filter_items = filterName.get(0).toString();
            String[] selected_filter_item = selected_filter_items.split(",");

            for (String tagName : selected_filter_item) {
                final Chip chip = new Chip(context);
                int paddingDp = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 10,
                        getResources().getDisplayMetrics()
                );
                chip.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);
                chip.setText(tagName);
                chipGroup.addView(chip);
            }


        }

    }
    private void getFilterValue() {
        String value = preferences.getString("filterObject", "");
        JSONObject jsonObject;
        filterName = new ArrayList<>();
        try {
            jsonObject = new JSONObject(value);
            FilterChip = jsonObject.getString("filterChip").replace("[", "").replace("]", "");
            if (!FilterChip.isEmpty()) {
                filterName.add(FilterChip);
            }
            setTag(filterName);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.filter_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.filter_menu_bn) {
           // b.putString("b", "NeedleTimeFragment");
            appClass.navigateTo(getActivity(), R.id.action_needleTimeFragment_to_filterFragment);
        } else {
            BaseActivity activity = (BaseActivity) getActivity();
            activity.onSupportNavigateUp();
        }
        return true;
    }

    private void tabConfig() {
        try {
            ViewPager pager = (ViewPager) binding.viewPagerNeedleTime;
            pageAdapter = new NeedleTimePageAdapter(getChildFragmentManager(), binding.tabLayoutNeedleTime.getTabCount());
            pager.setAdapter(pageAdapter);
            binding.tabLayoutNeedleTime.setupWithViewPager(pager, true);
        }catch (Exception e)
        {
        e.printStackTrace();
        }
    }

}
