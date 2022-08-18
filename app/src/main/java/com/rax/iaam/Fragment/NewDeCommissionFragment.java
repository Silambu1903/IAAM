package com.rax.iaam.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.rax.iaam.Activity.BaseActivity;
import com.rax.iaam.Callbacks.QRCallback;
import com.rax.iaam.Callbacks.VolleyCallback;
import com.rax.iaam.Others.ApplicationClass;
import com.rax.iaam.Others.QRCodeReader;
import com.rax.iaam.R;
import com.rax.iaam.databinding.FragmentDeCommisionBinding;

import org.json.JSONException;
import org.json.JSONObject;

import static com.rax.iaam.Others.ApplicationClass.accessToken;
import static com.rax.iaam.Others.ApplicationClass.accessTokenKey;
import static com.rax.iaam.Others.ApplicationClass.codeType;
import static com.rax.iaam.Others.ApplicationClass.decomission;
import static com.rax.iaam.Others.ApplicationClass.qrcodes;

public class NewDeCommissionFragment extends Fragment implements QRCallback {
    FragmentDeCommisionBinding binding;
    ApplicationClass appClass;
    Context mContext;
    private QRCodeReader qrFragment;
    BaseActivity baseActivity;
    int statuscode;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_de_commision, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getContext();
        appClass = (ApplicationClass) getActivity().getApplication();
        baseActivity= (BaseActivity) getActivity();
        qrFragment = new QRCodeReader(this);
        loadQRFrame();
    }

    private void loadQRFrame() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new Runnable() {
                    @Override
                    public void run() {
                        final FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                        ft.replace(binding.frmQRContainer.getId(), qrFragment, "");
                        ft.commit();
                    }
                }.run();
            }
        });
    }

    @Override
    public void OnQRReader(String QRData) {
        try {
            JSONObject object = new JSONObject(QRData);
            if (QRData.contains("SerialNo")) {
                checkSensorExistance(object);
            } else {
                appClass.showSnackBar(mContext, "Invalid QR Code");
            }
        } catch (JSONException e) {
            appClass.showSnackBar(mContext, "Invalid QR Code");
            e.printStackTrace();
        }

    }

    private void checkSensorExistance(JSONObject jsonObject) {
        //  https://api.esgrobot.com/api/v1/qrcodes/scan?access_token=QUVQam8623nucc_PilZ5S6PkXyI5V2K1U_tszEKVnRA&qrcode=1&code_type=sensor
        try {
            String sensorQrAPi = qrcodes + accessTokenKey + accessToken + "&qrcode=" + jsonObject.getString("SerialNo") + codeType + "sensor";
            appClass.httpRequestNewNightHacks(mContext, sensorQrAPi, null, Request.Method.GET, new VolleyCallback() {
                @Override
                public void OnSuccess(JSONObject object) {
                    Log.d("TAG", "OnSuccess:ob " + object);
                    try {
                        //if (appClass.checkStatus(object)) {
                        new MaterialAlertDialogBuilder(mContext).setTitle("Confirmation !").
                                setMessage("Do you want to decommission Smart device with MAC: " + object.getString("serial_number")).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    deCommission(object.getString("id"));

                                } catch (JSONException e) {
                                    appClass.showLocalError(mContext);
                                    e.printStackTrace();
                                }
                            }
                        }).setNegativeButton("NO", null).show();
//                        } else {
//                            new MaterialAlertDialogBuilder(mContext).setTitle("Failure").setMessage("Smart device not commissioned yet").setPositiveButton("OK", null).show();
//                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void OnFailure(VolleyError error) {
                    if (error.networkResponse != null) {
                        statuscode = error.networkResponse.statusCode;
                    }
                    if (statuscode == 401) {
                        baseActivity.TokenInvalid();

                    }
                    Log.d("TAG", "OnFailure1:" + statuscode);
                    error.printStackTrace();
                }
            }, 2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deCommission(String serial_number) {
        //https://api.esgrobot.com/api/v1/sensors/2401/decomission?access_token=sK7Vny-aehsF1FTSW5nQ7fSOVO-O_R5-WFGwlX5pW30
        String decommissionApi = "sensors/" + serial_number + decomission + accessTokenKey + accessToken;
        Log.d("TAG", "deCommission: sa" + serial_number);
        try {
            appClass.httpRequestNewNightHacks(mContext, decommissionApi, null, Request.Method.POST, new VolleyCallback() {
                @Override
                public void OnSuccess(JSONObject object) {
                    Log.d("TAG", "OnSuccess:"+object);
                    try {
                        if (object.getBoolean("success")) {
                            appClass.showSnackBar(mContext, object.getString("message"));
                        } else {
                            appClass.showSnackBar(mContext, object.getString("error"));
                        }
                        appClass.navigateTo(getActivity(), R.id.action_newDeCommissionFragment_to_nav_installation);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void OnFailure(VolleyError error) {

                    error.printStackTrace();

                    try{
                        if (error.networkResponse != null) {
                            statuscode = error.networkResponse.statusCode;
                        }
                        if (statuscode == 401) {
                            baseActivity.TokenInvalid();

                        }
                        Log.d("TAG", "OnFailure1:" + statuscode);
                    }catch (Exception e){

                    }
                }
            }, 2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
