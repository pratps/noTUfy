package notufy.thapar.com.notufy.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import notufy.thapar.com.notufy.Fragments.SendTeacherGroupSelect;
import notufy.thapar.com.notufy.Fragments.SendTeacherSendMessage;
import notufy.thapar.com.notufy.Fragments.SendTeacherSubSelect;

/**
 * Created by hp1 on 21-01-2015.
 */
public class SendTeacherPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created


    // Build a Constructor and assign the passed Values to appropriate values in the class
    public SendTeacherPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

        if(position == 0) // if the position is 0 we are returning the First tab
        {
            SendTeacherSubSelect sendTeacherSubSelect = new SendTeacherSubSelect();
            return sendTeacherSubSelect;
        }
        else if (position == 1)      // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
        {
            SendTeacherGroupSelect sendTeacherGroupSelect = new SendTeacherGroupSelect();
            return sendTeacherGroupSelect;
        }
        else
        {
            SendTeacherSendMessage sendTeacherSendMessage = new SendTeacherSendMessage();
            return sendTeacherSendMessage;
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