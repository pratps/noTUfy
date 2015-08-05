package notufy.thapar.com.notufy.Activity;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import notufy.thapar.com.notufy.Fragments.HomeFragment;
import notufy.thapar.com.notufy.Fragments.NavigationDrawerFragment;
import notufy.thapar.com.notufy.R;
import notufy.thapar.com.notufy.config;
import notufy.thapar.com.notufy.dataBase.dataBase;

public class logined extends ActionBarActivity {
    public static Toolbar mToolBar;
    public static RelativeLayout main_content;
    String LOGIN_URL="";
    static public Context mContext;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceIdentifier());
        //loadComponents();
        //loadInfoView();
        //initializeToolBar();

        mToolBar=(Toolbar)findViewById(R.id.screen_default_toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mContext=this;

        main_content=(RelativeLayout)findViewById(R.id.main_content);

        NavigationDrawerFragment drawerFragment=(NavigationDrawerFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(getSupportFragmentManager(),R.id.fragment_navigation_drawer,(DrawerLayout)findViewById(R.id.drawer_layout),mToolBar);


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_content, HomeFragment.newInstance(),"fragment_home").commit();
        }



        dataBase db=new dataBase(getApplication());
        //loadfloatingButtons();
    }

    protected int getLayoutResourceIdentifier() {
        return R.layout.activity_logined_;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.logout:
                getApplication().deleteDatabase("noTUfy_db");
                new config(getApplicationContext()).getPersonalInfoPreference().edit().clear().commit();
                Intent i=new Intent(logined.this,MainActivity.class);
                startActivity(i);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        AlertDialog.Builder build=new AlertDialog.Builder(logined.this);
        build.setTitle("Do You Want To Quit ?");
        build.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        build.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        build.show();
    }
}
