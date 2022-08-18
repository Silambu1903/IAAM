package com.rax.iaam.Fragment;

import android.content.Context;
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
import com.rax.iaam.Callbacks.DialogDismissListener;
import com.rax.iaam.Callbacks.QRCallback;
import com.rax.iaam.Callbacks.VolleyCallback;
import com.rax.iaam.Others.ApplicationClass;
import com.rax.iaam.Others.QRCodeReader;
import com.rax.iaam.R;
import com.rax.iaam.databinding.FragmentScanQrBinding;

import org.json.JSONException;
import org.json.JSONObject;

import static com.rax.iaam.Others.ApplicationClass.accessToken;
import static com.rax.iaam.Others.ApplicationClass.accessTokenKey;
import static com.rax.iaam.Others.ApplicationClass.codeType;
import static com.rax.iaam.Others.ApplicationClass.qrcodes;

public class NewScanQrFragment extends Fragment {
    FragmentScanQrBinding binding;
    Context mContext;
    ApplicationClass appClass;
    private String jsonMachine = "", jsonSocket = "",jsonCurrent="",jsonVoltage="";
    private final static int DeviceQR = 0, MachineQR = 1;
    private QRCodeReader qrFragment;
    private int currentQR;
    private int scanIndex = 0;
    private int[] sequence = new int[]{DeviceQR, MachineQR};
    private static final String TAG = "NewScanQrFragmentf";
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1;
    JSONObject machineObj, sensorObj;
    BaseActivity baseActivity;
    int statuscode;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_scan_qr, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getContext();
        appClass = (ApplicationClass) getActivity().getApplication();
        baseActivity= (BaseActivity) getActivity();
        qrFragment = new QRCodeReader(new QRCallback() {
            @Override
            public void OnQRReader(String QRData) {
                try {
                    switch (currentQR) {
                        case DeviceQR:
                            if (QRData.contains("SerialNo") || QRData.contains("MAC")) {
                                sensorObj = new JSONObject(QRData);
                                //checkSensorExistance(sensorObj);
                                Bundle b = new Bundle();
                                try {
                                    b.putString("sensorMac", sensorObj.getString("MAC"));
//                                    b.putString("jsonMachine", jsonMachine);
//                                    b.putString("jsonSocket", jsonSocket);
//                                    b.putString("jsonCurrent",jsonCurrent);
//                                    b.putString("jsonVoltage",jsonVoltage);
                                } catch (JSONException e) {e.printStackTrace();}


                                DialogFragment fragment = new DialogFragment();
                                NewSmartConfigFragment smartConfigFragment = new NewSmartConfigFragment(fragment);
                                smartConfigFragment.setArguments(b);

                                fragment.init(smartConfigFragment, "Commissioning", new DialogDismissListener() {
                                    @Override
                                    public void OnDismiss() {
                                        appClass.goBack(getActivity());
                                    }
                                });
                                fragment.show(getChildFragmentManager(), null);
                            } else {
                                appClass.showSnackBar(mContext, "Invalid Sensor QR");
                            }
                            break;
                        case MachineQR:
                            if (QRData.contains("SAPID")) {
                                machineObj = new JSONObject(QRData);
                                checkMachineExistance(machineObj);
                            } else {
                                appClass.showSnackBar(mContext, "Invalid Machine QR");
                            }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        loadQRFrame();
        configureQRFrame(getSequence());

    }

    private void checkMachineExistance(JSONObject jsonObject) {
        try {
            String machineQrAPi = qrcodes + accessTokenKey + accessToken + "&qrcode=" + jsonObject.getString("SAPID") + codeType + "machine";
            Log.d(TAG, "checkMachineExistance:" + machineQrAPi);
            appClass.httpRequestNewNightHacks(mContext, machineQrAPi, null, Request.Method.GET, new VolleyCallback() {
                @Override
                public void OnSuccess(JSONObject object) {
                    try {
                      if (object.getBoolean("success")){
                          jsonMachine = object.getString("sapid");
                          Log.d(TAG, "OnSuccessmachine:" + jsonMachine);
                          configureQRFrame(getSequence());
                          new MaterialAlertDialogBuilder(mContext).setMessage("Scan success").setPositiveButton("OK", null).show();
                      }else {
                          String message = object.getString("error");
                          new MaterialAlertDialogBuilder(mContext).setMessage(message).setPositiveButton("OK", null).show();
                      }
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
                    Log.d(TAG, "OnFailure1:" + statuscode);
                    error.printStackTrace();               }
            }, 2000);

        } catch (Exception e) {
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
                    Log.d(TAG, "OnSuccess:obj " + object);
                    try {
                        if (object.getBoolean("success")){
                            jsonSocket = object.getString("serial_number");
                            jsonCurrent = object.getString("current");
                            jsonVoltage = object.getString("voltage");

                            Log.d(TAG, "OnSuccess: js" + jsonSocket);
                            configureQRFrame(getSequence());
                            new MaterialAlertDialogBuilder(mContext).setMessage("Scan success").setPositiveButton("OK", null).show();

                        }else {
                            String message = object.getString("error");
                            new MaterialAlertDialogBuilder(mContext).setMessage(message).setPositiveButton("ok",null).show();
                        }
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
                    Log.d(TAG, "OnFailure1:" + statuscode);
                    error.printStackTrace();
                }
            }, 2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getSequence() {
        if (scanIndex < sequence.length) {
            return sequence[scanIndex];
        } else {
            return -1;
        }
    }

    private void configureQRFrame(int QRType) {
        if (QRType == -1) {
            if (jsonMachine.equals("") || jsonSocket.equals("")) {
                appClass.showSnackBar(mContext, "Insufficient data while commissioning \n Please start again");
            } else {
                Bundle b = new Bundle();
                try {
                    b.putString("sensorMac", sensorObj.getString("MAC"));
                    b.putString("jsonMachine", jsonMachine);
                    b.putString("jsonSocket", jsonSocket);
                    b.putString("jsonCurrent",jsonCurrent);
                    b.putString("jsonVoltage",jsonVoltage);
                } catch (JSONException e) {e.printStackTrace();}


                DialogFragment fragment = new DialogFragment();
                NewSmartConfigFragment smartConfigFragment = new NewSmartConfigFragment(fragment);
                smartConfigFragment.setArguments(b);

                fragment.init(smartConfigFragment, "Commissioning", new DialogDismissListener() {
                    @Override
                    public void OnDismiss() {
                        appClass.goBack(getActivity());
                    }
                });
                fragment.show(getChildFragmentManager(), null);

                //   appCLass.navigateTo(getActivity(), R.id.action_scanQRFragment_to_smartConfigFragment, b);
            }
            return;
        }

        if (scanIndex < sequence.length) {
            scanIndex++;
            currentQR = QRType;
            switch (QRType) {
                case DeviceQR:
                    binding.txtQRType.setText("Sensor QR");
                    binding.imgQrType.setImageResource(R.drawable.ic_socketnew);
                    binding.txtSubText.setText("Scan QR code found in Sensor");
                    break;
                case MachineQR:
                    binding.txtQRType.setText("Machine QR");
                    binding.imgQrType.setImageResource(R.drawable.ic_machine);
                    binding.txtSubText.setText("Scan QR code found in machine");
                    break;
            }
        }
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


}
