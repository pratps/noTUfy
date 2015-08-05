package notufy.thapar.com.notufy.Asynctasks;


import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AttemptLogin implements CallbackAsync{
    ProgressDialog pDialog;
    Context context;
    String LOGIN_URL;
    String message;
    Boolean show;
    Context class_context;
    private static HashMap<String, String> map;
    public AttemptLogin(Boolean show, String message, String login_url, Context context, HashMap<String, String> map, Context class_context) {
        this.LOGIN_URL = login_url;
        this.context = context;
        this.map = map;
        this.show = show;
        this.message = message;
        this.class_context = class_context;
    }

    public void execute() {

        Map<String,List<String>> params = new HashMap<String,List<String>>();
        Set<String> keys=map.keySet();
        String[] arr=keys.toArray(new String[keys.size()]);
        for(int i=0;i<keys.size();i++){
            List<String> v=new ArrayList<>();
            v.add(map.get(arr[i]));
            params.put(arr[i],v);
        }
        pDialog = new ProgressDialog(class_context);
        if(show){
            pDialog.setMessage(message);//Message to configure
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        Ion.with(class_context)
                .load(LOGIN_URL)
                .setBodyParameters(params)
                .asJsonObject()
                
                .setCallback(new FutureCallback<JsonObject>() {
                    public void onCompleted(Exception e, JsonObject result) {
                        if(show){
                            pDialog.dismiss();}
                        try {

                            if(result!=null) {
                                Log.e("response", result.toString());
                                setonCallback(new JSONObject(result.toString()));
                            }
                            else
                            {
                                Log.e("response","is null");
                                setonCallback(null);
                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                });


    }

    public void setonCallback(JSONObject json) {

    }
}
