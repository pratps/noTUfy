package notufy.thapar.com.notufy.Adapters;



import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.Future;

import notufy.thapar.com.notufy.Activity.logined;
import notufy.thapar.com.notufy.Asynctasks.AttemptLogin;
import notufy.thapar.com.notufy.Beans.hostel_message;
import notufy.thapar.com.notufy.Beans.teacher_message;
import notufy.thapar.com.notufy.R;
import notufy.thapar.com.notufy.config;
import notufy.thapar.com.notufy.dataBase.dataBase;

public class TeacherTab_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static List<teacher_message> mListItemsCard=null;
    int previousPosition=0;

    private static String file_folder="/noTUfy/Files";
    private static String extDir=Environment.getExternalStorageDirectory().getPath().toString();
    private static String server_file_download="/download.php";
    ColorGenerator generator = ColorGenerator.MATERIAL;
    Boolean no_more_message_flag=false;

    config conf;
    Context mContext;
    public TeacherTab_Adapter(List<teacher_message> listItemsCard,Context context) {
        mListItemsCard = listItemsCard;
        String root=extDir+file_folder;
        mContext=context;
        File mydir=new File(root);
        mydir.mkdirs();
        conf=new config(mContext);
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
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_teacher, parent, false));
        else {
            return new More(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_more, parent, false));
        }
    }

    public void onBindViewHolder(final RecyclerView.ViewHolder main_holder, int position) {
        if(main_holder instanceof ViewHolder) {
            ViewHolder holder=(ViewHolder)main_holder;
            holder.attachment.setVisibility(View.VISIBLE);
            final teacher_message itemCardView = mListItemsCard.get(position);
            Log.e("onBind", itemCardView.getSender_user_name() + itemCardView.getInfo() + itemCardView.getFile_name().length());
            holder.itemView.setTag(itemCardView);

            holder.teacher_name.setText(itemCardView.getSender_user_name());
            holder.datetime.setText(itemCardView.getDatetime());
            holder.info.setText(itemCardView.getInfo());
            char c = Character.toUpperCase(itemCardView.getSender_user_name().charAt(0));
            //int y,x=(int)c-65;
            if (itemCardView.color == -1) {
                itemCardView.color = generator.getColor(itemCardView.getInfo() + itemCardView.getSender_user_name());
            }

            //Log.e("info",itemCardView.getInfo());
            TextDrawable draw = TextDrawable.builder()
                    .beginConfig()
                    .bold()
                    .endConfig()
                    .buildRound(Character.toString(c), itemCardView.color);

            holder.teacher_icon.setImageDrawable(draw);


            holder.item = itemCardView;
            holder.type = "teacher";
            holder.position = position;
            holder.message_id = itemCardView.getMessage_id();
            if (itemCardView.getFile_name().length() == 0) {
                holder.attachment.setVisibility(View.GONE);

            }

            holder.progressBar.setProgress(itemCardView.getProgress());
            //Log.e("info",itemCardView.getInfo());
            holder.file_name.setText(itemCardView.getFile_name());
            holder.file_size.setText(itemCardView.getFile_size());



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
                        Toast.makeText(mContext, "No more messages.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        if (previousPosition < position)
            animateObject(main_holder, true, position);

        previousPosition = position;
    }

    void loadmoremessages(More holder){
        dataBase db=new dataBase(mContext);
        ArrayList<teacher_message> list;
        if(mListItemsCard.size()>0)
        list=db.get_teacher_messages(mListItemsCard.get(mListItemsCard.size() - 1).getMessage_id());
        else{
            list=db.get_teacher_messages(System.currentTimeMillis());
        }
        if(list.size()==0){
            if(conf.isNetworkConnectionAvailable())
            loadmoremessagesfromserver(holder);
            else
                Toast.makeText(mContext,"No network connection",Toast.LENGTH_SHORT).show();

        }else{
            addtorecycler(list);
        }
    }

    public void addtorecycler(ArrayList<teacher_message> new_messages)
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

        final dataBase db=new dataBase(mContext);
        String load_more_server_file="/load_more.php";
        SharedPreferences r = conf.getPersonalInfoPreference();
        String user_code = r.getString("user_code", "");
        String password = r.getString("password", "");
        map.put("user_code",user_code );
        map.put("password", password);
        map.put("msg_type","0");
        map.put("last_seen", Long.toString(mListItemsCard.get(mListItemsCard.size() - 1).getMessage_id()));
        //Log.e("last_seen",Long.toString(mListItemsCard.get(mListItemsCard.size() - 1).getMessage_id()));
        holder.more.setText("Loading...");
        new AttemptLogin(false, "", config.URL + load_more_server_file, mContext, map, mContext) {
            @Override
            public void setonCallback(JSONObject json) {
                super.setonCallback(json);
                if (json != null) {
                    ArrayList<teacher_message> new_m = new ArrayList<teacher_message>();

                    if (json != null) {
                        Log.e("json", json.toString());
                        int code;
                        try {
                            code = json.getInt("code");
                            Log.e("code", Integer.toString(code));
                            switch (code) {
                                case config.AUTHENTICATION_FAILURE:
                                    Toast.makeText(mContext, "Authentication error.Please contact admin.", Toast.LENGTH_SHORT).show();
                                    break;
                                case config.SUCCESS:
                                    new_m = conf.enumerate_teacher_messages(json, db,"teacher_messages");
                                    break;
                                default:
                                    Toast.makeText(mContext, "Network Connection error.", Toast.LENGTH_SHORT).show();
                                    //mSwipeRefreshLayout.setRefreshing(false);
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(mContext, "Network Connection error.", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(mContext, "Network Connection error.", Toast.LENGTH_SHORT).show();
                        //mSwipeRefreshLayout.setRefreshing(false);
                    }

                }

            }



        }.execute();

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

    public int getItemCount() {
        return mListItemsCard.size()+1;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView teacher_name,datetime,info,file_name,file_size;
        public ImageView teacher_icon;
        public RelativeLayout attachment;
        public ProgressBar progressBar;
        Future<File> downloading;
        teacher_message item;
        String type;
        int position;

        Long message_id;
        public ViewHolder(View itemView) {
            super(itemView);
            attachment=(RelativeLayout)itemView.findViewById(R.id.attach);
            teacher_name=(TextView)itemView.findViewById(R.id.teacher_name);
            datetime=(TextView)itemView.findViewById(R.id.datetime);
            info=(TextView)itemView.findViewById(R.id.info);
            file_name=(TextView)itemView.findViewById(R.id.file_name);
            file_size=(TextView)itemView.findViewById(R.id.file_size);
            teacher_icon=(ImageView)itemView.findViewById(R.id.teacher_icon);
            progressBar=(ProgressBar)itemView.findViewById(R.id.progressBar);

            attachment.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.attach){
                if(item.getFile_path().length()==0) {
                    setIsRecyclable(false);
                    downloadFile();

                }
                else{
                    File file=new File(item.getFile_path());
                    if(file.exists()) {
                        loadFile(item.getFile_path());
                    }
                    else{
                        downloadFile();
                    }
                }
            }
        }
        private void loadFile(String file_path){
            MimeTypeMap myMime = MimeTypeMap.getSingleton();
            Intent newIntent = new Intent(Intent.ACTION_VIEW);
            String mimeType = myMime.getMimeTypeFromExtension(fileExt(file_path).substring(1));
            newIntent.setDataAndType(Uri.fromFile(new File(file_path)),mimeType);
            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                logined.mContext.startActivity(newIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(logined.mContext, "Cannot open this type of file", Toast.LENGTH_LONG).show();
            }
        }
        private String fileExt(String url) {
            if (url.indexOf("?") > -1) {
                url = url.substring(0, url.indexOf("?"));
            }
            if (url.lastIndexOf(".") == -1) {
                return null;
            } else {
                String ext = url.substring(url.lastIndexOf("."));
                if (ext.indexOf("%") > -1) {
                    ext = ext.substring(0, ext.indexOf("%"));
                }
                if (ext.indexOf("/") > -1) {
                    ext = ext.substring(0, ext.indexOf("/"));
                }
                return ext.toLowerCase();

            }
        }
        void downloadFile(){

            setIsRecyclable(false);
            final dataBase db=new dataBase(logined.mContext);
            config conf=new config(logined.mContext);
            if(conf.isNetworkConnectionAvailable()) {
                progressBar.setProgress(0);
                downloading = Ion
                        .with(logined.mContext)
                        .load(config.URL+server_file_download)
                        .progressBar(progressBar)
                        .progress(new ProgressCallback() {
                            public void onProgress(long downloaded, long total) {

                                Log.e("File download", "" + downloaded + " / " + total);
                                int x=(int)(downloaded/total*100);
                                mListItemsCard.get(position).setProgress(x);
                            }
                        })
                        .setBodyParameter("code", item.getSender_user_code())
                        .setBodyParameter("type", type)
                        .setBodyParameter("file", item.getFile_link())
                        .setBodyParameter("name", item.getFile_name())
                        .write(new File(extDir + file_folder + "/" + item.getFile_name()))
                        .setCallback(new FutureCallback<File>() {
                            @Override
                            public void onCompleted(Exception e, File result) {
                                if (e != null) {
                                    Toast.makeText(logined.mContext, e.toString(), Toast.LENGTH_LONG).show();
                                    return;
                                }
                                Toast.makeText(logined.mContext, "File download complete.File at " + result.toString(), Toast.LENGTH_LONG).show();
                                Log.d("location", result.toString());
                                mListItemsCard.get(position).setFile_path(result.toString());
                                db.update_file_path(item.getMessage_id(), result.toString());
                                setIsRecyclable(true);
                            }
                        });
            }else{
                Toast.makeText(logined.mContext,"No network connection.",Toast.LENGTH_SHORT).show();
            }
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
