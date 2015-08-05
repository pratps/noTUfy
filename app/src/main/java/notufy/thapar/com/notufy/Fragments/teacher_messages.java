package notufy.thapar.com.notufy.Fragments;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import notufy.thapar.com.notufy.Activity.logined;
import notufy.thapar.com.notufy.Adapters.TeacherTab_Adapter;
import notufy.thapar.com.notufy.Asynctasks.AttemptLogin;
import notufy.thapar.com.notufy.Beans.teacher_message;
import notufy.thapar.com.notufy.R;
import notufy.thapar.com.notufy.config;
import notufy.thapar.com.notufy.dataBase.dataBase;

public class teacher_messages extends Fragment implements OnRefreshListener{
    private RecyclerView mRecyclerView;
    private List<teacher_message> mListItemsCard;
    String server_file="/teacher_sync.php";
    private static final int HIDE_THRESHOLD = 20;
    private int scrolledDistance = 0;
    private boolean controlsVisible = true;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    TeacherTab_Adapter messageadapter;
    public static teacher_messages newInstance() {
        teacher_messages fragment = new teacher_messages();
        return fragment;
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_logined_messages, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.messagelist);
        mSwipeRefreshLayout=(SwipeRefreshLayout)v.findViewById(R.id.swipeteachermessage);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#F44336"), Color.parseColor("#29B6F6"),Color.parseColor("#4CAF50"));
        loadInfoView();
        return v;
    }

    private void loadInfoView() {
        mListItemsCard=new ArrayList<teacher_message>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(logined.mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);

        Number x=-1;
        mListItemsCard=new dataBase(getActivity()).get_teacher_messages(x.longValue());

        messageadapter=new TeacherTab_Adapter(mListItemsCard,getActivity());
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                //show views if first item is first visible position and views are hidden

                    if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
                        logined.mToolBar.animate().translationY(-logined.mToolBar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
                        logined.main_content.animate().translationY(-logined.mToolBar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
                        controlsVisible = false;
                        scrolledDistance = 0;
                    } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
                        logined.mToolBar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
                        logined.main_content.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
                        controlsVisible = true;
                        scrolledDistance = 0;
                    }


                if((controlsVisible && dy>0) || (!controlsVisible && dy<0)) {
                    scrolledDistance += dy;
                }
            }
        });
        mRecyclerView.setAdapter(messageadapter);
    }


    @Override
    public void onRefresh() {
        final dataBase db=new dataBase(getActivity());
        final config conf=new config(getActivity());
        if(conf.isNetworkConnectionAvailable()) {
            Number x=0;
            Long last_sync=conf.getTeachermessagelastsync();
            ArrayList<Long> message_id_list = db.get_teacher_message_id(last_sync);

            int last_sync2=0;
            String message_ids = "";
            for (Long m : message_id_list) {
                    message_ids += Long.toString(m) + " ";
            }
            SharedPreferences r = conf.getPersonalInfoPreference();
            String user_code = r.getString("user_code", "");
            String password = r.getString("password", "");

            message_ids = message_ids.trim();
            Log.e("message_ids", message_ids);
            Log.e("last_sync",Long.toString(last_sync));
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("user_code", user_code);
            map.put("password", password);
            map.put("message_ids",message_ids);
            map.put("last_sync", Long.toString(last_sync2));
            new AttemptLogin(false, "", config.URL + server_file, getActivity(), map, getActivity()) {

                public void setonCallback(JSONObject json) {
                    //enumerate(json);
                    super.setonCallback(json);
                    ArrayList<teacher_message> new_m = new ArrayList<teacher_message>();
                    if (json != null) {
                        Log.e("json", json.toString());
                        int code;
                        try {
                            code = json.getInt("code");
                            switch (code) {
                                case 1:
                                    new_m=conf.enumerate_teacher_messages(json, db,"teacher_messages");
                                    conf.setTeachermessagelastsync();
                                    break;
                                default:
                                    Toast.makeText(getActivity(),"Network Connection error.",Toast.LENGTH_SHORT).show();
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


                        }
                        else
                        {
                            Toast.makeText(getActivity(),"No new messages.",Toast.LENGTH_SHORT).show();

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
    public void addtorecycler(ArrayList<teacher_message> new_messages)
    {
        int i;

        for(i=0;i<new_messages.size();i++) {
            TeacherTab_Adapter.mListItemsCard.add(i, new_messages.get(i));
            messageadapter.notifyItemInserted(i);
        }
        mRecyclerView.scrollToPosition(0);
    }

}
