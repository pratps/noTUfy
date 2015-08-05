package notufy.thapar.com.notufy.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import notufy.thapar.com.notufy.Adapters.SocietyTab_Adapter;
import notufy.thapar.com.notufy.Adapters.society_select_adapter;
import notufy.thapar.com.notufy.Beans.society;
import notufy.thapar.com.notufy.R;
import notufy.thapar.com.notufy.config;
import notufy.thapar.com.notufy.dataBase.dataBase;

public class society_select extends ActionBarActivity {

    private RecyclerView mRecyclerView;
    private Toolbar mToolBar;
    public static ArrayList<String> society_preference;
    public static Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_society_select);
        loadComponents();
        initializeToolBar();
        mRecyclerView=(RecyclerView)findViewById(R.id.societylist);
        String s;

        if(savedInstanceState==null)
        {
            s = "LITSOC MSC1 MSC2";
            society_preference = new ArrayList<String>(Arrays.asList(s.split(" ")));

        }
        createString(society_preference);
        StaggeredGridLayoutManager staggeredGridLayoutManager;
        if(getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT) {
            staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            society_select_adapter.image_width=getSharedPreferences("dimensions", Context.MODE_PRIVATE).getInt("width",1000)/2;
        }else {
            staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
            society_select_adapter.image_width=getSharedPreferences("dimensions", Context.MODE_PRIVATE).getInt("height",1000)/3;
        }
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);



        dataBase db=new dataBase(getApplication());

       //insert();
        ArrayList<society> socs=db.get_societies();
        for(society soc:socs)
        {
            if(society_preference.contains(soc.getSociety_code()))
            {
                soc.setFlag(1);
            }
        }
        society_select_adapter societySelectAdapter=new society_select_adapter(socs,this);
        mRecyclerView.setAdapter(societySelectAdapter);
    }

    private void loadComponents() {
        mToolBar = (Toolbar) findViewById(R.id.screen_default_toolbar);
        mContext = getApplicationContext();
    }


    private void loadInfoView() {
        if (mToolBar != null) {
            setSupportActionBar(mToolBar);
            getSupportActionBar().setTitle("noTUfy");
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    protected void initializeToolBar() {
        if (mToolBar != null) {
            mToolBar.setTitle("noTUfy");
            //mToolBar.setNavigationIcon(R.drawable.material_ic_drawer_menu_navigation);
            //getSupportActionBar().setDisplayHomeAsUpEnabled(getDisplayHomeAsUp());
            //getSupportActionBar().setHomeButtonEnabled(getHomeButtonEnabled());
        }
    }
/*
    @SuppressLint("NewApi")
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString("soc",createString(society_preference));
        Log.e("inside onsave","sad");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String s;
        if(savedInstanceState!=null)
        {
            s=savedInstanceState.getString("soc");
            if(s==null)
            Log.e("restored string","is null");
            else
                Log.e("restored string",s);

            //s = "LITSOC MSC1 MSC2";
            society_preference = new ArrayList<String>(Arrays.asList(s.split(" ")));
        }
    }
*/
    public String createString(ArrayList<String> strlist)
    {
        String str = "";
        Log.e("string array",strlist.toString());
        for (String s : strlist)
        {
            str += s + " ";
        }
        Log.e("string",str.trim());
        return str.trim();
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
    public void insert()
    {
        dataBase db=new dataBase(getApplicationContext());
        db.deleteAllsocieties();
        config conf=new config(getApplicationContext());
        String j="{\"societies\":[{\"society_code\":\"LITSOC\",\"society_name\":\"Literary Society\",\"society_icon\":\"litsoc.jpg\",\"society_info\":\"This society oragnises literary events\",\"society_colour\":\"#009688\"},{\"society_code\":\"CCS1\",\"society_name\":\"Creative Computing Society\",\"society_icon\":\"ccs.png\",\"society_info\":\"This society organises computer events\",\"society_colour\":\"#003456\"},{\"society_code\":\"MSC1\",\"society_name\":\"Microsoft Student Chapter\",\"society_icon\":\"msc1.jpg\",\"society_info\":\"Microsoft supported society to support student activities\",\"society_colour\":\"#AD1457\"},{\"society_code\":\"AISEC\",\"society_name\":\"AISEC THAPAR UNIVERSITY\",\"society_icon\":\"aisec.jpg\",\"society_info\":\"International community to support students and organise events\",\"society_colour\":\"#0097A7\"},{\"society_code\":\"FAPS\",\"society_name\":\"Fine Arts and Photography Society\",\"society_icon\":\"faps.jpg\",\"society_info\":\"A society that brings out the hidden talents inside of you\",\"society_colour\":\"#E64A19\"},{\"society_code\":\"NOX1\",\"society_name\":\"NOX Thapar University\",\"society_icon\":\"nox1.jpg\",\"society_info\":\"This society is for those students who are interested in dance\",\"society_colour\":\"#455A64\"}]}";
        try {
            JSONObject json=new JSONObject(j);
            conf.enumerate_all_societies(json,db);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
