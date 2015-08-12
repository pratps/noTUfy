package notufy.thapar.com.notufy.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import notufy.thapar.com.notufy.Fragments.hostel_messages;
import notufy.thapar.com.notufy.Fragments.society_messages;
import notufy.thapar.com.notufy.Fragments.teacher_messages;

public class HomePagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
    society_messages sm;
    teacher_messages tm;
    hostel_messages hm;
    // Build a Constructor and assign the passed Values to appropriate values in the class
    public HomePagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

        switch(position){
            case 0:
                if(tm==null) {
                    tm = new teacher_messages();
                }
                return tm;
            case 1:
                if(sm==null) {
                    sm = new society_messages();
                }
                return sm;
            default:
                if(hm==null){
                    hm=new hostel_messages();
                }
                return hm;
        }


    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}