package notufy.thapar.com.notufy.Fragments;

import android.content.SharedPreferences;
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
import notufy.thapar.com.notufy.Adapters.HostelTab_Adapter;
import notufy.thapar.com.notufy.Asynctasks.AttemptLogin;
import notufy.thapar.com.notufy.Beans.hostel_message;
import notufy.thapar.com.notufy.R;
import notufy.thapar.com.notufy.config;
import notufy.thapar.com.notufy.dataBase.dataBase;


public class hostel_messages extends Fragment implements ObservableScrollViewCallbacks,SwipeRefreshLayout.OnRefreshListener{
    private ObservableRecyclerView mRecyclerView;
    private int mBaseTranslationY;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    HostelTab_Adapter messageadapter;
    String server_file="/hostel_sync.php";
    Toolbar mToolBar;
    LinearLayout mHeaderView;

    public static hostel_messages newInstance() {
        hostel_messages fragment = new hostel_messages();
        return fragment;
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_hostel_messages, container, false);
        mRecyclerView = (ObservableRecyclerView) v.findViewById(R.id.messagelist);
        mSwipeRefreshLayout=(SwipeRefreshLayout)v.findViewById(R.id.swipehostelmessage);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setScrollViewCallbacks(this);
        mSwipeRefreshLayout.setProgressViewOffset(false,0,80);
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#F44336"), Color.parseColor("#29B6F6"),Color.parseColor("#4CAF50"));
        loadInfoView();
        return v;
    }
    private void loadInfoView() {
        List<hostel_message> mListItemsCard=new ArrayList<hostel_message>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(logined.mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);

        //insert();
        Number x=-1;
        mListItemsCard=new dataBase(getActivity()).get_hostel_messages(x.longValue());
        View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.recycler_header, null);
        messageadapter=new HostelTab_Adapter(mListItemsCard,headerView);
        mRecyclerView.setAdapter(messageadapter);
    }
    public void insert()
    {
        hostel_message m;
        dataBase db=new dataBase(getActivity());
        m=new hostel_message();
        m.setHeading("Message1");
        m.setInfo("alksdmasdlkadlmaldalkdmaslkmdlamdlamldl");
        m.setDatetime("12-32o1");
        m.setMessage_id(124);
        db.insert_hostel_message(m);
        m=new hostel_message();
        m.setHeading("Message2");
        m.setInfo("alksdmasdlkadlmaldalkdmaslkmdlamdlamldl");
        m.setDatetime("12-32o1");
        m.setMessage_id(12234);
        db.insert_hostel_message(m);
        m=new hostel_message();
        m.setHeading("Message3");
        m.setInfo("alksdmasdlkadlmaldalkdmaslkmdlamdlamldl");
        m.setDatetime("12-32o1");
        m.setMessage_id(12324);
        db.insert_hostel_message(m);
        m=new hostel_message();
        m.setHeading("Message4");
        m.setInfo("alksdmasdlkadlmaldalkdmaslkmdlamdlamldl");
        m.setDatetime("12-323o1");
        m.setMessage_id(1244);
        db.insert_hostel_message(m);

    }

    @Override
    public void onRefresh() {



        final dataBase db=new dataBase(getActivity());
        final config conf=new config(getActivity());
        if(conf.isNetworkConnectionAvailable()) {

            Long last_sync=conf.getHostelmessagelastsync();
            ArrayList<Long> message_list = db.get_hostel_message_id(last_sync);
            //int last_sync=0;
            String message_ids = "";

            for (Long m : message_list) {
                Log.e("message id inside loop",Long.toString(m));
                if (m > last_sync) {

                    message_ids += Long.toString(m) + " ";
                }
            }
            SharedPreferences r = conf.getPersonalInfoPreference();
            String user_code = r.getString("user_code", "1234");
            String password = r.getString("password", "pass");

            message_ids = message_ids.trim();
            Log.e("message_ids", message_ids);
            Log.e("last_sync",Long.toString(last_sync));
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("user_code", user_code);
            map.put("password", password);
            map.put("message_ids",message_ids);
            map.put("last_sync", Long.toString(last_sync));
            new AttemptLogin(false, "", config.URL + server_file, getActivity(), map, getActivity()) {

                public void setonCallback(JSONObject json) {
                    //enumerate(json);
                    super.setonCallback(json);
                    ArrayList<hostel_message> new_m = null;
                    if (json != null) {
                        Log.e("json", json.toString());
                        int code;
                        try {
                            code = json.getInt("code");
                            //Log.e("code",Integer.toString(code));
                            switch (code) {
                                case 1:
                                    new_m=conf.enumerate_hostel_messages(json, db);
                                    break;
                                default:
                                    Toast.makeText(getActivity(), "Network Connection error.1", Toast.LENGTH_SHORT).show();
                                    mSwipeRefreshLayout.setRefreshing(false);
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(),"Network Connection error.2",Toast.LENGTH_SHORT).show();
                            mSwipeRefreshLayout.setRefreshing(false);
                            code = 2;
                        }

                        mSwipeRefreshLayout.setRefreshing(false);
                        if(new_m.size()!=0) {
                            addtorecycler(new_m);
                            conf.setHostelmessagelastsync();

                        }
                        else
                        {
                            Toast.makeText(getActivity(),"No new messages.",Toast.LENGTH_SHORT).show();
                            conf.setHostelmessagelastsync();
                        }


                    }
                    else
                    {
                        Toast.makeText(getActivity(),"Network Connection error.1",Toast.LENGTH_SHORT).show();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                }
            }.execute();


        }
        else
        {
            Log.e("Teacher","no network");
            Toast.makeText(getActivity(),"No Network Connection.2",Toast.LENGTH_SHORT).show();
            mSwipeRefreshLayout.setRefreshing(false);
        }

    }
    public void addtorecycler(ArrayList<hostel_message> new_messages)
    {
        int i;

        for(i=0;i<new_messages.size();i++) {
            HostelTab_Adapter.mListItemsCard.add(0, new_messages.get(i));
            messageadapter.notifyItemInserted(0);
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

