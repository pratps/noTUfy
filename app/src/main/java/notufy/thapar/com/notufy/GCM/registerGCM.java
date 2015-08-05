package notufy.thapar.com.notufy.GCM;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONObject;

import java.io.IOException;

import notufy.thapar.com.notufy.Asynctasks.CallbackAsync;
import notufy.thapar.com.notufy.Asynctasks.CallbackGCM;

/**
 * Created by prat on 3/28/2015.
 */
public class registerGCM extends AsyncTask<String,String,String> implements CallbackGCM{
    String PRO_ID;
    GoogleCloudMessaging gcm;
    Context mContext;
    String reg_id,msg;
    public registerGCM(String PRO_ID,Context mContext)
    {
        this.PRO_ID=PRO_ID;
        this.mContext=mContext;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            if (gcm == null) {
                gcm = GoogleCloudMessaging.getInstance(mContext);
            }
            Log.e("before", "background");
            reg_id = gcm.register(PRO_ID);
            msg = "Device registered, registration id=" + reg_id;
            Log.e("GCM message", msg);

        } catch (IOException ex) {
            msg = "Error :" + ex.getMessage();
            Log.e("gcm", msg);
            reg_id=null;
        }
        return reg_id;
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        setonGCMCallback(s);
    }


    @Override
    public void setonGCMCallback(String reg_id) {

    }


}
