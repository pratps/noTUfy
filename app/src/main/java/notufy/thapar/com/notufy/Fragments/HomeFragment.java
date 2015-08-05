package notufy.thapar.com.notufy.Fragments;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;

import notufy.thapar.com.notufy.Activity.logined;
import notufy.thapar.com.notufy.Adapters.HomePagerAdapter;
import notufy.thapar.com.notufy.Adapters.SendHODPagerAdapter;
import notufy.thapar.com.notufy.Beans.SectionsTabsBean;
import notufy.thapar.com.notufy.R;
import notufy.thapar.com.notufy.SlidingTabLayout;


public class HomeFragment extends Fragment {
    private static final String TAG = HomeFragment.class.getSimpleName();
    ViewPager pager;
    View v;
    public static View faketoolbar;
    HomePagerAdapter adapter;

    SlidingTabLayout tabs;
    Context mContext;

    CharSequence Titles[]={"Teachers","Societies","Hostel"};
    int Numboftabs =3;
    FloatingActionsMenu menu;
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_home, container, false);

        adapter =  new HomePagerAdapter(getActivity().getSupportFragmentManager(),Titles,Numboftabs);
        menu=(FloatingActionsMenu)v.findViewById(R.id.menu);

        faketoolbar=v.findViewById(R.id.fakeToolbar);
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
        FloatingActionButton add=new FloatingActionButton(getActivity());
        add.setTitle("data");
        add.setVisibility(FloatingActionButton.VISIBLE);
        menu.addButton(add);

    }
}