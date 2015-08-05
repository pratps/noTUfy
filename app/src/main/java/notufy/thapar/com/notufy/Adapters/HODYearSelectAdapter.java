package notufy.thapar.com.notufy.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import notufy.thapar.com.notufy.Activity.SendHOD;

import notufy.thapar.com.notufy.Beans.subject_group_bean;
import notufy.thapar.com.notufy.R;

/**
 * Created by prat on 5/5/2015.
 */
public class HODYearSelectAdapter extends BaseAdapter {

    Context c;



    public HODYearSelectAdapter(Context context) {
        this.c = context;

    }

    @Override
    public int getCount() {
        return SendHOD.yearlist.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return SendHOD.yearlist.get(position);
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = null;
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) c.getSystemService(c.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.layout_subject_select, parent, false);
        } else {
            v = convertView;

        }


        subject_group_bean grp=SendHOD.yearlist.get(position);
        final String item = grp.getCourse()+" "+grp.getYear()+" year";
        final ImageView checkbox = (ImageView) v.findViewById(R.id.checkbox);
        final TextView sub_name = (TextView) v.findViewById(R.id.subj_name);


        sub_name.setText(item);
        final RelativeLayout r1 = (RelativeLayout) v.findViewById(R.id.lay1);
        final RelativeLayout r2 = (RelativeLayout) v.findViewById(R.id.lay2);

        if(SendHOD.yearlist.get(position).getFlag()==1)
        {
            checkbox.setImageResource(R.drawable.ic_action_accept);
            sub_name.setTextColor(Color.parseColor("#ffffff"));
            r1.setBackgroundColor(Color.parseColor("#009688"));
            SendHOD.yearlist.get(position).setFlag(1);

        }
        else
        {
            checkbox.setImageResource(R.drawable.abc_btn_switch_to_on_mtrl_00001);
            r1.setBackgroundColor(Color.parseColor("#d6d7d7"));
            sub_name.setTextColor(Color.parseColor("#37474f"));
            SendHOD.yearlist.get(position).setFlag(0);
        }

        r1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SendHOD.yearlist.get(position).getFlag()==0) {
                    checkbox.setImageResource(R.drawable.ic_action_accept);
                    sub_name.setTextColor(Color.parseColor("#ffffff"));
                    r1.setBackgroundColor(Color.parseColor("#009688"));
                    SendHOD.yearlist.get(position).setFlag(1);
                    //r2.setBackgroundColor(c.getResources().getColor());
                    SendHOD.chosenYear.add(SendHOD.yearlist.get(position));
                    Log.e("Array inserted", SendHOD.yearlist.get(position).getSubject_group_code());
                } else {
                    checkbox.setImageResource(R.drawable.abc_btn_switch_to_on_mtrl_00001);
                    r1.setBackgroundColor(Color.parseColor("#d6d7d7"));
                    sub_name.setTextColor(Color.parseColor("#37474f"));
                    SendHOD.yearlist.get(position).setFlag(0);
                    //r2.setBackgroundColor(c.getResources().getColor(R.color.black));
                    SendHOD.chosenYear.remove(SendHOD.yearlist.get(position));
                    Log.e("Array removed",SendHOD.yearlist.get(position).getSubject_group_code());

                }
            }
        });

        return v;
    }
}
