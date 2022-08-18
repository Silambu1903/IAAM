package com.rax.iaam.Others;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.Navigation;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.material.snackbar.Snackbar;
import com.rax.iaam.Callbacks.VolleyCallback;
import com.rax.iaam.Model.UserAssignment.BlockModel;
import com.rax.iaam.Model.UserAssignment.FloorModel;
import com.rax.iaam.Model.UserAssignment.LineModel;
import com.rax.iaam.Model.UserAssignment.SiteModel;
import com.rax.iaam.R;

import org.acra.ACRA;
import org.acra.annotation.AcraMailSender;
import org.acra.annotation.AcraToast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.rax.iaam.Others.FCM.ChannelID;

@AcraMailSender(mailTo = "mobileapp@raxgbc.co.in")
@AcraToast(resText = R.string.CrashReportMessage)
public class ApplicationClass extends Application {


    /*Night Hacks API */
    //public static final String baseURLNew = "https://uat-api.esgrobot.com/api/v1/";
    //public static final String smart_baseURLNew = "http://uat-api.esgrobot.com/api/v1/";
    public static final String baseURLNew = "https://api.esgrobot.com/api/v1/";
    public static final String smart_baseURLNew = "http://api.esgrobot.com/api/v1/";
    //for testing purpose
     //public static final String baseURLNew = "https://test-api.esgrobot.com/api/v1/";
     //public static final String smart_baseURLNew = "http://test-api.esgrobot.com/api/v1/";
    public static int userId = -1, userRoleId = -1;
    public static String clientSecret = "uk6MxHjWXo9oAOlQuJzRihXkJ-h4GfBf3OzdkZMjIyE";
    public static String clientIDNew = "jQsGmXCdoR24xEYSqfGHZBd_yz9EFBF0iwVSEQw_rok";
    public static String accessToken = "";
    public static String refreshToken = "";
    public static String createAt = "";
    public static String expireIn = "";
    public static String userEmail = "";
    public static String userRoleName = "";
    public static String startDate = "";
    public static String endDate = "";
    public static int filterselected_hiherachy_id;
    public static final String UserRole = "client_admin";
    public static final String
            signIn = "users/sign_in",
            signUp = "users/sign_up",
            renew = "users/renew",
            accessTokenKey = "access_token=",
            hierarchies = "hierarchies?",
            punctuality = "reports/punctuality?",
            qrcodes = "qrcodes/scan?",
            codeType = "&code_type=",
            decomission = "/decomission?",
            factoryWS = "hierarchies/factory_wise_status",
            machines_id = "machines",
            shiftHierarchies = "hierarchies/",
    //MachineDeatilsApi- Jeeva
    machineDetail = "machines/",
            idelTime = "reports/idletime_details",
            minutes = "reports/minutes",
            employee_details = "employees",
            maintenance = "issues",
            employee_profile = "employees/",
            years = "&year=",
    //NeedleTimeRunApi
    reports = "reports/",
    //EnvironmentalParameter
    epDetails = "ambient_environments/environment_sensor_data?",
            sensorTypeIdKey = "&sensor_type_id=",
            hierarchyIdKey = "&hierarchy_id=",
            startDateKey = "&start_date=",
            endDateKey = "&end_date=",
    //AssetStatus HeatMap
    assetStatus = "assets_status",
            heirarchyIds = "hierarchy_ids[]=",
            assetHeatMap = "asset_heat_map",
            sessionType = "&session_type=",
            yearKey = "year=";
    //Environmental Parameter
    public static final int TEMPERATURE_ID = 3,
            AIRQUALITY_ID = 4,
            LUX_ID = 5,
            HUMIDITY_ID = 6,
            NOISEQUALITY_ID = 7;
    public static int yearChipValue = 0;
    // mapping
    public static final String mapping = "qrcodes/mapping?",
            sourceIdKey = "&source_id=", sourceTypeKey = "&source_type=", destinationIdKey = "&destination_id=",
            destinationTypeKey = "&destination_type=";

    public static final String keyAcesstoken = "acessToken", keyRefreshtoken = "acessToken", keyNewUsername = "userName", keyNewPassword = "Password", keyNewQrHeight = "qrHeight", keyNewQrWidth = "qrWidth", keyNewSaveLocation = "saveLocation", keyNewIsFirstLauch = "isFirstLaunch", filterChipvalue = "filterObject";
    /*Night Hacks API*/

    public static final String keyUsername = "Username", keyPassword = "Password", keyQrHeight = "qrHeight", keyQrWidth = "qrWidth", keySaveLocation = "saveLocation", keyIsFirstLauch = "isFirstLaunch";
    public static final String macColumnName = "MacID", serialNoColumnName = "SerialNumber";
    public static final int superAdminRoleID = 1;
    public static final int machineDetailCurrentPage = 0;
    public static final int minimumPixel = 100, defaultPixels = 180;
    public static final int[] gColors = {
            Color.rgb(100, 181, 246), Color.rgb(253, 174, 31), Color.rgb(252, 119, 164), Color.rgb(110, 121, 230), Color.rgb(255, 122, 120), Color.rgb(48, 219, 136)
    };
    private static final String TAG = "ApplicationClass";
    public static int userID = -1, FIND_FILE_REQUEST_CODE = 1709, roleID = -1, clientID = -1;
    public static String password = "",
    configURL = baseURLNew + "sensor_data_feeders/feed_data?value=";
    public static String Packet, mIPAddress;
    public static int mPortNumber;
    public static int colorIndex = -1;
    public final String appPref = "IAAMPref";
    private final int httpRequestTimeout = 30000;

    public static int getRandomColors() {
        if (colorIndex < gColors.length - 1) {
            colorIndex++;
        } else {
            colorIndex = 0;
        }
        return colorIndex;
    }

    public static double parseTimeToMinutes(String hourFormat) {
        double minutes = 0;
        String[] split = hourFormat.split(":");
        try {
            minutes += Double.parseDouble(split[0]) * 60;
            minutes += Double.parseDouble(split[1]);
            minutes += Double.parseDouble(split[2]) / 60;
            return minutes;
        } catch (Exception e) {
            return -1;
        }

    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public String getWifiMacAddress() {
        try {
            String interfaceName = "wlan0";
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (!intf.getName().equalsIgnoreCase(interfaceName)) {
                    continue;
                }

                byte[] mac = intf.getHardwareAddress();
                if (mac == null) {
                    return "";
                }

                StringBuilder buf = new StringBuilder();
                for (byte aMac : mac) {
                    buf.append(String.format("%02X:", aMac));
                }
                if (buf.length() > 0) {
                    buf.deleteCharAt(buf.length() - 1);
                }
                return buf.toString().replace(":", "");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public String getCurrentDateTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyyHHmmss");
        Date date = new Date();
        return formatter.format(date);
    }

    public void showTimePicker(Context mContext, final TextView time) {
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(mContext,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        String hour = ("00" + hourOfDay).substring(String.valueOf(hourOfDay).length());
                        String min = ("00" + minute).substring(String.valueOf(minute).length());
                        time.setText(String.format("%s:%s:%s", hour, min, "00"));
                    }
                }, mHour, mMinute, true);
        timePickerDialog.show();
    }

    public boolean isValidMac(String mac) {
        Pattern p = Pattern.compile("^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$");
        Matcher m = p.matcher(mac);
        return m.matches();
    }


    public boolean checkStatus(JSONObject object) {
        try {
            return object.getInt("StatusCode") == 200;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }


    public void showNotification(String title, String message, PendingIntent pendingIntent) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ChannelID);
        notificationBuilder.setSmallIcon(R.drawable.ic_stat_name);
        notificationBuilder.setContentTitle(title);
        // notificationBuilder.setContentText(message);
        notificationBuilder.setStyle(new NotificationCompat.BigTextStyle()
                .bigText(message));
        if (pendingIntent != null) {
            notificationBuilder.setContentIntent(pendingIntent);
        }
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "IAAM";
            String description = "IoT Based Asset Tracking & Asset Management\n";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(ChannelID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NotificationID.getID(), notificationBuilder.build());
    }

    public void getFusedLocation(Context mContext, LocationCallback callback) {
        FusedLocationProviderClient locationProviderClient = new FusedLocationProviderClient(mContext);
        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setInterval(1000);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationProviderClient.requestLocationUpdates(request, callback, Looper.getMainLooper());
    }

    public JSONObject getDataAsObject(JSONObject object) throws Exception {
        return (JSONObject) object.get("Data");
    }

    public JSONArray getDataAsArray(JSONObject object) throws Exception {
        return (JSONArray) object.get("Data");
    }

    public void navigateTo(FragmentActivity activity, int fragmentIDinNavigation) {
        Navigation.findNavController((Activity) activity, R.id.nav_host_fragment).navigate(fragmentIDinNavigation);
    }

    public void goBack(FragmentActivity activity) {
        Navigation.findNavController((Activity) activity, R.id.nav_host_fragment).popBackStack();
    }

    public void navigateTo(FragmentActivity activity, int fragmentIDinNavigation, Bundle b) {
        Navigation.findNavController((Activity) activity, R.id.nav_host_fragment).navigate(fragmentIDinNavigation, b);
    }

    public void popupBackstack(FragmentActivity activity) {
        Navigation.findNavController((Activity) activity, R.id.nav_host_fragment).popBackStack();
    }

    public void showLocalError(Context mContext) {
        showSnackBar(mContext, "Error occurred");
    }

    public void showLNetworkError(Context mContext) {
        showSnackBar(mContext, mContext.getResources().getString(R.string.errorMessage));
    }

    public void showConnectionLNetworkError(Context mContext) {
        showSnackBar(mContext, mContext.getResources().getString(R.string.NetworkerrorMessage));
    }

    public void showConnectionLNetworksucess(Context mContext) {
        showSnackBar(mContext, mContext.getResources().getString(R.string.NetworkerrorMessage));
    }

    public void showSnackBar(Context context, String message) {
        try {
            Snackbar snackbar = Snackbar.make(((Activity) context).findViewById(R.id.root), message, Snackbar.LENGTH_SHORT);
            TextView tv = (TextView) snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snackbar.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendData(Context mContext, String message) {
        Intent mServiceIntent = new Intent(mContext,
                S_Communication.class);
        mServiceIntent.putExtra("test", message);
        mContext.startService(mServiceIntent);
    }

    public String getVolleyError(VolleyError error) throws Exception {
//        if (error instanceof TimeoutError) {
//            callBack.OnError("Timed out");
//        } else if (error instanceof NoConnectionError) {
//            callBack.OnError("No connection");
//        } else if (error instanceof AuthFailureError) {
//            callBack.OnError("Authentication failure");
//        } else if (error instanceof ServerError) {
//            callBack.OnError("Server error");
//        } else if (error instanceof NetworkError) {
//            callBack.OnError("Network error");
//        } else if (error instanceof ParseError) {
//            callBack.OnError("Error occured");
//        } else {
//            callBack.OnError("Error occured");
//        }
        return "";
    }

    public String getModelName() {
        String getModelMacId = "";
        String dName = android.os.Build.MODEL;
        String address = getWifiMacAddress();
        String trimMac = address.replaceAll(":", "").trim().toUpperCase();
        if (dName != null && trimMac != null) {
            getModelMacId = " - " + dName + " - " + trimMac;
            return getModelMacId;
        }
        return getModelMacId;
    }

    public void httpRequestNewNightHacks(Context mContext, String apiType, @Nullable String paramString, final int method, final VolleyCallback callBack, int timeOut) throws Exception {
        if (mContext == null) {
            throw new Exception("Null Context");
        }
        if (apiType == null) {
            throw new Exception("Null URL");
        }
        if (apiType.equals("")) {
            throw new Exception("Invalid URL");
        }
        if (callBack == null) {
            throw new Exception("Null CallBack");
        }
        if (timeOut < httpRequestTimeout) {
            timeOut = httpRequestTimeout;
        }

        HttpsTrustManager.allowAllSSL();
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        String URL = baseURLNew + apiType;

        Response.ErrorListener volleyErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null) {
                    Log.d(TAG, "Failure: " + error.networkResponse.statusCode);
                } else {
                    Log.d(TAG, "Failure: " + error.getMessage());
                }
                callBack.OnFailure(error);
            }
        };

        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    response = response.replaceAll("NaN", "0");
                    JSONObject object = new JSONObject(response);
                    Log.d(TAG, "Success: " + response);
                    callBack.OnSuccess(object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        StringRequest stringRequest = new StringRequest(method, URL, stringListener, volleyErrorListener) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
                // return "application/json;";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "*/*");
                params.put("Accept-Encoding", "gzip, deflate, br");
                params.put("Connection", "keep-alive");
                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                // return paramString == null ? null : paramString.getBytes("utf-8");
                return paramString == null ? null : paramString.getBytes();
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = new String(response.data);
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(timeOut, 0, 1.0f));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "URL: " + URL);
                Log.d(TAG, "Object: " + paramString);
                requestQueue.add(stringRequest);
            }
        }, 0);
    }

    /* public void postUserAssignment(Context mContext, int userID) {
         if (userID == -1) {
             showSnackBar(mContext, "User not selected");
             return;
         }
         if (getAssignedObject().equals("")) {
             showSnackBar(mContext, "No lines selected to assign");
             return;
         }
         ProgressDialog dialog = new ProgressDialog(mContext);
         dialog.setMessage("Assigning...");
         dialog.setCancelable(false);
         dialog.show();
         try {
             httpRequestNew(mContext, postUserAssign + "?lineids=" + getAssignedObject() + "&userid=" + userID, null, Request.Method.POST, new VolleyCallback() {
                 @Override
                 public void OnSuccess(JSONObject object) {
                     dialog.dismiss();
                     try {
                         if (checkStatus(object)) {
                             showSnackBar(mContext, "Assigned successfully");
                         } else {
                             showSnackBar(mContext, object.getString("Message"));
                         }
                     } catch (JSONException e) {
                         e.printStackTrace();
                     }
                 }

                 @Override
                 public void OnFailure(VolleyError error) {
                     showLNetworkError(mContext);
                     dialog.dismiss();
                 }
             }, 20000);
         } catch (Exception e) {
             dialog.dismiss();
             showSnackBar(mContext, "Error occurred");
             e.printStackTrace();
         }
     }

     private String getAssignedObject() {
         StringBuilder lineIds = new StringBuilder();
         for (SiteModel tempSite : UserAssignFragment.siteList) {
             for (BlockModel tempBlock : tempSite.getBlocks()) {
                 for (FloorModel tempFloor : tempBlock.getFloors()) {
                     for (LineModel tempLine : tempFloor.getLines()) {
                         if (tempLine.isAssigned()) {
                             lineIds.append(tempLine.getLineId()).append("%23");
                         }
                     }
                 }
             }
         }
         if (lineIds.toString().equals("")) {
             return "";
         }
         return lineIds.toString().substring(0, lineIds.toString().length() - 3);
     }
 */
    public boolean NetworkConnected() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        ACRA.init(this);
    }
}
