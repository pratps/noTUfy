package notufy.thapar.com.notufy.Asynctasks;

/**
 * Created by pranav vij on 4/27/2015.
 */

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import notufy.thapar.com.notufy.R;


public class AttemptLogin_With_File implements CallbackAsync_With_File {
    Context context;
    String LOGIN_URL;
    String message;
    Boolean show;
    ImageView to_remove;
    Context class_context;
    String File_Address;
    CircleProgressBar circleProgressBar;
    private static HashMap<String, String> map;
    public AttemptLogin_With_File(ImageView to_remove, CircleProgressBar circleProgressBar, String login_url, Context context, HashMap<String, String> map, Context class_context, String File_Address) {
        this.LOGIN_URL = login_url;
        this.context = context;
        this.map = map;
        this.to_remove=to_remove;
        this.show = show;
        this.File_Address=File_Address;
        this.message = message;
        this.circleProgressBar=circleProgressBar;
        this.class_context = class_context;
    }

    public void execute() {
        final Map<String,List<String>> params = new HashMap<String,List<String>>();
        Set<String> keys=map.keySet();
        String[] arr=keys.toArray(new String[keys.size()]);
        for(int i=0;i<keys.size();i++) {
            List<String> v = new ArrayList<>();
            v.add(map.get(arr[i]));
            params.put(arr[i], v);
        }
        to_remove.setVisibility(ImageView.GONE);
        circleProgressBar.setVisibility(CircleProgressBar.VISIBLE);
        circleProgressBar.setProgress(0);
        circleProgressBar.circleBackgroundEnabled();
        circleProgressBar.setColorSchemeColors(R.color.black, R.color.indigo_500);
        Log.d("hello","sdsknskln");
        Ion.with(context)
                .load(LOGIN_URL)
                .setLogging("UPLOAD LOGS:", Log.ERROR)
                .uploadProgressHandler(new ProgressCallback() {
                    @Override
                    public void onProgress(long downloaded, long total) {
                        circleProgressBar.setProgress((int)(100*(Float.parseFloat(""+downloaded)/Float.parseFloat(""+total))));
                    }
                })
                .setMultipartFile("file_name", new File(File_Address))
                .setMultipartParameters(params)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e != null) {
                            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                        }
                        if (result != null) {
                            Toast.makeText(context, result.toString(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, "null", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    public void setonCallback(JSONObject json) {

    }
}
