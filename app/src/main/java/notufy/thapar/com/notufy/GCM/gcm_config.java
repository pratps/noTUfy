package notufy.thapar.com.notufy.GCM;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

import notufy.thapar.com.notufy.Activity.MainActivity;

/**
 * Created by prat on 3/28/2015.
 */
public class gcm_config {

    Context context;
    public static String PRO_ID="1039596811736";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;


    public gcm_config(Context context)
    {
        this.context=context;
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }



    public boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, (Activity)context,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            return false;
        }
        return true;
    }



    public void storeGCMRegId(String regid) {
        SharedPreferences registry = context.getSharedPreferences("gcm_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = registry.edit();
        editor.putString("gcm_id", regid);
        editor.putInt("app_version", getAppVersion(context));
        editor.commit();
    }


    public String getGCMRegId()
    {
        SharedPreferences registry = context.getSharedPreferences("gcm_preferences", Context.MODE_PRIVATE);
        int currVersion=getAppVersion(context);
        String gcm_id=registry.getString("gcm_id", "");
        int storedAppVersion=registry.getInt("app_version",0);
        if(currVersion!=storedAppVersion)
        {
            return "";
        }
        return gcm_id;
    }



}
