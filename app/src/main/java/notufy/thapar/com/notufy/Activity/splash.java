package notufy.thapar.com.notufy.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;

import notufy.thapar.com.notufy.Asynctasks.AttemptLogin;
import notufy.thapar.com.notufy.GCM.gcm_config;
import notufy.thapar.com.notufy.GCM.registerGCM;
import notufy.thapar.com.notufy.R;
import notufy.thapar.com.notufy.config;

public class splash extends Activity {

    Context mContext;
    gcm_config gcm;
    config conf;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext=getApplicationContext();
        gcm=new gcm_config(mContext);
        conf=new config(mContext);
        final TextView t=(TextView)findViewById(R.id.gcm);

        screendimensions();
        if(gcm.checkPlayServices())
        {
            if(gcm.getGCMRegId().length()==0)
            {
                Log.e("Splash","GCM not registered");
                registerGCM();
            }
            else
            {
                Log.e("Splash","GCM registered");
                if(conf.getUsercode().length()==0)
                {
                    Log.e("Splash","User not logged in.");
                    intent=new Intent(splash.this,MainActivity.class);
                }
                else
                {
                    Log.e("Splash","User logged in.");
                    intent=new Intent(splash.this,logined.class);
                }
                launch(intent);
            }
        }
        else
        {
            Log.e("Splash","play services unavailable");
        }


    }

    private void send2server(String reg_id) {
        HashMap<String,String> map=new HashMap<String, String>();
        map.put("gcm_id",reg_id);
        new AttemptLogin(true,"Connecting to TU Sever...","http://172.31.149.12/notufy/gcm_id.php",getApplicationContext(),map,splash.this){

            public void setonCallback(JSONObject json) {
                super.setonCallback(json);
            }
        }.execute();
    }


    void launch(Intent intent)
    {
        startActivity(intent);
        finish();
    }


    void screendimensions(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        SharedPreferences.Editor di=getSharedPreferences("dimensions",MODE_PRIVATE).edit();
        di.putInt("width",width);
        di.putInt("height",height);
        di.commit();
    }
    void registerGCM()
    {
        if(conf.isNetworkConnectionAvailable())
        {

            new registerGCM(gcm_config.PRO_ID,mContext){
                @Override
                public void setonGCMCallback(String reg_id) {
                    super.setonGCMCallback(reg_id);
                    if(reg_id.length()>0) {
                        gcm.storeGCMRegId(reg_id);
                        if (conf.getUsercode().length() == 0) {
                            Log.e("Splash", "User not logged in.");
                            intent = new Intent(splash.this, MainActivity.class);
                        } else {
                            Log.e("Splash", "User logged in.");
                            intent = new Intent(splash.this, logined.class);
                        }
                        launch(intent);
                    }
                    else
                    {
                        Toast.makeText(mContext,"Some error occured",Toast.LENGTH_LONG);
                        //dialog error
                    }
                }
            }.execute();
        }
        else
        {
            Log.e("Splash","Network Unavailable");
            //dialog network;
        }
    }
}
