package notufy.thapar.com.notufy.Fragments;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;

import notufy.thapar.com.notufy.Activity.logined;
import notufy.thapar.com.notufy.Adapters.HomePagerAdapter;
import notufy.thapar.com.notufy.Adapters.SendHODPagerAdapter;
import notufy.thapar.com.notufy.Beans.SectionsTabsBean;
import notufy.thapar.com.notufy.R;
import notufy.thapar.com.notufy.SlidingTabLayout;
import notufy.thapar.com.notufy.config;


public class HomeFragment extends Fragment {
    private static final String TAG = HomeFragment.class.getSimpleName();
    ViewPager pager;
    View v;
    public  Toolbar mToolBar;
    public static View faketoolbar;
    HomePagerAdapter adapter;
    private int scrolledDistance = 0;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Teachers","Societies","Hostel"};
    int Numboftabs =3;
    FloatingActionsMenu menu;
    int ToolbarHeight=0;
    boolean animationdelay=true;
    LinearLayout collapsablecontainer;
    FrameLayout homecontentmain;
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_home, container, false);
        homecontentmain=(FrameLayout)v.findViewById(R.id.fragment_home_content_main);
        collapsablecontainer=(LinearLayout)v.findViewById(R.id.collapsetoolbar);
        adapter =  new HomePagerAdapter(getActivity().getSupportFragmentManager(),Titles,Numboftabs);
        mToolBar=(Toolbar)v.findViewById(R.id.screen_default_toolbar);
        menu=(FloatingActionsMenu)v.findViewById(R.id.menu);
        //faketoolbar=v.findViewById(R.id.fakeToolbar);
        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) v.findViewById(R.id.fragment_home_view_pager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) v.findViewById(R.id.fragment_home_pager_sliding_tab);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setSelectedIndicatorColors(Color.parseColor("#ffffff"));
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return Color.parseColor("#ffffff");
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);
        loadfloatingButtons();
        mToolBar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                mToolBar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                ((logined)getActivity()).setUpNavigationDrawer(mToolBar);
                ToolbarHeight=mToolBar.getHeight();
            }
        });
        return v;
    }


    private ArrayList<SectionsTabsBean> loadSectionTabs() {
        String[] sectionsTabsTitleArray;
        ArrayList<SectionsTabsBean> sectionTabsListItens = null;

        try {
            sectionsTabsTitleArray = getActivity().getResources().getStringArray(R.array.section);
            sectionTabsListItens = new ArrayList<>();
            for (String sectionsTabsTitle : sectionsTabsTitleArray) {
                sectionTabsListItens.add(new SectionsTabsBean(sectionsTabsTitle));
            }
            return sectionTabsListItens;
        }
        catch (Resources.NotFoundException notFoundExcepetion) {
            Log.e(TAG, "Error Getting The Array", notFoundExcepetion);
        }
        return sectionTabsListItens;
    }
    private void loadfloatingButtons() {
        config conf=new config(getActivity());
        int user_type=config.parseInt(conf.getUsercode());
        if ((user_type & config.STUDENT) != 0) {
            FloatingActionButton add = new FloatingActionButton(logined.mContext);
            add.setTitle("data");
            add.setImageResource(R.drawable.ic_action_settings);
            menu.addButton(add);
        }
        if ((user_type & config.CONVENER) != 0) {
            FloatingActionButton add = new FloatingActionButton(logined.mContext);
            add.setImageResource(R.drawable.ic_action_settings);
            add.setTitle("data");
            menu.addButton(add);
        }
        if ((user_type & config.TEACHER) != 0) {
            FloatingActionButton add = new FloatingActionButton(getActivity());
            add.setImageResource(R.drawable.ic_action_settings);
            add.setTitle("data");
            menu.addButton(add);
        }
        if ((user_type & config.SUBJECT_COORD) != 0) {
            FloatingActionButton add = new FloatingActionButton(getActivity());
            add.setImageResource(R.drawable.ic_action_settings);
            add.setTitle("data");
            menu.addButton(add);
        }
        if ((user_type & config.YEAR_COORD) != 0) {
            FloatingActionButton add = new FloatingActionButton(getActivity());
            add.setImageResource(R.drawable.ic_action_settings);
            add.setTitle("data");
            menu.addButton(add);
        }
        if ((user_type & config.HOD) != 0) {
            FloatingActionButton add = new FloatingActionButton(getActivity());
            add.setImageResource(R.drawable.ic_action_settings);
            add.setTitle("data");
            menu.addButton(add);
        }
        if ((user_type & config.HOSTEL_ADMIN) != 0) {
            FloatingActionButton add = new FloatingActionButton(getActivity());
            add.setImageResource(R.drawable.ic_action_settings);
            add.setTitle("data");
            menu.addButton(add);
        }
        if ((user_type & config.HOSTEL_RESIDENT) != 0) {
            FloatingActionButton add = new FloatingActionButton(getActivity());
            add.setImageResource(R.drawable.ic_action_settings);
            add.setTitle("data");
            menu.addButton(add);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public int getToolbarHeight() {
        return ToolbarHeight;
    }
}