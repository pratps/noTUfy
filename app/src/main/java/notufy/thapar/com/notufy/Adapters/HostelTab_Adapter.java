package notufy.thapar.com.notufy.Adapters;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.util.ColorGenerator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import notufy.thapar.com.notufy.Activity.logined;
import notufy.thapar.com.notufy.Asynctasks.AttemptLogin;
import notufy.thapar.com.notufy.Beans.hostel_message;
import notufy.thapar.com.notufy.Beans.society_message;
import notufy.thapar.com.notufy.R;
import notufy.thapar.com.notufy.config;
import notufy.thapar.com.notufy.dataBase.dataBase;

/**
 * Created by prat on 3/25/2015.
 */
public class HostelTab_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static List<hostel_message> mListItemsCard;
    int previousPosition=0;
    ColorGenerator generator = ColorGenerator.MATERIAL;
    Boolean no_more_message_flag=false;
    config conf;

    public HostelTab_Adapter(List<hostel_message> listItemsCard) {
        mListItemsCard = listItemsCard;
        conf=new config(logined.mContext);
    }

    public int getItemViewType(int position) {
        if(position==mListItemsCard.size()) {

            return 1;
        }
        else
            return 0;
    }



    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==0)
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_hostel, parent, false));
        else {
            return new More(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_more, parent, false));
        }
    }

    public void onBindViewHolder(final RecyclerView.ViewHolder main_holder, int position) {
        if(main_holder instanceof ViewHolder) {
            ViewHolder holder=(ViewHolder)main_holder;
            final hostel_message itemCardView = mListItemsCard.get(position);
            holder.itemView.setTag(itemCardView);
            holder.heading.setText(itemCardView.getHeading());
            holder.datetime.setText(itemCardView.getDatetime());
            holder.info.setText(itemCardView.getInfo());
            holder.heading.setTextColor(generator.getColor(itemCardView.getHeading() + itemCardView.getInfo()));
        }
        else{
            final More holder=(More)main_holder;
            if(no_more_message_flag)
            {
                holder.more.setText("No more messages.");
            }
            holder.main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!no_more_message_flag) {
                        loadmoremessages(holder);
                    }
                    else
                    {
                        Toast.makeText(logined.mContext, "No more messages.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        if(previousPosition<position)
            animateObject(main_holder,true,position);
        else
            animateObject(main_holder,false,position);
        previousPosition=position;
    }

    public int getItemCount() {
        return mListItemsCard.size()+1;
    }

    void animateObject(RecyclerView.ViewHolder holder,Boolean scrolldown,int position)
    {
        int x,y,ty=300,tx=0,durationX=500,durationY=500;
        if(position%2==0)
            x=tx*-1;
        else
            x=tx;
        if(scrolldown)
            y=ty;
        else
            y=ty*-1;
        AnimatorSet animset=new AnimatorSet();
        ObjectAnimator animatorX=ObjectAnimator.ofFloat(holder.itemView,"translationX",x,0);
        ObjectAnimator animatorY=ObjectAnimator.ofFloat(holder.itemView,"translationY",scrolldown?300:-300,0);
        animatorY.setDuration(durationY);
        animatorX.setDuration(durationX);
        //animatorY.start();
        animset.playTogether(animatorX,animatorY);
        animset.start();
    }


    void loadmoremessages(More holder){
        dataBase db=new dataBase(logined.mContext);
        ArrayList<hostel_message> list;
        if(mListItemsCard.size()>0)
            list=db.get_hostel_messages(mListItemsCard.get(mListItemsCard.size() - 1).getMessage_id());
        else
            list=db.get_hostel_messages(System.currentTimeMillis());
        if(list.size()==0){
            if(conf.isNetworkConnectionAvailable())
                loadmoremessagesfromserver(holder);
            else
                Toast.makeText(logined.mContext,"No network connection",Toast.LENGTH_SHORT).show();

        }else{
            addtorecycler(list);
        }
    }


    void loadmoremessagesfromserver(final More holder) {
        HashMap<String, String> map = new HashMap<String, String>();

        final dataBase db=new dataBase(logined.mContext);
        String load_more_server_file="/load_more.php";
        SharedPreferences r = conf.getPersonalInfoPreference();
        String user_code = r.getString("user_code", "");
        String password = r.getString("password", "");
        map.put("user_code",user_code );
        map.put("password", password);
        map.put("msg_type","2");

        map.put("last_seen", Long.toString(mListItemsCard.get(mListItemsCard.size() - 1).getMessage_id()));
        //Log.e("last_seen",Long.toString(mListItemsCard.get(mListItemsCard.size() - 1).getMessage_id()));
        holder.more.setText("Loading...");
        new AttemptLogin(false, "", config.URL + load_more_server_file, logined.mContext, map, logined.mContext) {
            @Override
            public void setonCallback(JSONObject json) {
                super.setonCallback(json);
                if (json != null) {
                    ArrayList<hostel_message> new_m = new ArrayList<hostel_message>();

                    if (json != null) {
                        Log.e("json", json.toString());
                        int code;
                        try {
                            code = json.getInt("code");
                            Log.e("code", Integer.toString(code));
                            switch (code) {
                                case config.AUTHENTICATION_FAILURE:
                                    Toast.makeText(logined.mContext, "Authentication error.Please contact admin.", Toast.LENGTH_SHORT).show();
                                    break;
                                case config.SUCCESS:
                                    new_m = conf.enumerate_hostel_messages(json, db);
                                    break;
                                default:
                                    Toast.makeText(logined.mContext, "Network Connection error.", Toast.LENGTH_SHORT).show();
                                    //mSwipeRefreshLayout.setRefreshing(false);
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(logined.mContext, "Network Connection error.", Toast.LENGTH_SHORT).show();
                            //mSwipeRefreshLayout.setRefreshing(false);
                            code = 2;
                        }


                        //mSwipeRefreshLayout.setRefreshing(false);
                        if (new_m.size() != 0) {
                            addtorecycler(new_m);
                            conf.setHostelmessagelastsync();
                            holder.more.setText("More");
                        } else {
                            holder.more.setText("No more messages.");
                            no_more_message_flag=true;
                            //notifyItemRemoved(mListItemsCard.size());
                        }
                        //mListItemsCard = db.get_teacher_messages();
                        //mRecyclerView.setAdapter(new TeacherTab_Adapter(mListItemsCard));
                    } else {
                        Toast.makeText(logined.mContext, "Network Connection error.", Toast.LENGTH_SHORT).show();
                        //mSwipeRefreshLayout.setRefreshing(false);
                    }

                }

            }



        }.execute();

    }

    public void addtorecycler(ArrayList<hostel_message> new_messages)
    {
        int i;

        for(i=0;i<new_messages.size();i++) {
            mListItemsCard.add(new_messages.get(i));
            notifyItemInserted(mListItemsCard.size()-1);
        }
        // mRecyclerView.scrollToPosition(0);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {

        public TextView heading,info,datetime;
        public ViewHolder(View itemView) {
            super(itemView);
            heading=(TextView)itemView.findViewById(R.id.hostel_heading);
            info=(TextView)itemView.findViewById(R.id.hostel_info);
            datetime=(TextView)itemView.findViewById(R.id.hostel_datetime);
        }
    }

    public static class More extends RecyclerView.ViewHolder{
        RelativeLayout main;
        TextView more;
        public More(View itemView){
            super(itemView);
            main=(RelativeLayout)itemView.findViewById(R.id.main);
            more=(TextView)itemView.findViewById(R.id.more);
        }
    }


}
