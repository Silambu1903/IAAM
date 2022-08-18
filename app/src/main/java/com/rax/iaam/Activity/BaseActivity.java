package com.rax.iaam.Activity;

import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.rax.iaam.Fragment.DialogFragment;
import com.rax.iaam.Others.ApplicationClass;
import com.rax.iaam.Others.Menus;
import com.rax.iaam.R;
import com.rax.iaam.databinding.ActivityMainBinding;

import org.json.JSONObject;

import java.io.File;

import static com.rax.iaam.Others.ApplicationClass.accessToken;
import static com.rax.iaam.Others.ApplicationClass.createAt;
import static com.rax.iaam.Others.ApplicationClass.defaultPixels;
import static com.rax.iaam.Others.ApplicationClass.expireIn;
import static com.rax.iaam.Others.ApplicationClass.filterChipvalue;
import static com.rax.iaam.Others.ApplicationClass.keyAcesstoken;
import static com.rax.iaam.Others.ApplicationClass.keyIsFirstLauch;
import static com.rax.iaam.Others.ApplicationClass.keyNewIsFirstLauch;
import static com.rax.iaam.Others.ApplicationClass.keyNewPassword;
import static com.rax.iaam.Others.ApplicationClass.keyNewQrHeight;
import static com.rax.iaam.Others.ApplicationClass.keyNewQrWidth;
import static com.rax.iaam.Others.ApplicationClass.keyNewSaveLocation;
import static com.rax.iaam.Others.ApplicationClass.keyNewUsername;
import static com.rax.iaam.Others.ApplicationClass.keyPassword;
import static com.rax.iaam.Others.ApplicationClass.keyQrHeight;
import static com.rax.iaam.Others.ApplicationClass.keyQrWidth;
import static com.rax.iaam.Others.ApplicationClass.keyRefreshtoken;
import static com.rax.iaam.Others.ApplicationClass.keySaveLocation;
import static com.rax.iaam.Others.ApplicationClass.keyUsername;
import static com.rax.iaam.Others.ApplicationClass.refreshToken;
import static com.rax.iaam.Others.ApplicationClass.superAdminRoleID;
import static com.rax.iaam.Others.ApplicationClass.userEmail;
import static com.rax.iaam.Others.ApplicationClass.userId;
import static com.rax.iaam.Others.ApplicationClass.userRoleId;
import static com.rax.iaam.Others.ApplicationClass.userRoleName;

public class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";
    ApplicationClass appClass;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    NavController navController;
    AppBarConfiguration mAppBarConfiguration;
    ActivityMainBinding binding;
    private Context mContext;
    private String fcmToken, token, refreshtoken;
    private boolean launchedFromNotification = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mContext = this;
        Menus.addMenus();
        appClass = (ApplicationClass) getApplication();
        setSupportActionBar(binding.included.toolbar);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        // Later app bar configuration will be configured based on user role and menus
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.loginFragment2)
                .setDrawerLayout(binding.drawerLayout)
                .build();
        setUpNavigationUI(mAppBarConfiguration);
        preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                fcmToken = instanceIdResult.getToken();
            }
        });
        loadSettings();
        loadNewSettings();
        try {
            if (getIntent().getExtras() != null) {
                if (getIntent().getExtras().getString("openedFrom").equals("Notification")) {
                    launchedFromNotification = true;
                }
            }
        } catch (Exception e) {
            launchedFromNotification = false;
            e.printStackTrace();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    public String getPathFromUri(final Context context, final Uri uri) {
        // DocumentProvider
        if (DocumentsContract.isDocumentUri(context, uri)) {
            if ("com.android.externalstorage.documents".equals(
                    uri.getAuthority())) {// ExternalStorageProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                } else {
                    return "/stroage/" + type + "/" + split[1];
                }
            } else if ("com.android.providers.downloads.documents".equals(
                    uri.getAuthority())) {// DownloadsProvider
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if ("com.android.providers.media.documents".equals(
                    uri.getAuthority())) {// MediaProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                contentUri = MediaStore.Files.getContentUri("external");
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {//MediaStore
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {// File
            return uri.getPath();
        }
        return null;
    }

    private String getDataColumn(Context context, Uri uri, String selection,
                                 String[] selectionArgs) {
        Cursor cursor = null;
        final String[] projection = {MediaStore.Files.FileColumns.DATA
        };
        try {
            cursor = getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int cindex = cursor.getColumnIndexOrThrow(projection[0]);
                return cursor.getString(cindex);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private void loadSettings() {
        if (!preferences.getBoolean(keyIsFirstLauch, false)) {
            editor = preferences.edit();
            editor.putBoolean(keyIsFirstLauch, true);
            editor.putInt(keyQrHeight, defaultPixels);
            editor.putInt(keyQrWidth, defaultPixels);
            File saveLocation = new File(
                    Environment.getExternalStorageDirectory() + "/IAAM");
            if (!saveLocation.exists()) {
                saveLocation.mkdirs();
            }
            editor.putString(keySaveLocation, saveLocation.getAbsolutePath());
            editor.apply();
        }
    }

    public AppBarConfiguration getAppBarConfiguration(int roleId, int[] menuIds) {
        if (roleId == superAdminRoleID) {
            binding.navView.getMenu().clear();
            binding.navView.inflateMenu(R.menu.activity_main_drawer_sa);
            mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_dashboard, R.id.nav_devices, R.id.nav_client,
                    R.id.nav_device_assign, R.id.nav_change_password)
                    .setDrawerLayout(binding.drawerLayout)
                    .build();
        } else {
            binding.navView.getMenu().clear();
            binding.navView.inflateMenu(R.menu.activity_main_drawer_ca);
            for (int i = 0; i < binding.navView.getMenu().size(); i++) {
                if (binding.navView.getMenu().getItem(i).getItemId() == R.id.nav_logout) {
                    continue;
                }
                binding.navView.getMenu().getItem(i).setVisible(false);
            }
            for (int menu : Menus.getMenus(menuIds)) {
                binding.navView.getMenu().findItem(menu).setVisible(true);
            }
            for (int i = 0; i < binding.navView.getMenu().size(); i++) {
                if (binding.navView.getMenu().getItem(i).getSubMenu() != null) {
                    for (int j = 0; j < binding.navView.getMenu().getItem(i).getSubMenu().size(); j++) {
                        if (binding.navView.getMenu().getItem(i).getSubMenu().getItem(j).isVisible()) {
                            binding.navView.getMenu().getItem(i).setVisible(true);
                            break;
                        }
                    }
                }
            }
            mAppBarConfiguration = new AppBarConfiguration.Builder(Menus.getMenus(menuIds))
                    .setDrawerLayout(binding.drawerLayout)
                    .build();
        }
        return mAppBarConfiguration;
    }//New


    public void setUpNavigationUI(AppBarConfiguration appBarConfiguration) {
        navController.setGraph(R.navigation.login);
        NavigationUI.setupWithNavController(binding.navView, navController);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }

    public void updateNavigationUi(AppBarConfiguration mAppBarConfiguration, int roleId) {
        Log.d(TAG, "updateNavigationUi: Start");
        getSupportActionBar().show();
        setDrawerEnabled(true);
        navController.setGraph(R.navigation.mobile_navigation);
        NavGraph graph = navController.getGraph();
        if (roleId == superAdminRoleID) {
            graph.setStartDestination(R.id.nav_dashboard);
        } else {
            menuloop:
            for (int i = 0; i < binding.navView.getMenu().size(); i++) {
                if (binding.navView.getMenu().getItem(i).isVisible()) {
                    if (binding.navView.getMenu().getItem(i).getSubMenu() != null) {
                        for (int j = 0; j < binding.navView.getMenu().getItem(i).getSubMenu().size(); j++) {
                            if (binding.navView.getMenu().getItem(i).getSubMenu().getItem(j).isVisible()) {
                                if (binding.navView.getMenu().getItem(i).getSubMenu().getItem(j).getIcon() != null) {
                                    graph.setStartDestination(binding.navView.getMenu().getItem(i).getSubMenu().getItem(j).getItemId());
                                    break menuloop;
                                }
                            }
                        }
                    } else {
                        if (binding.navView.getMenu().getItem(i).isVisible()) {
                            if (binding.navView.getMenu().getItem(i).getIcon() != null) {
                                graph.setStartDestination(binding.navView.getMenu().getItem(i).getItemId());
                                break;
                            }
                        }
                    }
                }
            }
        }
        navController.setGraph(graph);
        NavigationUI.setupWithNavController(binding.navView, navController);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        binding.navView.setNavigationItemSelectedListener(item -> {
            try {
                if (item.getItemId() == R.id.nav_logout) {
                    binding.drawerLayout.closeDrawers();
                    new MaterialAlertDialogBuilder(mContext).setTitle("Logout").setMessage("Do you want to logout ?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            editor = preferences.edit();
                            editor.putString(keyUsername, "");
                            editor.putString(keyPassword, "");
                            editor.apply();
                            goToLogin(mAppBarConfiguration);
                            appClass.showSnackBar(mContext, "Signed out successfully");
                        }
                    }).setNegativeButton("No", null).show();
                } else {
                    appClass.navigateTo(BaseActivity.this, item.getItemId());
                    binding.drawerLayout.closeDrawers();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        });
        Log.d(TAG, "updateNavigationUi: End");

    }

    public void goToLogin(AppBarConfiguration mAppBarConfiguration) {
        navController.setGraph(R.navigation.login);
        NavigationUI.setupWithNavController(binding.navView, navController);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
    }

    public void setDrawerEnabled(boolean enabled) {
        int lockMode = enabled ? DrawerLayout.LOCK_MODE_UNLOCKED :
                DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
        binding.drawerLayout.setDrawerLockMode(lockMode);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void handleNewLoginResponse(JSONObject object, String userName, String password, boolean remember) {
        View view = binding.navView.getHeaderView(0);
        TextView txtusername, txtrolename;
        txtusername = view.findViewById(R.id.txtMain);
        txtrolename = view.findViewById(R.id.textView);
        try {

            if (object.has("access_token")) {
                JSONObject tempObj = object.getJSONObject("access_token");

                accessToken = tempObj.getString("token");
                token = accessToken;
                Log.d(TAG, "----------1-->" + token);
                refreshToken = tempObj.getString("refresh_token");
                refreshtoken = accessToken;
                Log.d(TAG, "----------2-->" + refreshtoken);
                createAt = tempObj.getString("created_at");
                expireIn = tempObj.getString("expires_in");
            }


            if (object.has("user")) {
                JSONObject tempObj1 = object.getJSONObject("user");
                userId = tempObj1.getInt("id");
                userEmail = tempObj1.getString("email");
                if (tempObj1.has("user_role")) {
                    JSONObject tempObj2 = tempObj1.getJSONObject("user_role");
                    userRoleName = tempObj2.getString("name");
                    userRoleId = tempObj2.getInt("id");
                }
            }
            txtusername.setText(userName);
            txtrolename.setText(userRoleName);
            appClass.showSnackBar(mContext, "token received");
            /********************************************************************/
            /*    userID = appClass.getDataAsObject(object).getInt("UserId");
            roleID = appClass.getDataAsObject(object).getInt("RoleId");
            clientID = appClass.getDataAsObject(object).getInt("ClientId");
            configURL = appClass.getDataAsObject(object).getString("ConfigURL");
            String menuIds = appClass.getDataAsObject(object).getString("MenuList");
            String[] tempMenus = menuIds.split("#");
            int[] mIds = new int[tempMenus.length];
            for (int i = 0; i < tempMenus.length; i++) {
                mIds[i] = Integer.parseInt(tempMenus[i]);
            }*/
            postNewMobile(userRoleId);
            if (remember) {
                editor = preferences.edit();
                editor.putString(keyNewUsername, userName);
                editor.putString(keyNewPassword, password);
                editor.putString(keyAcesstoken, accessToken);
                editor.putString(keyRefreshtoken, refreshToken);
                Log.d(TAG, "-----------" + accessToken);
                Log.d(TAG, "-----------" + refreshToken);
                editor.apply();
            }
        } catch (Exception e) {
            appClass.showLocalError(mContext);
            e.printStackTrace();
        }
    }

    public void handleNewTokenLoginResponse(JSONObject object, String acesstoken, String refreshtoken, boolean remember) {
        View view = binding.navView.getHeaderView(0);
        TextView txtusername, txtrolename;
        txtusername = view.findViewById(R.id.txtMain);
        txtrolename = view.findViewById(R.id.textView);

        try {
            Log.d(TAG, "handleNewTokenLoginResponse:" + object);
            accessToken = object.getString("access_token");
            Log.d(TAG, "handleNewTokenLoginResponse:" + acesstoken);
            refreshToken = object.getString("refresh_token");
            createAt = object.getString("created_at");
            expireIn = object.getString("expires_in");

            postNewMobile(userRoleId);
            if (remember) {
                editor = preferences.edit();
                editor.putString(keyAcesstoken, acesstoken);
                editor.putString(keyRefreshtoken, refreshtoken);
                Log.d(TAG, "------+++-----" + refreshtoken);
                editor.apply();
            }
        } catch (Exception e) {
            appClass.showLocalError(mContext);
            e.printStackTrace();
        }
    }

    private void postNewMobile(int userRoleId) {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new Runnable() {
                        @Override
                        public void run() {
                            updateNewNavigationUi(getNewAppBarConfiguration(userRoleId), userRoleId);
                        }
                    }.run();
                }
            });
        } catch (Exception e) {
            appClass.showLocalError(mContext);
            e.printStackTrace();
        }
    }

    public AppBarConfiguration getNewAppBarConfiguration(int roleId) {
        if (roleId == userRoleId) {
            binding.navView.getMenu().clear();
            binding.navView.inflateMenu(R.menu.activity_main_drawer_sa);
            mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_dashboard, R.id.nav_devices, R.id.nav_client,
                    R.id.nav_device_assign, R.id.nav_change_password)
                    .setDrawerLayout(binding.drawerLayout)
                    .build();
        }
        /*else {
            binding.navView.getMenu().clear();
            binding.navView.inflateMenu(R.menu.activity_main_drawer_ca);
            for (int i = 0; i < binding.navView.getMenu().size(); i++) {
                if (binding.navView.getMenu().getItem(i).getItemId() == R.id.nav_logout) {
                    continue;
                }
                binding.navView.getMenu().getItem(i).setVisible(false);
            }
            for (int menu : Menus.getMenus(menuIds)) {
                binding.navView.getMenu().findItem(menu).setVisible(true);
            }
            for (int i = 0; i < binding.navView.getMenu().size(); i++) {
                if (binding.navView.getMenu().getItem(i).getSubMenu() != null) {
                    for (int j = 0; j < binding.navView.getMenu().getItem(i).getSubMenu().size(); j++) {
                        if (binding.navView.getMenu().getItem(i).getSubMenu().getItem(j).isVisible()) {
                            binding.navView.getMenu().getItem(i).setVisible(true);
                            break;
                        }
                    }
                }
            }
            mAppBarConfiguration = new AppBarConfiguration.Builder(Menus.getMenus(menuIds))
                    .setDrawerLayout(binding.drawerLayout)
                    .build();
        }*/
        return mAppBarConfiguration;
    }

    public void updateNewNavigationUi(AppBarConfiguration mAppBarConfiguration, int roleId) {
        Log.d(TAG, "updateNavigationUi: Start");
        getSupportActionBar().show();
        setDrawerEnabled(true);
        navController.setGraph(R.navigation.mobile_navigation);
        NavGraph graph = navController.getGraph();
        if (roleId == userRoleId) {
            graph.setStartDestination(R.id.nav_dashboard);
        }
       /* else {
            menuloop:
            for (int i = 0; i < binding.navView.getMenu().size(); i++) {
                if (binding.navView.getMenu().getItem(i).isVisible()) {
                    if (binding.navView.getMenu().getItem(i).getSubMenu() != null) {
                        for (int j = 0; j < binding.navView.getMenu().getItem(i).getSubMenu().size(); j++) {
                            if (binding.navView.getMenu().getItem(i).getSubMenu().getItem(j).isVisible()) {
                                if (binding.navView.getMenu().getItem(i).getSubMenu().getItem(j).getIcon() != null) {
                                    graph.setStartDestination(binding.navView.getMenu().getItem(i).getSubMenu().getItem(j).getItemId());
                                    break menuloop;
                                }
                            }
                        }
                    } else {
                        if (binding.navView.getMenu().getItem(i).isVisible()) {
                            if (binding.navView.getMenu().getItem(i).getIcon() != null) {
                                graph.setStartDestination(binding.navView.getMenu().getItem(i).getItemId());
                                break;
                            }
                        }
                    }
                }
            }
        }*/
        navController.setGraph(graph);
        NavigationUI.setupWithNavController(binding.navView, navController);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        binding.navView.setNavigationItemSelectedListener(item -> {
            try {
                if (item.getItemId() == R.id.nav_logout) {
                    binding.drawerLayout.closeDrawers();
                    new MaterialAlertDialogBuilder(mContext).setTitle("Logout").setMessage("Do you want to logout ?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            editor = preferences.edit();
                            editor.putString(keyNewUsername, "");
                            editor.putString(keyNewPassword, "");
                            editor.putString(keyAcesstoken, "");
                            editor.putString(keyRefreshtoken, "");
                            editor.putString(filterChipvalue, "");
                            editor.clear();
                            editor.apply();
                            goToLogin(mAppBarConfiguration);
                            appClass.showSnackBar(mContext, "Signed out successfully");
                        }
                    }).setNegativeButton("No", null).show();
                } else {
                    appClass.navigateTo(BaseActivity.this, item.getItemId());
                    binding.drawerLayout.closeDrawers();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        });
        Log.d(TAG, "updateNavigationUi: End");

    }

    private void loadNewSettings() {
        if (!preferences.getBoolean(keyNewIsFirstLauch, false)) {
            editor = preferences.edit();
            editor.putBoolean(keyNewIsFirstLauch, true);
            editor.putInt(keyNewQrWidth, defaultPixels);
            editor.putInt(keyNewQrHeight, defaultPixels);
            File saveLocation = new File(
                    Environment.getExternalStorageDirectory() + "/IAAM");
            if (!saveLocation.exists()) {
                saveLocation.mkdirs();
            }
            editor.putString(keyNewSaveLocation, saveLocation.getAbsolutePath());
            editor.apply();
        }
    }

    public void TokenInvalid() {
        new MaterialAlertDialogBuilder(mContext).setTitle("Access Token Invalid").setMessage("Do you want to logout ?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editor = preferences.edit();
                editor.putString(keyNewUsername, "");
                editor.putString(keyNewPassword, "");
                editor.putString(keyAcesstoken, "");
                editor.putString(keyRefreshtoken, "");
                editor.putString(filterChipvalue, "");
                editor.clear();
                editor.apply();
                editor.commit();

                goToLogin(mAppBarConfiguration);
                appClass.showSnackBar(mContext, "Signed out successfully");
            }
        }).setCancelable(false).show();
    }
}
