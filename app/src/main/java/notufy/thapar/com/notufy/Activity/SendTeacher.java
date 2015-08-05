package notufy.thapar.com.notufy.Activity;

import android.content.Context;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import java.util.ArrayList;

import notufy.thapar.com.notufy.Adapters.GroupSelectAdapter;
import notufy.thapar.com.notufy.Adapters.SendTeacherPagerAdapter;
import notufy.thapar.com.notufy.Beans.subject_group_bean;
import notufy.thapar.com.notufy.Fragments.SendTeacherGroupSelect;
import notufy.thapar.com.notufy.R;
import notufy.thapar.com.notufy.SlidingTabLayoutTeacher;

import notufy.thapar.com.notufy.dataBase.dataBase;


public class SendTeacher extends ActionBarActivity {

    // Declaring Your View and Variables

    Toolbar mToolBar;
    ViewPager pager;
    SendTeacherPagerAdapter adapter;
    SlidingTabLayoutTeacher tabs;
    static Context mContext;
    CharSequence Titles[]={"Home","Events","Final"};
    int Numboftabs =3;
    public static ArrayList<String> chosenSubjects=new ArrayList<String>();
    public static ArrayList<subject_group_bean> group_list=new ArrayList<subject_group_bean>();
    public static ArrayList<String> chosenGroupcodes=new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_teacher);
        chosenSubjects=new ArrayList<String>();
        chosenGroupcodes=new ArrayList<String>();
        group_list=new ArrayList<subject_group_bean>();

        // Creating The Toolbar and setting it as the Toolbar for the activity


       //insert();



        mContext=getApplicationContext();
        mToolBar = (Toolbar) findViewById(R.id.screen_default_toolbar);
        mToolBar.setTitle("bla bla");
        mToolBar.setNavigationIcon(R.drawable.material_ic_drawer_menu_navigation);

        setSupportActionBar(mToolBar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);


        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter =  new SendTeacherPagerAdapter(getSupportFragmentManager(),Titles,Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayoutTeacher) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayoutTeacher.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.white);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);







    }


    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_send_teacher, menu);
        return true;
    }
*/


    void insert()
    {
        dataBase db=new dataBase(getApplication());
       // db.deleteTeacher_sub_grp_map();
       // db.createTeacher_sub_grp_map();
        ArrayList<subject_group_bean> subs=new ArrayList<subject_group_bean>();
        subject_group_bean s;
        s=new subject_group_bean();
        s.setYear("1");
        s.setSubject_code("UCS101");
        s.setGroup("COE3");
        s.setCourse("BE");
        s.setLTP("LP");
        s.setSubject_group_code("1BECOE3_UCS101");
        subs.add(s);
        s=new subject_group_bean();
        s.setYear("2");
        s.setSubject_code("UCS201");
        s.setGroup("COE8");
        s.setCourse("BE");
        s.setLTP("LT");
        s.setSubject_group_code("2BECOE8_UCS201");
        subs.add(s);
        s=new subject_group_bean();
        s.setYear("1");
        s.setSubject_code("UTA701");
        s.setGroup("COE2");
        s.setCourse("BE");
        s.setLTP("LP");
        s.setSubject_group_code("1BECOE2_UTA701");
        subs.add(s);
        s=new subject_group_bean();
        s.setYear("2");
        s.setSubject_code("UCS101");
        s.setGroup("COE7");
        s.setCourse("BE");
        s.setLTP("LP");
        s.setSubject_group_code("2BECOE3_UCS101");
        subs.add(s);
        s=new subject_group_bean();
        s.setYear("1");
        s.setSubject_code("UCS601");
        s.setGroup("COE3");
        s.setCourse("BE");
        s.setLTP("LP");
        s.setSubject_group_code("1BECOE3_UCS601");
        subs.add(s);
        s=new subject_group_bean();


        db.insert_teacher_sub_grp_map(subs);
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

    public static void populateTab2()
    {
        Log.e("inside","populate");
        dataBase db=new dataBase(mContext);
        SendTeacher.group_list=db.get_teacher_grps(chosenSubjects);
        SendTeacherGroupSelect.groupadapter=new GroupSelectAdapter(SendTeacher.group_list,mContext);
        SendTeacherGroupSelect.grouplistview.setAdapter(SendTeacherGroupSelect.groupadapter);
        SendTeacherGroupSelect.groupadapter.notifyDataSetChanged();
        Log.e("testing","adapter set");
    }
}