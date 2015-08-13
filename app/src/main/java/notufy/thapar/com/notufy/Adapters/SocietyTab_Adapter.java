package notufy.thapar.com.notufy.Adapters;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

import android.os.Environment;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import notufy.thapar.com.notufy.Activity.logined;
import notufy.thapar.com.notufy.Asynctasks.AttemptLogin;
import notufy.thapar.com.notufy.Beans.society_message;
import notufy.thapar.com.notufy.Beans.teacher_message;
import notufy.thapar.com.notufy.R;
import notufy.thapar.com.notufy.config;
import notufy.thapar.com.notufy.dataBase.dataBase;

public class SocietyTab_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static List<society_message> mListItemsCard;
    private static final int VIEW_TYPE_HEADER = 2;
    private static final int VIEW_TYPE_ITEM = 0;
    private static final int LOAD_MORE=1;
    private String folder="/noTUfy/Event_images";
    private String icon_folder="/noTUfy/Society_icons";
    private String extDir=Environment.getExternalStorageDirectory().getPath().toString();
    Context context;
    config conf;

    Boolean no_more_message_flag=false;
    public static int image_width,max_image_width,icon_width;

    private int downloadFlag=0,previousPosition=0;


    View mHeader;
    public SocietyTab_Adapter(List<society_message> listItemsCard,Context context,View HeaderView) {
        this.mListItemsCard = listItemsCard;
        this.context=context;
        conf=new config(logined.mContext);
        mHeader=HeaderView;
        int padding_in_dp = 60;  // 6 dps
        final float scale = context.getResources().getDisplayMetrics().density;
        SocietyTab_Adapter.icon_width= (int) (padding_in_dp * scale + 0.5f);
        SocietyTab_Adapter.max_image_width=context.getSharedPreferences("dimensions", Context.MODE_PRIVATE).getInt("height",1000);


    }

    public int getItemViewType(int position) {
        if(position==mListItemsCard.size()+1) {
            return LOAD_MORE;
        }
        else if(position==0){
            return VIEW_TYPE_HEADER;
        }
        else
            return VIEW_TYPE_ITEM;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==0)
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_society, parent, false));
        else if(viewType==VIEW_TYPE_HEADER){
            return new HeaderViewHolder(mHeader);
        }
        else {
            return new More(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_more, parent, false));
        }
    }





    public void onBindViewHolder(final RecyclerView.ViewHolder main_holder, final int position) {


        if(main_holder instanceof ViewHolder) {
            final ViewHolder holder=(ViewHolder)main_holder;
            holder.event_image.setVisibility(View.GONE);
            holder.download_button.setVisibility(View.GONE);
            holder.date_rel.setVisibility(View.VISIBLE);
            holder.event_time.setVisibility(View.VISIBLE);
            holder.event_venue.setVisibility(View.VISIBLE);
            final society_message itemCardView = mListItemsCard.get(position-1);
            holder.itemView.setTag(itemCardView);
            Log.e("onbind", itemCardView.getHeading());

            holder.society_name.setText(itemCardView.getSociety_name());
            holder.heading.setText(itemCardView.getHeading());
            holder.info.setText(itemCardView.getInfo());
            String[] datetime = itemCardView.getEvent_datetime().split(" ");
            switch (datetime.length) {

                case 3:
                    holder.event_date_number.setText(datetime[0]);
                    holder.event_date_month.setText(datetime[1]);
                    holder.event_date_day.setText(datetime[2]);
                    break;

                case 4:
                    holder.event_date_number.setText(datetime[0]);
                    holder.event_date_month.setText(datetime[1]);
                    holder.event_date_day.setText(datetime[2]);
                    holder.event_time.setText(datetime[3]);
                    break;
                default:
                    holder.date_rel.setVisibility(View.GONE);
                    holder.event_time.setVisibility(View.GONE);

            }
            if (itemCardView.getEvent_venue().length() != 0) {

                holder.event_venue.setText(itemCardView.getEvent_venue());
            } else {
                holder.event_venue.setVisibility(View.GONE);
            }
            try {
                int color = Color.parseColor(itemCardView.getSociety_color());
                setcolor(holder, color);
            } catch (Exception E) {
                int color = Color.parseColor("#000000");
                setcolor(holder, color);
            }


            if (!itemCardView.getImage_url().isEmpty())
                loadImage(holder, itemCardView, position);
            else {
                holder.event_image.setVisibility(View.GONE);
                holder.download_button.setVisibility(View.GONE);
            }

            loadSocietyicon(holder, itemCardView, position);



            holder.download_button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    holder.download_button.setVisibility(View.GONE);
                    holder.event_image.setVisibility(View.VISIBLE);
                    holder.setIsRecyclable(false);
                    downloadImage(holder, itemCardView, position);
                }
            });
        }
        else if(main_holder instanceof HeaderViewHolder){

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
        previousPosition=position;

   }

    void setcolor(ViewHolder holder,int color){
        holder.society_heading.setBackgroundColor(color);
        holder.heading.setTextColor(color);
        holder.society_heading.setBackgroundColor(color);
        holder.event_date_number.setTextColor(color);
        holder.download_image.setBackgroundColor(color);
        holder.download_text.setTextColor(color);
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
        ObjectAnimator animatorY=ObjectAnimator.ofFloat(holder.itemView,"translationY",y,0);
        animatorY.setDuration(durationY);
        animatorX.setDuration(durationX);
        //animatorY.start();
        animset.playTogether(animatorX,animatorY);
        animset.start();
    }



    public int getItemCount() {
        return mListItemsCard.size()+1;
    }



    void loadmoremessages(More holder){
        dataBase db=new dataBase(logined.mContext);
        ArrayList<society_message> list;
        if(mListItemsCard.size()>0)
        list=db.get_society_messages(mListItemsCard.get(mListItemsCard.size() - 1).getMessage_id());
        else
            list=db.get_society_messages(System.currentTimeMillis());
        if(list.size()==0){
            if(conf.isNetworkConnectionAvailable())
                loadmoremessagesfromserver(holder);
            else
                Toast.makeText(logined.mContext,"No network connection",Toast.LENGTH_SHORT).show();

        }else{
            addtorecycler(list);
        }
    }

    public void addtorecycler(ArrayList<society_message> new_messages)
    {
        int i;

        for(i=0;i<new_messages.size();i++) {
            mListItemsCard.add(new_messages.get(i));
            notifyItemInserted(mListItemsCard.size()-1);
        }
        // mRecyclerView.scrollToPosition(0);
    }


    void loadmoremessagesfromserver(final More holder) {
        HashMap<String, String> map = new HashMap<String, String>();

        final dataBase db=new dataBase(logined.mContext);
        String load_more_server_file="/load_more.php";
        SharedPreferences r = conf.getPersonalInfoPreference();
        String user_code = r.getString("user_code", "");
        String password = r.getString("password", "");
        map.put("user_code",user_code );
        map.put("msg_type","1");
        map.put("password", password);
        map.put("last_seen", Long.toString(mListItemsCard.get(mListItemsCard.size() - 1).getMessage_id()));
        //Log.e("last_seen",Long.toString(mListItemsCard.get(mListItemsCard.size() - 1).getMessage_id()));
        holder.more.setText("Loading...");
        new AttemptLogin(false, "", config.URL + load_more_server_file, logined.mContext, map, logined.mContext) {
            @Override
            public void setonCallback(JSONObject json) {
                super.setonCallback(json);
                if (json != null) {
                    ArrayList<society_message> new_m = new ArrayList<society_message>();

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
                                    new_m = conf.enumerate_society_messages(json, db);
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
    public class ViewHolder extends RecyclerView.ViewHolder  {


        TextView society_name,heading,info,event_date_number,event_date_month,event_date_day,event_venue,event_time,download_text;
        ImageView society_icon,event_image,download_image;
        RelativeLayout society_heading,download_button,date_rel;
        public ViewHolder(View itemView) {
            super(itemView);
            society_name=(TextView)itemView.findViewById(R.id.society_name);
            heading=(TextView)itemView.findViewById(R.id.heading);
            info=(TextView)itemView.findViewById(R.id.info);
            event_date_number=(TextView)itemView.findViewById(R.id.event_date_number);
            event_date_month=(TextView)itemView.findViewById(R.id.event_date_month);
            event_date_day=(TextView)itemView.findViewById(R.id.event_date_day);
            event_venue=(TextView)itemView.findViewById(R.id.event_venue);
            event_time=(TextView)itemView.findViewById(R.id.event_time);
            society_icon=(ImageView)itemView.findViewById(R.id.society_icon);
            event_image=(ImageView)itemView.findViewById(R.id.event_image);
            society_heading=(RelativeLayout)itemView.findViewById(R.id.society_heading);

            date_rel=(RelativeLayout)itemView.findViewById(R.id.event_datetime);
            download_text=(TextView)itemView.findViewById(R.id.download_text);
            download_image=(ImageView)itemView.findViewById(R.id.download_image);
            download_button=(RelativeLayout)itemView.findViewById(R.id.download_button);

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

    private void loadImage(final ViewHolder holder,final society_message item, final int position)
    {
        String url;
        if(item.getImage_path().length()==0)
        {
            Log.e("Image not downlaoded","");
            if(downloadFlag==1)
            {
                holder.download_button.setVisibility(View.GONE);
                holder.event_image.setVisibility(View.VISIBLE);
                downloadImage(holder, item, position);
            }
            else
            {
                holder.event_image.setVisibility(View.GONE);
                holder.download_button.setVisibility(View.VISIBLE);
            }

        }
        else
        {
            holder.download_button.setVisibility(View.GONE);
            holder.event_image.setVisibility(View.VISIBLE);
            Log.e("image path",extDir+item.getImage_path());
            File file=new File(extDir+item.getImage_path());
            if(file.exists())
            {
                Log.e("Image available","");

                Picasso.
                        with(context).
                        load(file).
                        resize(SocietyTab_Adapter.image_width,0).
                        placeholder(R.drawable.placeholder).
                        into(holder.event_image);
            }
            else
            {
                Log.e("Image unavaiable","");

                if(downloadFlag==1)
                {
                    holder.download_button.setVisibility(View.GONE);
                    holder.event_image.setVisibility(View.VISIBLE);
                    downloadImage(holder, item, position);
                }
                else
                {
                    holder.download_button.setVisibility(View.VISIBLE);
                    holder.event_image.setVisibility(View.GONE);
                }
            }
        }
    }


    private void downloadImage(final ViewHolder holder,final society_message item, final int position)
    {
        Target target=new Target(){


            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                savetoext(bitmap,position);
                holder.event_image.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        Log.e("path",config.DOWNLOAD_URL+"/uploads/society/"+item.getSociety_code()+"/"+item.getImage_url());
        Picasso.
                with(context).
                load(config.DOWNLOAD_URL+"/uploads/society/"+item.getSociety_code()+"/"+item.getImage_url()).
                resize(SocietyTab_Adapter.max_image_width,0).
                placeholder(R.drawable.placeholder).
                skipMemoryCache().
                into(target);
        holder.event_image.setTag(target);

    }
    private void savetoext(Bitmap bitmap,int position)
    {
        String root=extDir+folder;
        File mydir=new File(root);
        mydir.mkdirs();
        Random generator = new Random();
        int n = 1000000;
        n = generator.nextInt(n);
        String fname = n +".png";
        File file=new File(mydir,fname);
        if (file.exists ()) file.delete ();
        try
        {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            mListItemsCard.get(position).setImage_path(folder + "/" + fname);
            dataBase db=new dataBase(context);
            db.update_society_message_image_path(mListItemsCard.get(position).getMessage_id(),mListItemsCard.get(position).getImage_path());
            Log.e("file","saved");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }






    //load society icon functions
    private void loadSocietyicon(final ViewHolder holder, final society_message item, final int position)
    {
        Bitmap bm;
        String society_code=item.getSociety_code();
     //if(societies.contains(society_code))
       // {
         //   bm=society_icons.get(societies.indexOf(society_code));
           // holder.society_icon.setImageBitmap(bm);
        //}
       // else
       // {
            if(item.getSociety_icon_path().length()==0)
            {
                Log.e("icon Imagenotdownlaoded","");
                downloadSocietyicon(holder, item, position);
            }
            else
            {
                Log.e("icon image path",item.getSociety_icon_path());
                File file=new File(extDir+item.getSociety_icon_path());
                if(file.exists())
                {
                    Log.e("icon Image available",extDir+item.getSociety_icon_path());

                    Picasso.
                            with(context).
                            load(file).
                            resize(SocietyTab_Adapter.icon_width,0).
                            placeholder(R.drawable.placeholder).
                            into(holder.society_icon);
                }
                else
                {
                    Log.e("Image unavaiable","");

                    downloadSocietyicon(holder,item,position);
                }
            }
       }




    private void downloadSocietyicon(final ViewHolder holder,final society_message item, final int position)
    {
        Log.e("icon inside download","");
        Target target=new Target(){


            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                savetoextSocietyicon(bitmap,position);
                holder.society_icon.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        Picasso.
                with(context).
                load(config.DOWNLOAD_URL+"/images/society/"+item.getSociety_icon_link()).
                resize(SocietyTab_Adapter.max_image_width,0).
                skipMemoryCache().
                into(target);


    }

    private void savetoextSocietyicon(Bitmap bitmap,int position)
    {
        Log.e("icon inside save","");
        String root=extDir+icon_folder;
        File mydir=new File(root);
        mydir.mkdirs();
        Random generator = new Random();
        int n = 1000000;
        n = generator.nextInt(n);
        String fname = n +".png";
        File file=new File(mydir,fname);
        if (file.exists ()) file.delete ();
        try
        {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            mListItemsCard.get(position).setSociety_icon_path(icon_folder + "/" + fname);

            dataBase db=new dataBase(context);
            db.update_society_icon_path(mListItemsCard.get(position).getSociety_code(), mListItemsCard.get(position).getSociety_icon_path());
            Log.e("icon file","saved");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View view) {
            super(view);
        }
    }
}
