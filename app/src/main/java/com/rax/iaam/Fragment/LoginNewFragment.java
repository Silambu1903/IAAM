package com.rax.iaam.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.rax.iaam.Activity.BaseActivity;
import com.rax.iaam.Callbacks.VolleyCallback;
import com.rax.iaam.Others.ApplicationClass;
import com.rax.iaam.R;
import com.rax.iaam.databinding.FragmentLoginBinding;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;
import static com.rax.iaam.Others.ApplicationClass.UserRole;
import static com.rax.iaam.Others.ApplicationClass.accessToken;
import static com.rax.iaam.Others.ApplicationClass.clientID;
import static com.rax.iaam.Others.ApplicationClass.clientIDNew;
import static com.rax.iaam.Others.ApplicationClass.clientSecret;
import static com.rax.iaam.Others.ApplicationClass.createAt;
import static com.rax.iaam.Others.ApplicationClass.expireIn;
import static com.rax.iaam.Others.ApplicationClass.keyAcesstoken;
import static com.rax.iaam.Others.ApplicationClass.keyNewPassword;
import static com.rax.iaam.Others.ApplicationClass.keyNewUsername;
import static com.rax.iaam.Others.ApplicationClass.keyPassword;
import static com.rax.iaam.Others.ApplicationClass.keyRefreshtoken;
import static com.rax.iaam.Others.ApplicationClass.keyUsername;
import static com.rax.iaam.Others.ApplicationClass.refreshToken;
import static com.rax.iaam.Others.ApplicationClass.renew;
import static com.rax.iaam.Others.ApplicationClass.roleID;
import static com.rax.iaam.Others.ApplicationClass.signIn;
import static com.rax.iaam.Others.ApplicationClass.superAdminRoleID;
import static com.rax.iaam.Others.ApplicationClass.userEmail;
import static com.rax.iaam.Others.ApplicationClass.userID;
import static com.rax.iaam.Others.ApplicationClass.userId;
import static com.rax.iaam.Others.ApplicationClass.userRoleId;
import static com.rax.iaam.Others.ApplicationClass.userRoleName;

public class LoginNewFragment extends Fragment {

    private static final String TAG = "LoginFragment";
    private ApplicationClass appClass;
    private FragmentLoginBinding binding;
    private Context mContext;
    BaseActivity activity;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    String abc, userName, passWord, refresh_Token;
    int statuscode;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_login, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getContext();
        appClass = (ApplicationClass) getActivity().getApplication();
        activity = (BaseActivity) getActivity();
        activity.getSupportActionBar().hide();
        activity.setDrawerEnabled(false);
        preferences = PreferenceManager.getDefaultSharedPreferences(mContext);

       if (appClass.NetworkConnected()) {
       }
       else if (!appClass.NetworkConnected()) {
           appClass.showConnectionLNetworkError(mContext);
       }
            binding.btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (appClass.NetworkConnected()) {
                        if (Validate()) {
                            try {
                                JSONObject object1 = new JSONObject();
                                object1.put("client_id", clientIDNew);
                                object1.put("client_secret", clientSecret);
                                object1.put("email", binding.edtUsername.getText().toString());
                                object1.put("password", binding.edtPassword.getText().toString());

                                binding.progressCircular.setVisibility(View.VISIBLE);
                                appClass.httpRequestNewNightHacks(mContext, signIn, object1.toString(), Request.Method.POST, new VolleyCallback() {
                                    @Override
                                    public void OnSuccess(JSONObject object) {
                                        Log.d(TAG, "OnSuccess:" + object);
                                        binding.progressCircular.setVisibility(View.GONE);
                                        activity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {

                                                if (object.has("access_token")) {
                                                    activity.handleNewLoginResponse(object, binding.edtUsername.getText().toString(),
                                                            binding.edtPassword.getText().toString(), binding.chkRemPass.isChecked());
                                                } else {
                                                    appClass.showSnackBar(mContext, "Invlaid Credentials");
                                                }}

                                        });


                                    }

                                    @Override
                                    public void OnFailure(VolleyError error) {
                                        binding.progressCircular.setVisibility(View.GONE);

                                    if (error.networkResponse != null) {
                                        statuscode = error.networkResponse.statusCode;
                                        Log.d(TAG, "Failure: " + error.networkResponse.statusCode);
                                    } else {
                                        Log.d(TAG, "Failure: " + error.getMessage());
                                    }
                                    if (statuscode == 401) {

                                    }
                                    Log.d(TAG, "OnFailure1:" + statuscode);

                                        appClass.showLNetworkError(mContext);
                                    }
                                }, 0);
                            } catch (Exception e) {
                                binding.progressCircular.setVisibility(View.GONE);
                                appClass.showLocalError(mContext);

                            }
                        }
                    }
                    else if (!appClass.NetworkConnected()) {
                        appClass.showConnectionLNetworkError(mContext);
                    }


                }
            });

        if (appClass.NetworkConnected()) {
            if (!preferences.getString(keyNewUsername, "").equals("")) {
                refreshToken();
                try {
                    binding.progressCircular.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject object2 = new JSONObject();
                                object2.put("client_id", clientIDNew);
                                object2.put("client_secret", clientSecret);
                                Log.d(TAG, "run:" + abc);
                                object2.put("refresh_token", preferences.getString(keyRefreshtoken, ""));

                                appClass.httpRequestNewNightHacks(mContext, renew, object2.toString(), Request.Method.POST, new VolleyCallback() {
                                    @Override
                                    public void OnSuccess(JSONObject obj) {
                                        Log.d(TAG, "OnSuccess:" + obj);
                                        binding.progressCircular.setVisibility(View.GONE);
                                        activity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {


                                                        String message = obj.getString("access_token");
                                                        if (message != null && !message.isEmpty()) {
                                                            Log.d(TAG, "run:" + message);


                                                            //  activity.handleNewTokenLoginResponse(obj, preferences.getString(keyAcesstoken, ""), preferences.getString(keyRefreshtoken, ""), true);
                                                            getRefreshTokenApi(refresh_Token, clientIDNew, clientSecret);
                                                        } else {
                                                            if (obj.getString("error").equals("invalid_grant")) {
                                                                new MaterialAlertDialogBuilder(mContext)
                                                                        .setTitle("Time Out")
                                                                        .setMessage("Session Token Expired...Login Again !")
                                                                        .setPositiveButton("RECONNECT", new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                getAccessToken();
                                                                            }
                                                                        }).show();
                                                            }
                                                            Toast.makeText(appClass, "Not-Connected", Toast.LENGTH_SHORT).show();

                                                        }



                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                        });


                                    }

                                    @Override
                                    public void OnFailure(VolleyError error) {
                                        binding.progressCircular.setVisibility(View.GONE);
                                        appClass.showLNetworkError(mContext);
                                    }
                                }, 0);


                            } catch (Exception e) {
                                binding.progressCircular.setVisibility(View.GONE);
                                e.printStackTrace();
                            }
                        }
                    }, 2000);

                } catch (Exception e) {
                    binding.progressCircular.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }
        }
        else if (!appClass.NetworkConnected()) {
            appClass.showConnectionLNetworkError(mContext);
        }




    }

    private void getRefreshTokenApi(String refresh_Token, String clientIDNew, String clientSecret) {
        String api = renew + "?client_id=" + clientIDNew + "&client_secret=" + clientSecret + "&refresh_token=" + refresh_Token;

        try {
            JSONObject postObject = new JSONObject();
            postObject.put("client_id", clientIDNew);
            postObject.put("client_secret", clientSecret);
            postObject.put("refresh_token", preferences.getString(keyRefreshtoken, ""));
            try {
                appClass.httpRequestNewNightHacks(mContext, api, postObject.toString(), Request.Method.POST, new VolleyCallback() {
                    @Override
                    public void OnSuccess(JSONObject object) {
                        try {
                            String message = null;
                            try {
                                message = object.getString("access_token");
                            } catch (JSONException e) {
                                e.printStackTrace();
                                message = null;
                            }
                            if (message != null && !message.isEmpty()) {
                                activity.handleNewTokenLoginResponse(object, preferences.getString(keyAcesstoken, ""), preferences.getString(keyRefreshtoken, ""), true);

                            } else if (object.getString("error").equals("invalid_grant")) {
                                Toast.makeText(mContext, "Login Expired !", Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void OnFailure(VolleyError error) {
                        error.printStackTrace();
                    }
                }, 2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getAccessToken() {
        try {
            JSONObject object1 = new JSONObject();
            object1.put("client_id", clientIDNew);
            object1.put("client_secret", clientSecret);
            object1.put("email", preferences.getString(keyNewUsername, ""));
            object1.put("password", preferences.getString(keyNewPassword, ""));

            binding.progressCircular.setVisibility(View.VISIBLE);
            appClass.httpRequestNewNightHacks(mContext, signIn, object1.toString(), Request.Method.POST, new VolleyCallback() {
                @Override
                public void OnSuccess(JSONObject object) {
                    Log.d(TAG, "OnSuccess:" + object);
                    binding.progressCircular.setVisibility(View.GONE);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (object.has("access_token")) {
                                activity.handleNewLoginResponse(object, binding.edtUsername.getText().toString(),
                                        binding.edtPassword.getText().toString(), binding.chkRemPass.isChecked());
                            } else {
                                appClass.showSnackBar(mContext, "Invlaid Credentials");
                            }
                        }
                    });


                }

                @Override
                public void OnFailure(VolleyError error) {
                    binding.progressCircular.setVisibility(View.GONE);
                    appClass.showLNetworkError(mContext);
                }
            }, 0);
        } catch (Exception e) {
            binding.progressCircular.setVisibility(View.GONE);
            appClass.showLocalError(mContext);

        }
    }

    private Boolean Validate() {
        if (binding.edtUsername.getText().toString().length() == 0) {
            binding.edtUsername.setError("Enter Username");
            return false;
        }
        if (binding.edtPassword.getText().toString().length() == 0) {
            binding.edtPassword.setError("Enter Password");
            return false;
        }
        return true;
    }

    public void refreshToken() {
        preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        refresh_Token = preferences.getString(keyRefreshtoken, "");
        abc = preferences.getString(keyRefreshtoken, "");
        System.out.println(abc);
        Log.d(TAG, "refreshToken:" + abc);

    }
}
