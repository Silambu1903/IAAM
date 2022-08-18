package com.rax.iaam.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.rax.iaam.Activity.BaseActivity;
import com.rax.iaam.Adapter.EnvironmentalPageAdapter;
import com.rax.iaam.Callbacks.VolleyCallback;
import com.rax.iaam.Others.ApplicationClass;
import com.rax.iaam.R;
import com.rax.iaam.databinding.FragmentEnvironmentalParameterBinding;

import org.json.JSONObject;

import java.util.ArrayList;

import static com.rax.iaam.Others.ApplicationClass.clientIDNew;
import static com.rax.iaam.Others.ApplicationClass.clientSecret;
import static com.rax.iaam.Others.ApplicationClass.signIn;

public class EnvironmentalParameterFragment extends Fragment {
    private ApplicationClass appClass;
    private Context mContext;
    private FragmentEnvironmentalParameterBinding binding;
    private EnvironmentalPageAdapter pageAdapter;
    String FilterChip;
    ArrayList<String> filterName;
    ChipGroup chipGroup;
    private SharedPreferences preferences;
    private static final String TAG = "EnvironmentalParameterF";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_environmental_parameter, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onViewCreated(view, savedInstanceState);
        appClass = (ApplicationClass) getActivity().getApplication();
        mContext = getContext();
        preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        chipGroup = binding.chip3;
        getFilterValue();
        //InternetCheck


        tabConfig();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.filter_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.filter_menu_bn){
            appClass.navigateTo(getActivity(),R.id.action_environmentalParameterFragment_to_filterFragment);
        }else {
            BaseActivity activity = (BaseActivity)getActivity();
            activity.onSupportNavigateUp();
        }
        return true;
    }

    private void tabConfig() {
        try{
        ViewPager pager = (ViewPager) binding.viewPagerEnvironmentalParameter;
        pageAdapter = new EnvironmentalPageAdapter(getChildFragmentManager(),
                binding.tabLayoutEnvironmentalParameter.getTabCount());
        pager.setAdapter(pageAdapter);
        binding.tabLayoutEnvironmentalParameter.setupWithViewPager(pager, true);}
        catch (Exception e){
            e.printStackTrace();
        }

    }

    private void setTag(ArrayList<String> filterName) {
        if (filterName != null) {
            String selected_filter_items = filterName.get(0).toString();
            String[] selected_filter_item = selected_filter_items.split(",");

            for (String tagName : selected_filter_item) {
                final Chip chip = new Chip(mContext);
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

        filterName = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(value);
            FilterChip = jsonObject.getString("filterChip").replace("[", "").replace("]", "");

            if (!FilterChip.isEmpty()) {
                filterName.add(FilterChip);
            }
            setTag(filterName);
            Log.e(TAG, "getFilterValue: " + jsonObject.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}