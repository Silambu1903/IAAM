package com.rax.iaam.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.rax.iaam.Callbacks.VolleyCallback;
import com.rax.iaam.Others.ApplicationClass;
import com.rax.iaam.R;
import com.rax.iaam.databinding.FragementDashboardBinding;

import org.json.JSONException;
import org.json.JSONObject;

import static com.rax.iaam.Others.ApplicationClass.accessToken;
import static com.rax.iaam.Others.ApplicationClass.clientIDNew;
import static com.rax.iaam.Others.ApplicationClass.clientSecret;
import static com.rax.iaam.Others.ApplicationClass.refreshToken;
import static com.rax.iaam.Others.ApplicationClass.signIn;

public class AdminDashboardFragment extends Fragment {

    FragementDashboardBinding binding;
    private ApplicationClass appClass;
    private Context mContext;
    private static final String TAG = "AdminDashboardFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragement_dashboard, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getContext();
        appClass = (ApplicationClass) getActivity().getApplication();
        Bundle b = new Bundle();
        binding.client.setOnClickListener(v -> appClass.navigateTo(getActivity(), R.id.action_nav_dashboard_to_clientAssignmentDetailFragment));
        binding.devices.setOnClickListener(v -> appClass.navigateTo(getActivity(), R.id.action_nav_dashboard_to_device));

        binding.assigned.setOnClickListener(v -> {
            b.putInt("filterType", 2);
            b.putInt("clientId", 0);
            appClass.navigateTo(getActivity(), R.id.action_nav_dashboard_to_device, b);
        });

        binding.unassigned.setOnClickListener(v -> {
            b.putInt("filterType", 1);
            b.putInt("clientId", 0);
            appClass.navigateTo(getActivity(), R.id.action_nav_dashboard_to_device, b);
        });
        //   getDashboardData();
        binding.srlRefreshCounts.setOnRefreshListener(() -> {
            binding.srlRefreshCounts.setRefreshing(false);
            //      getDashboardData();
        });

        binding.btnFWS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appClass.navigateTo(getActivity(), R.id.action_nav_dashboard_to_factoryWiseStatusFragment, b);
            }
        });

        binding.btnSpareConsumption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appClass.navigateTo(getActivity(), R.id.action_nav_dashboard_to_spareConsumptionFragment, b);

            }
        });
        binding.btnMaintenance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appClass.navigateTo(getActivity(), R.id.action_nav_dashboard_to_maintenanceTrackerFragment, b);

            }
        });
        binding.btnEmpDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appClass.navigateTo(getActivity(), R.id.action_nav_dashboard_to_employeeDetailsFragment, b);
            }
        });
        binding.btnMinutes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appClass.navigateTo(getActivity(), R.id.action_nav_dashboard_to_minutesFragment, b);
            }
        });
        binding.btnEnvironmentalParameter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appClass.navigateTo(getActivity(), R.id.action_nav_dashboard_to_environmentalParameterFragment, b);
            }
        });
        binding.btnAssetStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appClass.navigateTo(getActivity(), R.id.action_nav_dashboard_to_assetStatusFragment, b);
            }
        });
        binding.btnNeedleTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appClass.navigateTo(getActivity(), R.id.action_nav_dashboard_to_needleTimeFragment, b);
            }
        });
        binding.btnSessionPunctuality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appClass.navigateTo(getActivity(), R.id.action_nav_dashboard_to_sessionPunctuality, b);
            }
        });

        binding.btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appClass.navigateTo(getActivity(), R.id.action_nav_dashboard_to_filterFragment, b);

            }
        });

        binding.QrScanerBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appClass.navigateTo(getActivity(), R.id.action_nav_dashboard_to_nav_installation, b);
            }
        });


    }
}
