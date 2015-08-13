package notufy.thapar.com.notufy.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import notufy.thapar.com.notufy.Activity.logined;
import notufy.thapar.com.notufy.Adapters.SocietyTab_Adapter;
import notufy.thapar.com.notufy.Asynctasks.AttemptLogin;
import notufy.thapar.com.notufy.Beans.society_message;
import notufy.thapar.com.notufy.config;
import notufy.thapar.com.notufy.dataBase.dataBase;
import notufy.thapar.com.notufy.R;


public class society_messages extends Fragment implements ObservableScrollViewCallbacks,SwipeRefreshLayout.OnRefreshListener {
    private ObservableRecyclerView mRecyclerView;
    Toolbar mToolBar;
    LinearLayout mHeaderView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int mBaseTranslationY;
    SocietyTab_Adapter societyadapter;
    String server_file="/society_sync.php";
    public static society_messages newInstance() {
        society_messages fragment = new society_messages();
        return fragment;
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_society, container, false);
        mRecyclerView = (ObservableRecyclerView) v.findViewById(R.id.societylist);
        mRecyclerView.setScrollViewCallbacks(this);
        mSwipeRefreshLayout=(SwipeRefreshLayout)v.findViewById(R.id.swipesocietymessage);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setProgressViewOffset(false,0,80);
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#F44336"), Color.parseColor("#29B6F6"),Color.parseColor("#4CAF50"));
        loadInfoView();
        return v;
    }
    private void loadInfoView() {
        List<society_message> mListItemsCard = new ArrayList<society_message>();
        //insert();
        LinearLayoutManager layoutManager = new LinearLayoutManager(logined.mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        Number x=-1;
        mListItemsCard=new dataBase(getActivity()).get_society_messages(x.longValue());
        if(getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT)
            SocietyTab_Adapter.image_width=getActivity().getSharedPreferences("dimensions", Context.MODE_PRIVATE).getInt("width",50);
        else
            SocietyTab_Adapter.image_width=getActivity().getSharedPreferences("dimensions", Context.MODE_PRIVATE).getInt("height",50);
        View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.recycler_header, null);
        societyadapter=new SocietyTab_Adapter(mListItemsCard,getActivity(),headerView);
        mRecyclerView.setAdapter(societyadapter);
    }

    @Override
    public void onRefresh() {


        final dataBase db=new dataBase(getActivity());
        final config conf=new config(getActivity());
        if(conf.isNetworkConnectionAvailable()) {
            Long last_sync = conf.getSocietymessagelastsync();
            ArrayList<Long> message_list = db.get_society_message_id(last_sync);

            //int last_sync=0;
            String message_ids = "";
            for (Long m : message_list) {
                if (m > last_sync) {
                    message_ids += Long.toString(m) + " ";
                }
            }
            SharedPreferences r = conf.getPersonalInfoPreference();
            String user_code = r.getString("user_code", "");
            String password = r.getString("password", "");

            message_ids = message_ids.trim();
            Log.e("message_ids", message_ids);
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("user_code", user_code);
            map.put("password",password);
            map.put("message_ids", message_ids);
            map.put("last_sync", Long.toString(last_sync));
            new AttemptLogin(false, "", config.URL + server_file, getActivity(), map, getActivity()) {

                public void setonCallback(JSONObject json) {
                    //enumerate(json);
                    super.setonCallback(json);
                    ArrayList<society_message> new_m = new ArrayList<society_message>();
                    if (json != null) {
                        Log.e("json", json.toString());
                        int code;
                        try {
                            code = json.getInt("code");
                            switch (code) {
                                case 1:
                                    new_m=conf.enumerate_society_messages(json, db);
                                    break;
                                default:
                                    Toast.makeText(getActivity(), "Network Connection error.", Toast.LENGTH_SHORT).show();
                                    mSwipeRefreshLayout.setRefreshing(false);
                                    break;


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(),"Network Connection error.",Toast.LENGTH_SHORT).show();
                            mSwipeRefreshLayout.setRefreshing(false);
                            code = 2;
                        }


                        mSwipeRefreshLayout.setRefreshing(false);
                        if(new_m.size()!=0) {
                            addtorecycler(new_m);
                            conf.setSocietymessagelastsync();

                        }
                        else
                        {
                            Toast.makeText(getActivity(),"No new messages.",Toast.LENGTH_SHORT).show();
                            conf.setSocietymessagelastsync();
                        }
                        //mListItemsCard = db.get_teacher_messages();
                        //mRecyclerView.setAdapter(new TeacherTab_Adapter(mListItemsCard));
                    }
                    else
                    {
                        Toast.makeText(getActivity(),"Network Connection error.",Toast.LENGTH_SHORT).show();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                }
            }.execute();


        }
        else
        {
            Log.e("Teacher","no network");
            Toast.makeText(getActivity(),"No Network Connection.",Toast.LENGTH_SHORT).show();
            mSwipeRefreshLayout.setRefreshing(false);
        }

    }
    public void addtorecycler(ArrayList<society_message> new_messages)
    {
        int i;


        for(i=0;i<new_messages.size();i++) {
            SocietyTab_Adapter.mListItemsCard.add(i, new_messages.get(i));
            societyadapter.notifyItemInserted(0);
        }
        mRecyclerView.scrollToPosition(0);


    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        mToolBar=((logined)getActivity()).getHomeFragment().mToolBar;
        mHeaderView=((logined)getActivity()).getHomeFragment().collapsablecontainer;
        if (dragging) {
            int toolbarHeight = mToolBar.getHeight();
            float currentHeaderTranslationY = ViewHelper.getTranslationY(mHeaderView);
            if (firstScroll) {
                if (-toolbarHeight < currentHeaderTranslationY) {
                    mBaseTranslationY = scrollY;
                }
            }
            float headerTranslationY = ScrollUtils.getFloat(-(scrollY - mBaseTranslationY), -toolbarHeight, 0);
            ViewPropertyAnimator.animate(mHeaderView).cancel();
            ViewHelper.setTranslationY(mHeaderView, headerTranslationY);
        }
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        mBaseTranslationY = 0;
        mHeaderView=((logined)getActivity()).getHomeFragment().collapsablecontainer;
        mToolBar=((logined)getActivity()).getHomeFragment().mToolBar;
        int toolbarHeight = mToolBar.getHeight();
        int scrollY = mRecyclerView.getCurrentScrollY();
        if (scrollState == ScrollState.DOWN) {
            showToolbar();
        } else if (scrollState == ScrollState.UP) {
            if (toolbarHeight <= scrollY) {
                hideToolbar();
            } else {
                showToolbar();
            }
        } else {
            // Toolbar is moving but doesn't know which to move:
            // you can change this to hideToolbar()
            showToolbar();
        }
    }

    private void showToolbar() {
        float headerTranslationY = ViewHelper.getTranslationY(mHeaderView);
        if (headerTranslationY != 0) {
            ViewPropertyAnimator.animate(mHeaderView).cancel();
            ViewPropertyAnimator.animate(mHeaderView).translationY(0).setDuration(200).start();
        }
    }

    private void hideToolbar() {
        float headerTranslationY = ViewHelper.getTranslationY(mHeaderView);
        int toolbarHeight = mToolBar.getHeight();
        if (headerTranslationY != -toolbarHeight) {
            ViewPropertyAnimator.animate(mHeaderView).cancel();
            ViewPropertyAnimator.animate(mHeaderView).translationY(-toolbarHeight).setDuration(200).start();
        }
    }
}
