package notufy.thapar.com.notufy.Activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import org.json.JSONObject;

import java.io.File;
import java.net.URISyntaxException;
import java.util.HashMap;

import notufy.thapar.com.notufy.Asynctasks.AttemptLogin;
import notufy.thapar.com.notufy.Asynctasks.AttemptLogin_With_File;
import notufy.thapar.com.notufy.R;
import notufy.thapar.com.notufy.config;

public class SendSubCoord extends ActionBarActivity {

    String server_file="/teacher_message.php",file_path="";
    String reg_info="^[a-z|A-Z| |.]+$";

    Context mContext;
    Toolbar mToolBar;
    TextView attach_text;
    int info_error=1,file_set=0;
    ImageView attachment_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sub_coord);
        final EditText info=(EditText)findViewById(R.id.info);





        mToolBar = (Toolbar) findViewById(R.id.screen_default_toolbar);
        mToolBar.setTitle("bla bla");

        final CircleProgressBar progressBar=(CircleProgressBar)findViewById(R.id.Customprogress);

        setSupportActionBar(mToolBar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        //getActionBar().setDisplayHomeAsUpEnabled(getDisplayHomeAsUp());
        //getActionBar().setHomeButtonEnabled(getHomeButtonEnabled());


        mContext=this;
        int padding_in_dp = 10;  // 6 dps
        final float scale = getResources().getDisplayMetrics().density;
        int px = (int) (padding_in_dp * scale + 0.5f);
        info.setPadding(px, px, px, px);
        attach_text=(TextView)findViewById(R.id.attach_text);
        RelativeLayout b=(RelativeLayout)findViewById(R.id.sendmessage);
        RelativeLayout attach=(RelativeLayout)findViewById(R.id.attachment);
        attach.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("*/*");
                startActivityForResult(i, 1);
            }
        });
        info.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String i=s.toString();
                if(s==null||s=="")
                {

                    info_error=1;
                }
                else
                {
                    if(i.matches(reg_info))
                    {
                        info.setTextColor(Color.parseColor("#000000"));
                        info_error=0;
                    }
                    else
                    {
                        info.setTextColor(Color.parseColor("#ff0000"));
                        info_error=1;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                config conf=new config(mContext);
                if(info_error!=1) {

                    if(conf.isNetworkConnectionAvailable()) {


                        String in = info.getText().toString();
                        String groups = "";
                        for (String s : SendTeacher.chosenGroupcodes) {
                            groups += s + " ";
                        }
                        groups = groups.trim();
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("sender_user_code", "8001");
                        map.put("password", "pass");
                        map.put("to_group", "3BECOE9_UCS602L");
                        map.put("info", in);
                        if(file_set==0) {

                            new AttemptLogin(true, "Sending message", config.URL + server_file, mContext, map, mContext) {

                                public void setonCallback(JSONObject json) {
                                    super.setonCallback(json);
                                    if (json != null)
                                        Toast.makeText(mContext, json.toString(), Toast.LENGTH_LONG).show();
                                    else
                                        Toast.makeText(mContext, "Network Connection Error", Toast.LENGTH_LONG).show();
                                }
                            }.execute();
                        }else{
                            new AttemptLogin_With_File(attachment_image,progressBar, config.URL + server_file, mContext, map, mContext,file_path) {
                                public void setonCallback(JSONObject json) {
                                    super.setonCallback(json);
                                    if (json != null)
                                        Toast.makeText(mContext, json.toString(), Toast.LENGTH_LONG).show();
                                    else
                                        Toast.makeText(mContext, "Network Connection Error", Toast.LENGTH_LONG).show();
                                }
                            }.execute();
                        }

                    }
                    else
                    {
                        Toast.makeText(mContext, "No Network Connection", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(mContext, "Please enter valid text.", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_send_sub_coord, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }

    String selectedImagePath;
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {



        Uri uri=data.getData();

        file_set=1;

        try {
            file_path = config.getPath(this, uri);
            Log.e("onactivityresult","enter1");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        //File file=new File(path);
        attach_text.setText(file_path);
        Log.e("onactivityresult","enter2");
        super.onActivityResult(requestCode, resultCode, data);
    }

}
