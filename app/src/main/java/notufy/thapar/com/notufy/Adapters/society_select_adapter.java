package notufy.thapar.com.notufy.Adapters;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import notufy.thapar.com.notufy.Activity.society_select;
import notufy.thapar.com.notufy.Beans.hostel_message;
import notufy.thapar.com.notufy.Beans.society;
import notufy.thapar.com.notufy.Beans.society_message;
import notufy.thapar.com.notufy.R;
import notufy.thapar.com.notufy.config;
import notufy.thapar.com.notufy.dataBase.dataBase;

/**
 * Created by prat on 3/25/2015.
 */
public class society_select_adapter extends RecyclerView.Adapter<society_select_adapter.ViewHolder> {
    private final List<society> mListItemsCard;

    private String folder="/noTUfy/Society_icons";
    private String extDir= Environment.getExternalStorageDirectory().getPath().toString();
    int previousPosition=0;
    public static int image_width,max_image_width;

    Context context;
    public society_select_adapter(List<society> listItemsCard,Context context) {
        this.mListItemsCard = listItemsCard;
        this.context=context;
        max_image_width=context.getSharedPreferences("dimensions", Context.MODE_PRIVATE).getInt("width",1000);
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.society_bean, parent, false));
    }

    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final society itemCardView = mListItemsCard.get(position);

        holder.itemView.setTag(itemCardView);
        holder.society_name.setText(itemCardView.getSociety_name());
        //if(position==0)
        //holder.society_icon.setImageBitmap(itemCardView.getIcon_bitmap());

        if(itemCardView.getFlag()==1)
        {
            holder.society_select.setVisibility(View.VISIBLE);

        }
        holder.society_name.setTextColor(Color.parseColor(itemCardView.getSociety_color()));

        holder.society_main.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(itemCardView.getFlag()==0)
                {
                    holder.society_select.setVisibility(View.VISIBLE);
                    itemCardView.setFlag(1);
                    society_select.society_preference.add(itemCardView.getSociety_code());
                    Log.e("string",society_select.society_preference.toString());
                }
                else
                {
                    holder.society_select.setVisibility(View.GONE);
                    itemCardView.setFlag(0);
                    society_select.society_preference.remove(itemCardView.getSociety_code());
                }
            }
        });



                loadImage(holder, itemCardView, position);




        if(previousPosition<position)
            animateObject(holder,true,position);
        else
            animateObject(holder,false,position);
        previousPosition=position;


        holder.info_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog d=new Dialog(context);
                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                d.setContentView(R.layout.dialog_society_info);
                d.setCancelable(true);
                TextView society_info,society_name;
                society_info=(TextView)d.findViewById(R.id.society_info);
                society_name=(TextView)d.findViewById(R.id.society_name);
                ImageView society_icon=(ImageView)d.findViewById(R.id.society_icon);
                File file=new File(extDir+itemCardView.getSociety_icon_path());
                Picasso.with(society_icon.getContext()).invalidate(file);
                Picasso.
                        with(society_icon.getContext()).
                        load(file).
                        resize(SocietyTab_Adapter.max_image_width,0).
                        skipMemoryCache().
                        into(society_icon);
                society_info.setText(itemCardView.getSociety_info());
                society_name.setText(itemCardView.getSociety_name());
                society_name.setBackgroundColor(Color.parseColor(itemCardView.getSociety_color()));
                d.show();
            }
        });

    }


    private void loadImage(final ViewHolder holder,final society item, final int position)
    {
        String url;
        Log.e("path b4 check", item.getSociety_icon_path());
        if(item.getSociety_icon_path().length()==0)
        {
            Log.e("Image not downlaoded","");
            downloadImage(holder,item,position);
        }
        else
        {
            Log.e("image path",item.getSociety_icon_path());
            File file=new File(extDir+item.getSociety_icon_path());
            if(file.exists())
            {
                Log.e("Image available","");

                Picasso.
                        with(holder.society_icon.getContext()).
                        load(file).
                        resize(SocietyTab_Adapter.image_width,0).
                        into(holder.society_icon);
            }
            else
            {
                Log.e("Image unavaiable","");

                downloadImage(holder,item,position);
            }
        }
    }
    void animateObject(ViewHolder holder,Boolean scrolldown,int position)
    {
        int x,y,ty=0,tx=300,durationX=500,durationY=1000;
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



    private void downloadImage(final ViewHolder holder,final society item, final int position)
    {
        Log.e("inside download","");
        Target target=new Target(){


            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                savetoext(bitmap,position);
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
                with(holder.society_icon.getContext()).
                load(config.DOWNLOAD_URL+"/images/society/"+item.getSociety_icon_link()).
                placeholder(R.drawable.placeholder).
                resize(SocietyTab_Adapter.max_image_width,0).
                skipMemoryCache().
                into(target);
        holder.society_icon.setTag(target);

    }
    private void savetoext(Bitmap bitmap,int position)
    {
        Log.e("inside save","");
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
            mListItemsCard.get(position).setSociety_icon_path(folder + "/" + fname);

            dataBase db=new dataBase(context);
            db.update_society_icon_path(mListItemsCard.get(position).getSociety_code(), mListItemsCard.get(position).getSociety_icon_path());
            Log.e("file","saved");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public int getItemCount() {
        return mListItemsCard.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder  {

        public TextView society_name;
        public ImageView society_icon;
        public ImageButton info_button;
        public RelativeLayout society_main,society_select;
        public ViewHolder(View itemView) {
            super(itemView);
            society_name=(TextView)itemView.findViewById(R.id.society_name);
            society_icon=(ImageView)itemView.findViewById(R.id.society_icon);
            society_main=(RelativeLayout)itemView.findViewById(R.id.society_main);
            society_select=(RelativeLayout)itemView.findViewById(R.id.society_select);
            info_button=(ImageButton)itemView.findViewById(R.id.info_button);
        }
    }

}
