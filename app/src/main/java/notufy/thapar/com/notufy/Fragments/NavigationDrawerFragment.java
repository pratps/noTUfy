package notufy.thapar.com.notufy.Fragments;


import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import notufy.thapar.com.notufy.Activity.SendHOD;
import notufy.thapar.com.notufy.Activity.SendSubCoord;
import notufy.thapar.com.notufy.Activity.SendTeacher;
import notufy.thapar.com.notufy.Activity.logined;
import notufy.thapar.com.notufy.Activity.settings;
import notufy.thapar.com.notufy.Activity.society_select;
import notufy.thapar.com.notufy.Adapters.navigation_adapter;
import notufy.thapar.com.notufy.R;

public class NavigationDrawerFragment extends Fragment {
	private FragmentManager fragmentmanager;
	private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
	private int set=0; 
	ListView list;
	private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
	private boolean mFromSavedInstanceState;
	private boolean mUserLearnedDrawer;
    private RelativeLayout relative,aim,contact,message;
	private View containerView,layout; 
	private ActionBarDrawerToggle mDrawerToggle;
	TextView text;
	private DrawerLayout mDrawerLayout;
	public static int active_fragment;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mUserLearnedDrawer=Boolean.valueOf(readFromPreference(getActivity(), PREF_USER_LEARNED_DRAWER, "false"));
		if(savedInstanceState!=null)
		{
			mFromSavedInstanceState=true;
		}
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
		layout=inflater.inflate(R.layout.fragment_navigation_drawer,container,false);
		list=(ListView)layout.findViewById(R.id.nav_list);
		navigation_adapter nav=new navigation_adapter(getActivity());
		list.setAdapter(nav);
		list.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				mDrawerLayout.closeDrawers();
                Log.e("position",Integer.toString(position));
                Intent i;
                switch(position)
                {
                    case 0: i=new Intent(getActivity(),society_select.class);
                        break;
                    case 1:i=new Intent(getActivity(), SendTeacher.class);
                        break;
                    case 2:i=new Intent(getActivity(), SendSubCoord.class);
                        break;
                    case 3:i=new Intent(getActivity(), SendHOD.class);
                        break;
                    case 4:i=new Intent(getActivity(),settings.class);
                        break;
                    default:i=new Intent(getActivity(),logined.class);

                }
                startActivity(i);
			}
		});
		return layout;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}
	
	
	
	public void setUp(FragmentManager fragment,int fragmentid,DrawerLayout drawerLayout, Toolbar toolbar) {
	// TODO Auto-generated method stub
		this.fragmentmanager=fragment;
		containerView=getActivity().findViewById(fragmentid);
		mDrawerLayout=drawerLayout;
		mDrawerToggle=new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close){
		@Override
			public void onDrawerOpened(View drawerView) {
			// TODO Auto-generated method stub
				super.onDrawerOpened(drawerView);
				if(!mUserLearnedDrawer)
				{
					mUserLearnedDrawer=true;
					saveToPreference(getActivity(), PREF_USER_LEARNED_DRAWER, Boolean.toString(mUserLearnedDrawer));
				}
				getActivity().invalidateOptionsMenu();
			}
			@Override
			public void onDrawerClosed(View drawerView) {

				super.onDrawerClosed(drawerView);
				getActivity().invalidateOptionsMenu();
			}
		};
		if(!mUserLearnedDrawer&&!mFromSavedInstanceState)
		{
			mDrawerLayout.openDrawer(containerView);
		}
		mDrawerLayout.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mDrawerToggle.syncState();
			}
		});
		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	public void saveToPreference(Context context,String preferenceName,String preferenceValue){
	
		SharedPreferences sp=context.getSharedPreferences("drawerpref", Context.MODE_PRIVATE);
		Editor editor=sp.edit();
		editor.putString(preferenceName, preferenceValue);
		editor.apply();
	}
	public String readFromPreference(Context context, String preferenceName,String defValue){
		SharedPreferences sp=context.getSharedPreferences("drawerpref", Context.MODE_PRIVATE);
		return sp.getString(preferenceName, defValue);
	}
	void change_color(int layout_id,View layout,int id){
		relative=(RelativeLayout)layout.findViewById(layout_id);	
		relative.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffd630")));
		text=(TextView)layout.findViewById(id);
		text.setTextColor(Color.parseColor("#50514f"));
		relative.setClickable(false);
	}
}
