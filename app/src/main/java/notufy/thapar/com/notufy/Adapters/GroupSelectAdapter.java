package notufy.thapar.com.notufy.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;

import notufy.thapar.com.notufy.Activity.SendTeacher;
import notufy.thapar.com.notufy.Beans.subject_group_bean;
import notufy.thapar.com.notufy.R;

/**
 * Created by prat on 4/2/2015.
 */
public class GroupSelectAdapter extends BaseAdapter {
    Context mContext;
    private LayoutInflater mInflater;
    public ArrayList<subject_group_bean> group_list;
    ColorGenerator generator = ColorGenerator.MATERIAL;
    @Override
    public int getCount() {
        return group_list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public GroupSelectAdapter(ArrayList<subject_group_bean> group_list,Context c)
    {
        this.group_list=group_list;
        this.mContext=c;
        mInflater=(LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);

        Log.e("inside","constructor");
    }

    GradientDrawable dl,dt,dp;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v=null;
        if(group_list.get(position).getFlag()==2)
        {
            v = mInflater.inflate(R.layout.layout_group_header, parent, false);
            TextView sub=(TextView)v.findViewById(R.id.subject_code);
            LinearLayout back=(LinearLayout)v.findViewById(R.id.header_back);

            sub.setText(group_list.get(position).getSubject_code());

        }
        else
        {
            final subject_group_bean item=group_list.get(position);
             v = mInflater.inflate(R.layout.layout_batch_select, parent, false);
             final TextView grp,l,t,p;
            grp=(TextView)v.findViewById(R.id.grp_name);
            t=(TextView)v.findViewById(R.id.t);
            p=(TextView)v.findViewById(R.id.p);
            l=(TextView)v.findViewById(R.id.l);

            final String[] group_code = {"","",""};
            group_code[0]=item.getSubject_group_code()+"L";
            group_code[1]=item.getSubject_group_code()+"T";
            group_code[2]=item.getSubject_group_code()+"P";


            if(item.getLTP().contains("L"))
                l.setVisibility(View.VISIBLE);
            if(item.getLTP().contains("T"))
                t.setVisibility(View.VISIBLE);
            if(item.getLTP().contains("P"))
                p.setVisibility(View.VISIBLE);


            if(!SendTeacher.chosenGroupcodes.contains(group_code[0]))
            {
                l.setBackgroundResource(R.drawable.circle);
                l.setTextColor(Color.parseColor("#37474F"));

            }
            else
            {
                l.setBackgroundResource(R.drawable.selected_circle);
                l.setTextColor(Color.parseColor("#ffffff"));

            }

            if(!SendTeacher.chosenGroupcodes.contains(group_code[1]))
            {
                t.setBackgroundResource(R.drawable.circle);
                t.setTextColor(Color.parseColor("#37474F"));

            }
            else
            {
                t.setBackgroundResource(R.drawable.selected_circle);
                t.setTextColor(Color.parseColor("#ffffff"));

            }

            if(!SendTeacher.chosenGroupcodes.contains(group_code[2]))
            {
                p.setBackgroundResource(R.drawable.circle);
                p.setTextColor(Color.parseColor("#37474F"));

            }
            else
            {
                p.setBackgroundResource(R.drawable.selected_circle);

                p.setTextColor(Color.parseColor("#ffffff"));

            }


            grp.setText(item.getCourse()+" "+item.getYear()+" YEAR "+item.getGroup());
            l.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {


                    if(SendTeacher.chosenGroupcodes.contains(group_code[0]))
                    {
                        l.setBackgroundResource(R.drawable.circle);
                         l.setTextColor(Color.parseColor("#37474F"));
                        SendTeacher.chosenGroupcodes.remove(group_code[0]);
                    }
                    else
                    {
                        l.setBackgroundResource(R.drawable.selected_circle);
                        l.setTextColor(Color.parseColor("#ffffff"));
                        SendTeacher.chosenGroupcodes.add(group_code[0]);
                    }
                    Log.e("Array2",SendTeacher.chosenGroupcodes.toString());



                }
            });
            t.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {

                    if(SendTeacher.chosenGroupcodes.contains(group_code[1]))
                    {
                        t.setBackgroundResource(R.drawable.circle);
                        t.setTextColor(Color.parseColor("#37474F"));
                        SendTeacher.chosenGroupcodes.remove(group_code[1]);
                    }
                    else
                    {
                        t.setBackgroundResource(R.drawable.selected_circle);
                        t.setTextColor(Color.parseColor("#ffffff"));
                        SendTeacher.chosenGroupcodes.add(group_code[1]);
                    }


                    Log.e("Array2",SendTeacher.chosenGroupcodes.toString());
                }
            });
            p.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {


                    if(SendTeacher.chosenGroupcodes.contains(group_code[2]))
                    {
                        p.setBackgroundResource(R.drawable.circle);
                        p.setTextColor(Color.parseColor("#37474F"));
                        SendTeacher.chosenGroupcodes.remove(group_code[2]);
                    }
                    else
                    {
                        p.setBackgroundResource(R.drawable.selected_circle);

                        p.setTextColor(Color.parseColor("#ffffff"));
                        SendTeacher.chosenGroupcodes.add(group_code[2]);
                    }

                    Log.e("Array2", SendTeacher.chosenGroupcodes.toString());

                }
            });
             //   v=convertView;
        }


        return v;
    }

}
