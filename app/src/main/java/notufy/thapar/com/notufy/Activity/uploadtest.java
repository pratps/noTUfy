package notufy.thapar.com.notufy.Activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import notufy.thapar.com.notufy.R;

public class uploadtest extends ActionBarActivity {

    File file;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadtest);
        Button upload=(Button)findViewById(R.id.upload);
        Button choose=(Button)findViewById(R.id.choose);
        tv=(TextView)findViewById(R.id.filename);


        HashMap<String,String> map=new HashMap<String,String>();
        map.put("sender_user_code","8001");
        map.put("password","pass");
        map.put("to_group","3BECOE9_UCS602L");
        map.put("info","test");

        final Map<String,List<String>> params = new HashMap<String,List<String>>();
        Set<String> keys=map.keySet();
        String[] arr=keys.toArray(new String[keys.size()]);
        for(int i=0;i<keys.size();i++) {
            List<String> v = new ArrayList<>();
            v.add(map.get(arr[i]));
            params.put(arr[i], v);
        }
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("*/*");
                startActivityForResult(i, 1);
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // file=new File(Environment.getDownloadCacheDirectory().getPath().toString()+"/mypic.jpg");
                Ion.with(getApplicationContext())
                        .load("http://learntheeasyway.tk/notufy/app/teacher_message.php")
                        .setLogging("UPLOAD LOGS:", Log.ERROR)
                        .setMultipartFile("file_name", file)
                        .setMultipartParameters(params)
                        .asString()

                        .setCallback(new FutureCallback<String>() {
                            @Override
                            public void onCompleted(Exception e, String result) {
                                if(e!=null){
                                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                                }
                                if(result!=null)
                                Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_LONG).show();
                                else
                                    Toast.makeText(getApplicationContext(), "null", Toast.LENGTH_LONG).show();


                            }
                        });
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_uploadtest, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri uri=data.getData();

        String path= null;
        try {
            path = getPath(this,uri);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        file=new File(path);
        tv.setText(path);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }
}
