package com.rax.iaam.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.rax.iaam.Enums.QRMode;
import com.rax.iaam.Others.ApplicationClass;
import com.rax.iaam.R;
import com.rax.iaam.databinding.FragmentQrGeneratorBinding;

public class GenerateQRFragment extends Fragment {
    private static final String TAG = "GenerateQRFragment";

    private Context mContext;
    private ApplicationClass appClass;
    private FragmentQrGeneratorBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_qr_generator, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getContext();
        appClass = (ApplicationClass) getActivity().getApplication();
        Bundle b = new Bundle();

        binding.txtMachine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.putSerializable("viewMode", QRMode.MachineQR);
                appClass.navigateTo(getActivity(), R.id.action_nav_generate_qr_to_QRGenerationFragment, b);
            }
        });

        binding.txtSocket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.putSerializable("viewMode", QRMode.socketQR);
                appClass.navigateTo(getActivity(), R.id.action_nav_generate_qr_to_QRGenerationFragment, b);
            }
        });

        binding.txtDesk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.putSerializable("viewMode", QRMode.deskQR);
                appClass.navigateTo(getActivity(), R.id.action_nav_generate_qr_to_QRGenerationFragment, b);
            }
        });

    }
}
