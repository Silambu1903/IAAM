package com.rax.iaam.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.location.LocationManagerCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.espressif.iot.esptouch.EsptouchTask;
import com.espressif.iot.esptouch.IEsptouchResult;
import com.espressif.iot.esptouch.IEsptouchTask;
import com.espressif.iot.esptouch.util.ByteUtil;
import com.espressif.iot.esptouch.util.TouchNetUtil;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.rax.iaam.Activity.BaseActivity;
import com.rax.iaam.Callbacks.VolleyCallback;
import com.rax.iaam.Others.ApplicationClass;
import com.rax.iaam.Others.S_Communication;
import com.rax.iaam.R;
import com.rax.iaam.databinding.FragmentSmartConfigBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.Context.WIFI_SERVICE;
import static com.rax.iaam.Others.ApplicationClass.accessToken;
import static com.rax.iaam.Others.ApplicationClass.accessTokenKey;
import static com.rax.iaam.Others.ApplicationClass.baseURLNew;
import static com.rax.iaam.Others.ApplicationClass.clientID;
import static com.rax.iaam.Others.ApplicationClass.configURL;
import static com.rax.iaam.Others.ApplicationClass.destinationIdKey;
import static com.rax.iaam.Others.ApplicationClass.destinationTypeKey;
import static com.rax.iaam.Others.ApplicationClass.mIPAddress;
import static com.rax.iaam.Others.ApplicationClass.mPortNumber;
import static com.rax.iaam.Others.ApplicationClass.mapping;

import static com.rax.iaam.Others.ApplicationClass.smart_baseURLNew;
import static com.rax.iaam.Others.ApplicationClass.sourceIdKey;
import static com.rax.iaam.Others.ApplicationClass.sourceTypeKey;
import static com.rax.iaam.Others.ApplicationClass.userID;
import static com.rax.iaam.Others.S_Communication.ACTION_MyIntentService;

public class NewSmartConfigFragment extends Fragment {
    private static final int REQUEST_PERMISSION = 0x01;
    private static final String TAG = "SmartConfigFragment";
    private static JSONObject machine, desk, socket;
    private static String machineSerial, sensorSerial, sensorMac, current;
    private final int RETRY_COUNT = 2, RETRY_DELAY = 2000;
    public ApplicationClass appClass;
    private FragmentSmartConfigBinding binding;
    private static DialogFragment parent;
    Bundle lineBundle;
    private Location currentLocation;
    private boolean mReceiverRegistered = false;
    private Context mContext;
    int CurrentFinalValue;
    String FinalValue;
    BaseActivity baseActivity;
    int statuscode,value;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private CountDownTimer packetSender = new CountDownTimer(RETRY_DELAY * (RETRY_COUNT + 1), RETRY_DELAY) {
        @Override
        public void onTick(long millisUntilFinished) {
            showProgress();
            sendPacket();
        }

        @Override
        public void onFinish() {
            appClass.showSnackBar(mContext, "Timed out");
        }
    };
    private EspSmartConfig mTask;
    private boolean mDestroyed = false;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null) {
                return;
            }

            WifiManager wifiManager = (WifiManager) context.getApplicationContext()
                    .getSystemService(WIFI_SERVICE);
            assert wifiManager != null;

            switch (action) {
                case WifiManager.NETWORK_STATE_CHANGED_ACTION:
                case LocationManager.PROVIDERS_CHANGED_ACTION:
                    onWifiChanged(wifiManager.getConnectionInfo());
                    break;
            }
        }
    };

    NewSmartConfigFragment(DialogFragment parent) {
        this.parent = parent;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_smart_config, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        appClass = (ApplicationClass) getActivity().getApplication();
        baseActivity = (BaseActivity) getActivity();
        mContext = getContext();
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        if (preferences.getString("wifiPassword", "") != null) {
            binding.edtPass.setText(preferences.getString("wifiPassword", ""));
        }
        binding.edtConfigUrl.setText(smart_baseURLNew + "\n" + "sensor_data_feeders/feed_data?value=");
        if (isSDKAtLeastP()) {
            if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
                requestPermissions(permissions, REQUEST_PERMISSION);
            } else {
                registerBroadcastReceiver();
            }

        } else {
            registerBroadcastReceiver();
        }

        try {

           // machineSerial = getArguments().getString("jsonMachine");
           // sensorSerial = getArguments().getString("jsonSocket");
            sensorMac = getArguments().getString("sensorMac");
            //current = getArguments().getString("jsonCurrent");
           // voltage = getArguments().getString("jsonVoltage");
            current="5Amps";
            if(current.equals("")) {
                // if((!current.equals(""))&& (!voltage.equals("null") || voltage.isEmpty()||!voltage.equals(""))) {
                Toast.makeText(appClass, "No value for current", Toast.LENGTH_SHORT).show();
            }else {
                if (current.contains("Amps")) {
                    String[] splitCurrent = current.split("Amps");
                    int CurrentValue = Integer.parseInt(splitCurrent[0]);
                    CurrentFinalValue = CurrentValue * 100;
                    Log.d(TAG, "getConfigPacket: cua" + CurrentFinalValue);
                } else {
                    String[] splitVoltage = current.split("Watt");
                    int CurrentVoltage = Integer.parseInt(splitVoltage[0]);
                   // String[] splitV = voltage.split("-");
                    //int splitFirstValue = Integer.parseInt(splitV[0]);
                    int VoltageDiv = CurrentVoltage / 230;
                    CurrentFinalValue = VoltageDiv * 1000;
                    Log.d(TAG, "onViewCreated: first" + CurrentFinalValue);
                }

                //need to file for client
           /* if (CurrentFinalValue <= 1000) {
                CurrentFinalValue = CurrentFinalValue / 3;
            } else {
                CurrentFinalValue = CurrentFinalValue / 5;
            }*/
//Checking the current length is less than 3 digit then convet to 4 digit
                //   binding.edtCurrent.setText(CurrentFinalValue+"mA");
                String machineCurrent = String.valueOf(CurrentFinalValue);
                if (machineCurrent.length() > 3) {
                    FinalValue = machineCurrent;
                    binding.edtCurrent.setText(FinalValue);
                } else {
                    FinalValue = String.format("%04d", CurrentFinalValue);
//            CurrentFinalValue = Integer.parseInt(formatted);
                    binding.edtCurrent.setText(FinalValue);
                }
            }
        } catch (Exception e) {
            appClass.showLocalError(mContext);
            e.printStackTrace();
            return;
        }

        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.edtCurrent.getText().toString().equals("")){
                    Toast.makeText(appClass, "No value for current", Toast.LENGTH_SHORT).show();
                }else{
                    value = Integer.parseInt(binding.edtCurrent.getText().toString());
                }
                if (binding.edtSSID.getText().toString().equals("")) {
                    appClass.showSnackBar(mContext, "Connect to wifi network");
                    return;
                }
                if (binding.edtBSSID.getText().toString().equals("")) {
                    appClass.showSnackBar(mContext, "Unknown BSSID");
                    return;
                }
                if (binding.edtPass.getText().toString().equals("")) {
                    appClass.showSnackBar(mContext, "Enter password");
                    return;
                }
                if (binding.edtHeartBeat.getText().toString().equals("")) {
                    appClass.showSnackBar(mContext, "Enter heartbeat interval");
                    return;
                }
                if (Integer.parseInt(binding.edtHeartBeat.getText().toString()) < 10) {
                    appClass.showSnackBar(mContext, "Heartbeat interval should be more than 10 seconds");
                    return;
                }
                saveWifiPassword();
            }
        });
    }
    void saveWifiPassword() {
        editor = preferences.edit();
        editor.putString("wifiPassword", binding.edtPass.getText().toString()).apply();
        dialog();
    }
    void dialog() {
        new MaterialAlertDialogBuilder(getContext()).setMessage("Please Confirm the Wifi Password")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                byte[] ssid = binding.edtSSID.getTag() == null ? ByteUtil.getBytesByString(binding.edtSSID.getText().toString())
                        : (byte[]) binding.edtSSID.getTag();
                byte[] password = ByteUtil.getBytesByString(binding.edtPass.getText().toString());
                byte[] bssid = TouchNetUtil.parseBssid2bytes(binding.edtBSSID.getText().toString());
                byte[] deviceCount = "1".getBytes();
//                byte[] broadcast = {(byte) (mPackageModeGroup.getCheckedRadioButtonId() == R.id.package_broadcast
//                        ? 1 : 0)};

                byte[] broadcast = {(byte) 1};

                if (mTask != null) {
                    mTask.cancelEsptouch();
                }
                if(value>=100){
                    if (value >= 100) {
                        String machineCurrents = String.valueOf(value);
                        if (machineCurrents.length() > 3) {
                            FinalValue = machineCurrents;
                            binding.edtCurrent.setText(FinalValue);
                            mTask = new EspSmartConfig(NewSmartConfigFragment.this);
                            mTask.execute(ssid, bssid, password, deviceCount, broadcast);
                        } else {
                            FinalValue = String.format("%04d", value);
//            CurrentFinalValue = Integer.parseInt(formatted);
                            binding.edtCurrent.setText(FinalValue);
                            mTask = new EspSmartConfig(NewSmartConfigFragment.this);
                            mTask.execute(ssid, bssid, password, deviceCount, broadcast);
                        }
                    } else if(value<=99) {
                        Toast.makeText(mContext, "Value range should be 100 - 9999", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(mContext, "Value range should be 100 - 9999", Toast.LENGTH_SHORT).show();
                }
            }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (!mDestroyed) {
                    registerBroadcastReceiver();
                }
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDestroyed = true;
        if (mReceiverRegistered) {
            getActivity().unregisterReceiver(mReceiver);
        }
    }

    private void onWifiChanged(WifiInfo info) {
        boolean disconnected = info == null
                || info.getNetworkId() == -1
                || "<unknown ssid>".equals(info.getSSID());
        if (disconnected) {
            binding.edtSSID.setText("");
            binding.edtSSID.setTag(null);
            appClass.showSnackBar(mContext, getResources().getString(R.string.no_wifi_connection));
            binding.btnSearch.setEnabled(false);
            if (isSDKAtLeastP()) {
                checkLocation();
            }
            if (mTask != null) {
                mTask.cancelEsptouch();
                mTask = null;
                new AlertDialog.Builder(mContext)
                        .setMessage(R.string.configure_wifi_change_message)
                        .setNegativeButton(android.R.string.cancel, null)
                        .show();
            }
        } else {
            String ssid = info.getSSID();
            if (ssid.startsWith("\"") && ssid.endsWith("\"")) {
                ssid = ssid.substring(1, ssid.length() - 1);
            }
            binding.edtSSID.setText(ssid);
            binding.edtSSID.setTag(ByteUtil.getBytesByString(ssid));
            byte[] ssidOriginalData = TouchNetUtil.getOriginalSsidBytes(info);
            binding.edtSSID.setTag(ssidOriginalData);
            String bssid = info.getBSSID();
            binding.edtBSSID.setText(bssid);
            binding.btnSearch.setEnabled(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int frequency = info.getFrequency();
                if (frequency > 4900 && frequency < 5900) {
                    // Connected 5G wifi. Device does not support 5G
                    appClass.showSnackBar(mContext, getResources().getString(R.string.wifi_5g_message));
                }
            }
        }
    }

    private void checkLocation() {
        boolean enable;
        // LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        // enable = locationManager != null && LocationManagerCompat.isLocationEnabled(locationManager);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // This is new method provided in API 28
            LocationManager locationManagers = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            if (!locationManagers.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                new MaterialAlertDialogBuilder(mContext).setTitle("GPS Enable").setMessage("Turn on GPS/Location").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                }).setNegativeButton("No", null).setCancelable(false).show();
            }
        } else {
            LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            enable = locationManager != null && LocationManagerCompat.isLocationEnabled(locationManager);
            if (!enable) {
                appClass.showSnackBar(mContext, getResources().getString(R.string.location_disable_message));
            }
        }

    }

    private void registerBroadcastReceiver() {
        IntentFilter filter = new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        if (isSDKAtLeastP()) {
            filter.addAction(LocationManager.PROVIDERS_CHANGED_ACTION);
        }
        getActivity().registerReceiver(mReceiver, filter);
        mReceiverRegistered = true;
    }

    private boolean isSDKAtLeastP() {
        return Build.VERSION.SDK_INT >= 28;
    }

    private String getConfigPacket() {
        if (configURL.equals("")) {
            appClass.showSnackBar(mContext, "Target server not defined");
            return null;
        }
        try {
            Log.d(TAG, "configHttpUrl: " + machine);
            Log.d(TAG, "configHttpUrl: " + socket);
            Log.d(TAG, "configHttpUrl: " + desk);


            String keepAliveInterval = binding.edtHeartBeat.getText().toString();
            // baseURLNew +"sensor_data_feeders/feed_data?value="+"#"+configURL + "#" +
            String baseUrl = binding.edtConfigUrl.getText().toString().replace("\n", "");
            keepAliveInterval = ("0000" + keepAliveInterval).substring(keepAliveInterval.length());
            String currentDate = new SimpleDateFormat("dd:MM:yy", Locale.getDefault()).format(new Date());
            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
            // ST* MAC_ID# HTTP_PAGE# MACHINE_CURRENT# KEEP_ALIVE# TIMESTAMP# DAY_STAMP *ED
//             CheckValue = 100;
//              String formatted = String.format("%04d", CheckValue);
            // CurrentFinalValue = Integer.parseInt(formatted);

            String configPacket =
                    "SST*" + sensorMac.toUpperCase() + "#" +
                            baseUrl + "#" +
                            FinalValue + "#" +
                            keepAliveInterval + "#" +
                            currentTime + "#" +
                            currentDate + "*ED";
            Log.d(TAG, "getConfigPacket_packet:" + configPacket.toString());
            return configPacket;

        } catch (Exception e) {
            appClass.showLocalError(mContext);
            e.printStackTrace();
            return null;
        }
    }

    private void showProgress() {
        binding.progressCircular.setVisibility(View.VISIBLE);
    }

    private void dismissProgress() {
        binding.progressCircular.setVisibility(View.GONE);
    }

    private void postCommissioningData(String sourceID, String sourceType, String destinationId, String destinationType) {
        try {
            showProgress();
            //qrcodes/mapping?access_token=Ta9R7yEZKzD7t5vkVtCLY8rq180igPc3FzfCpOzIY_o
            // &source_id=1&source_type=sensor&destination_id=2&destination_type=machine
            String api = mapping + accessTokenKey + accessToken + sourceIdKey + sourceID +
                    sourceTypeKey + sourceType + destinationIdKey + destinationId + destinationTypeKey + destinationType;
            Log.d(TAG, "postCommissioningData" + api);
            appClass.httpRequestNewNightHacks(mContext, api, null, Request.Method.GET, new VolleyCallback() {
                @Override
                public void OnSuccess(JSONObject object) {
                    Log.d(TAG, "OnSuccess:" + object);
                    dismissProgress();
                    try {
                  /*      String check = object.getString("success");
                        Log.d(TAG, "OnSuccess: check"+check);*/
                        if (object.getBoolean("success")) {
                            new MaterialAlertDialogBuilder(mContext).setTitle("Commissioning Status").
                                    setMessage("Commissioning success for " + sensorMac)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            parent.dismiss();
                                        }
                                    }).show();
                        } else {
                            String message = object.getString("error");
                            new MaterialAlertDialogBuilder(mContext).setTitle("Commissioning Status").
                                    setMessage(message + " For " + sensorMac)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            parent.dismiss();
                                        }
                                    }).show();
                        }
                    } catch (Exception e) {
                        appClass.showLocalError(mContext);
                        e.printStackTrace();
                    }
                }

                @Override
                public void OnFailure(VolleyError error) {
                    dismissProgress();
                    try {
                        if (error.networkResponse != null) {
                            statuscode = error.networkResponse.statusCode;
                        }
                        if (statuscode == 401) {
                            baseActivity.TokenInvalid();

                        }
                        Log.d(TAG, "OnFailure1:" + statuscode);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 0);
        } catch (Exception e) {
            dismissProgress();
            appClass.showLocalError(mContext);
        }
    }

    private void startPacketSender() {
        packetSender.start();
    }

    private void sendPacket() {
        S_Communication communication = new S_Communication();
        communication.stop();
        appClass.sendData(mContext, getConfigPacket());
    }

    public static class EspSmartConfig extends AsyncTask<byte[], IEsptouchResult, List<IEsptouchResult>> {
        private final Object mLock = new Object();
        boolean isReceiverRegistered = false;
        private WeakReference<NewSmartConfigFragment> mActivity;

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    String data = intent.getStringExtra("news");
                    Log.d(TAG, "------------" + data);
                    Log.d(TAG, "onReceive_data:" + data);
                    if (data.equalsIgnoreCase("OK")) {
                        mActivity.get().dismissProgress();
                        mActivity.get().packetSender.cancel();
                        new MaterialAlertDialogBuilder(context).setTitle("Commissioning Status").
                                setMessage("Commissioning success for " + sensorMac)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        parent.dismiss();
                                    }
                                }).show();
                        //mActivity.get().postCommissioningData(sensorSerial, "sensor", machineSerial, "machine");
                    } else if (data.contains("NOT OK")) {
                        mActivity.get().dismissProgress();
                        mActivity.get().packetSender.cancel();
                        new MaterialAlertDialogBuilder(mActivity.get().mContext).setTitle("Error Code 104 - Commissioning failed").
                                setMessage(data).show();
                    }
//                    else{
//                        new MaterialAlertDialogBuilder(mActivity.get().mContext).setTitle("Error Code 105 - Commissioning failed").
//                                setMessage(data).show();
//                    }
                } catch (Exception e) {
                    try {
                        Log.d(TAG, "onReceive_error:" + e.toString());
                        mActivity.get().dismissProgress();
                        mActivity.get().appClass.showLocalError(mActivity.get().mContext);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }

            }
        };
        private ProgressDialog mProgressDialog;
        private AlertDialog mResultDialog;
        private IEsptouchTask mEsptouchTask;

        EspSmartConfig(NewSmartConfigFragment mActivity) {
            this.mActivity = new WeakReference<>(mActivity);
            registerCustomReceiver(mActivity.mContext, receiver);
        }

        void registerCustomReceiver(Context context, BroadcastReceiver receiver) {
            if (!isReceiverRegistered) {
                IntentFilter intentFilter = new IntentFilter(ACTION_MyIntentService);
                intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
                context.registerReceiver(receiver, intentFilter);
                isReceiverRegistered = true;
            }

        }

        void unregisterReceiver() {
            if (isReceiverRegistered) {
                mActivity.get().getActivity().unregisterReceiver(receiver);
                isReceiverRegistered = false;
            }
        }

        void cancelEsptouch() {
            cancel(true);
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
            if (mResultDialog != null) {
                mResultDialog.dismiss();
            }
            if (mEsptouchTask != null) {
                mEsptouchTask.interrupt();
            }
        }

        @Override
        protected void onPreExecute() {
            Activity activity = mActivity.get().getActivity();
            mProgressDialog = new ProgressDialog(activity);
            mProgressDialog.setMessage(activity.getString(R.string.configuring_message));
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setOnCancelListener(dialog -> {
                synchronized (mLock) {
                    if (mEsptouchTask != null) {
                        mEsptouchTask.interrupt();
                    }
                }
            });
            mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, activity.getText(android.R.string.cancel),
                    (dialog, which) -> {
                        synchronized (mLock) {
                            if (mEsptouchTask != null) {
                                mEsptouchTask.interrupt();
                            }
                        }
                    });
            mProgressDialog.show();
        }

        @Override
        protected List<IEsptouchResult> doInBackground(byte[]... params) {
            NewSmartConfigFragment activity = mActivity.get();
            int taskResultCount;
            synchronized (mLock) {
                byte[] apSsid = params[0];
                byte[] apBssid = params[1];
                byte[] apPassword = params[2];
                byte[] deviceCountData = params[3];
                byte[] broadcastData = params[4];
                taskResultCount = deviceCountData.length == 0 ? -1 : Integer.parseInt(new String(deviceCountData));
                Context context = activity.getActivity().getApplicationContext();
                mEsptouchTask = new EsptouchTask(apSsid, apBssid, apPassword, context);
                mEsptouchTask.setPackageBroadcast(broadcastData[0] == 1);
                mEsptouchTask.setEsptouchListener(this::publishProgress);
                //End Here
            }
            return mEsptouchTask.executeForResults(taskResultCount);
        }

        @Override
        protected void onProgressUpdate(IEsptouchResult... values) {
            Context context = mActivity.get().getActivity();
            if (context != null) {
                IEsptouchResult result = values[0];
                Log.i(TAG, "EspTouchResult: " + result);
                String text = result.getBssid() + " is connected to the wifi";
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPostExecute(List<IEsptouchResult> result) {
            NewSmartConfigFragment activity = mActivity.get();
            activity.mTask = null;
            mProgressDialog.dismiss();
            if (result == null) {
                mResultDialog = new AlertDialog.Builder(activity.getActivity())
                        .setTitle("Error Code - 100")
                        .setMessage(R.string.configure_result_failed_port)
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
                mResultDialog.setCanceledOnTouchOutside(false);
                unregisterReceiver();
                return;
            }

            // check whether the task is cancelled and no results received
            IEsptouchResult firstResult = result.get(0);
            if (firstResult.isCancelled()) {
                unregisterReceiver();
                return;
            }
            // the task received some results including cancelled while
            // executing before receiving enough results

            if (!firstResult.isSuc()) {
                mResultDialog = new AlertDialog.Builder(activity.getActivity())
                        .setTitle("Error Code -102 "+R.string.configure_result_failed_port)
                        .setMessage(R.string.configure_result_failed)
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
                mResultDialog.setCanceledOnTouchOutside(false);
                unregisterReceiver();
                return;
            }
            try {
                if (firstResult.getBssid().toUpperCase().equalsIgnoreCase(sensorMac)) {
                    mIPAddress = firstResult.getInetAddress().getHostAddress();
                    Log.d(TAG, "onPostExecute: mi" + mIPAddress);
                    Toast.makeText(mActivity.get().mContext, "ipAddress" + mIPAddress, Toast.LENGTH_SHORT).show();
                    mPortNumber = 5000;
                    mActivity.get().startPacketSender();
                } else {
                    new MaterialAlertDialogBuilder(mActivity.get().mContext).setTitle("Error Code - 103").setMessage("Sensor Mac = "+sensorMac+" Smart Device MAc= "+firstResult.getBssid().toUpperCase() + (" - Mac id mismatch")).setPositiveButton("OK", null).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //            ArrayList<CharSequence> resultMsgList = new ArrayList<>(result.size());
//
//            for (IEsptouchResult touchResult : result) {
//                String message = activity.getString(R.string.configure_result_success_item,
//                        touchResult.getBssid(), touchResult.getInetAddress().getHostAddress());
//                Address = touchResult.getInetAddress().getHostAddress();
//                resultMsgList.add(message);
//            }
//            CharSequence[] items = new CharSequence[resultMsgList.size()];
//            for (CharSequence temp : resultMsgList) {
//                Log.d(TAG, temp.toString());
//            }
//            mActivity.get().configHttpUrl(ipAddress);
        }
    }

}
