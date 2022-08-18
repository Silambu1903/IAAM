package com.rax.iaam.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.rax.iaam.Others.ApplicationClass;
import com.rax.iaam.R;
import com.rax.iaam.databinding.FragmentInstallationBinding;
//edited on 30/12/2020 by silambu
public class InstallationFragment extends Fragment {

    FragmentInstallationBinding binding;
    ApplicationClass appClass;
    Context mContext;

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_installation, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appClass = (ApplicationClass) getActivity().getApplication();
        mContext = getContext();
      /*  binding.imgCommissioning.setOnClickListener(v -> appClass.navigateTo(getActivity(), R.id.action_nav_installation_to_newScanQrFragment));
        binding.imgDecommissioning.setOnClickListener(v -> {
            appClass.navigateTo(getActivity(), R.id.action_nav_installation_to_newDeCommissionFragment);
        });*/
        binding.imgCommissioning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    //    Toast.makeText(mContext, "Camera permission is already granted", Toast.LENGTH_SHORT).show();
                    appClass.navigateTo(getActivity(), R.id.action_nav_installation_to_newScanQrFragment);
                } else {
                    // Request Camera Permission
                    ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
                    return;
                }
            }
        });

        binding.imgDecommissioning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    //    Toast.makeText(mContext, "Camera permission is already granted", Toast.LENGTH_SHORT).show();
                    appClass.navigateTo(getActivity(), R.id.action_nav_installation_to_newDeCommissionFragment);
                } else {
                    // Request Camera Permission
                    ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
                    return;
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CAMERA_PERMISSION_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(mContext, "Camera  permission granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "Camera  permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
