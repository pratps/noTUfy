package notufy.thapar.com.notufy.Activity;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import notufy.thapar.com.notufy.Adapters.GroupSelectAdapter;
import notufy.thapar.com.notufy.Adapters.HODgrpSelectAdapter;
import notufy.thapar.com.notufy.Adapters.SendHODPagerAdapter;
import notufy.thapar.com.notufy.Beans.subject_group_bean;
import notufy.thapar.com.notufy.Fragments.SendHODGrpSelect;
import notufy.thapar.com.notufy.Fragments.SendTeacherGroupSelect;
import notufy.thapar.com.notufy.R;
import notufy.thapar.com.notufy.SlidingTabLayout;
import notufy.thapar.com.notufy.dataBase.dataBase;


public class SendHOD extends ActionBarActivity {

    // Declaring Your View and Variables

    Toolbar toolbar;
    ViewPager pager;
    SendHODPagerAdapter adapter;
    SlidingTabLayout tabs;
    static Context mContext;
    public static ArrayList<subject_group_bean> chosenGroups=new ArrayList<subject_group_bean>();
    public static ArrayList<subject_group_bean> group_list=new ArrayList<subject_group_bean>();
    public static ArrayList<subject_group_bean> chosenYear=new ArrayList<subject_group_bean>();
    public static ArrayList<subject_group_bean> yearlist;

    CharSequence Titles[]={"Home","Events","Final"};
    int Numboftabs =3;

    Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_hod);
        mContext=getApplicationContext();
        dataBase db=new dataBase(mContext);

        yearlist=db.get_extra_year_course();
        mToolBar = (Toolbar) findViewById(R.id.screen_default_toolbar);
        mToolBar.setTitle("bla bla");
        mToolBar.setNavigationIcon(R.drawable.material_ic_drawer_menu_navigation);

        setSupportActionBar(mToolBar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        //
        //insert();

        // Creating The Toolbar and setting it as the Toolbar for the activity


        insert();



        mContext=getApplicationContext();



        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter =  new SendHODPagerAdapter(getSupportFragmentManager(),Titles,Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setHODflag();
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return Color.parseColor("#F26463");
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);







    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_send_hod, menu);
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
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }
    void insert(){
        dataBase db=new dataBase(mContext);
        ArrayList<subject_group_bean> grps=new ArrayList<subject_group_bean>();
        subject_group_bean grp;
        grp=new subject_group_bean();
        grp.setYear("1");
        grp.setCourse("BE");
        grp.setGroup("COE1");
        grp.setSubject_group_code("1BECOE1");
        grps.add(grp);
        grp=new subject_group_bean();
        grp.setYear("3");
        grp.setCourse("BE");
        grp.setGroup("COE1");
        grp.setSubject_group_code("3BECOE1");
        grps.add(grp);
        grp=new subject_group_bean();
        grp.setYear("3");
        grp.setCourse("BE");
        grp.setGroup("COE2");
        grp.setSubject_group_code("3BECOE2");
        grps.add(grp);
        grp=new subject_group_bean();
        grp.setYear("2");
        grp.setCourse("ME");
        grp.setGroup("COE1");
        grp.setSubject_group_code("2MECOE1");
        grps.add(grp); grp=new subject_group_bean();
        grp.setYear("1");
        grp.setCourse("ME");
        grp.setGroup("COE1");
        grp.setSubject_group_code("1MECOE1");
        grps.add(grp); grp=new subject_group_bean();
        grp.setYear("2");
        grp.setCourse("BE");
        grp.setGroup("COE1");
        grp.setSubject_group_code("2BECOE1");
        grps.add(grp); grp=new subject_group_bean();
        grp.setYear("1");
        grp.setCourse("BE");
        grp.setGroup("COE3");
        grp.setSubject_group_code("1BECOE3");
        grps.add(grp); grp=new subject_group_bean();
        grp.setYear("1");
        grp.setCourse("BE");
        grp.setGroup("COE2");
        grp.setSubject_group_code("1BECOE2");
        grps.add(grp);


        db.insert_extra_grp(grps);




    }
    public static void populatetab2(){

        Log.e("inside", "populate");
        dataBase db=new dataBase(mContext);
        ArrayList<String> year=new ArrayList<String>();
        ArrayList<String> course=new ArrayList<String>();
        for(subject_group_bean s:chosenYear){
            year.add(s.getYear());
            course.add(s.getCourse());
        }

        Log.e("b4 func","b4 func");
        group_list=db.get_extra_grp(year,course);
        SendHODGrpSelect.hoDgrpSelectAdapter=new HODgrpSelectAdapter(mContext,group_list);
        SendHODGrpSelect.list.setAdapter(SendHODGrpSelect.hoDgrpSelectAdapter);
        SendHODGrpSelect.hoDgrpSelectAdapter.notifyDataSetChanged();
        Log.e("testing",Integer.toString(chosenGroups.size()));
    }
}
