package notufy.thapar.com.notufy.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import notufy.thapar.com.notufy.Activity.SendHOD;
import notufy.thapar.com.notufy.Activity.SendTeacher;
import notufy.thapar.com.notufy.Beans.subject_group_bean;
import notufy.thapar.com.notufy.R;


public class HODgrpSelectAdapter extends BaseAdapter {

    Context c;

    ArrayList<subject_group_bean> group_list;
    LayoutInflater mInflater;
    public HODgrpSelectAdapter(Context context, ArrayList<subject_group_bean> group_list) {
        this.c = context;
        this.group_list = group_list;
        mInflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return group_list.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return group_list.get(position);
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = null;

        if(group_list.get(position).getFlag()==2)
        {
            v = mInflater.inflate(R.layout.layout_group_header, parent, false);
            TextView sub=(TextView)v.findViewById(R.id.subject_code);
            LinearLayout back=(LinearLayout)v.findViewById(R.id.header_back);

            sub.setText(group_list.get(position).getCourse()+" "+group_list.get(position).getYear()+" year");

        }

        else {

            v = mInflater.inflate(R.layout.layout_subject_select, parent, false);
            subject_group_bean grp = group_list.get(position);
            final String item =  grp.getGroup();
            final ImageView checkbox = (ImageView) v.findViewById(R.id.checkbox);
            final TextView sub_name = (TextView) v.findViewById(R.id.subj_name);


            sub_name.setText(item);
            final RelativeLayout r1 = (RelativeLayout) v.findViewById(R.id.lay1);
            final RelativeLayout r2 = (RelativeLayout) v.findViewById(R.id.lay2);

            if (SendTeacher.chosenSubjects.contains(item)) {
                checkbox.setImageResource(R.drawable.ic_action_accept);
                sub_name.setTextColor(Color.parseColor("#ffffff"));
                r1.setBackgroundColor(Color.parseColor("#009688"));
                group_list.get(position).setFlag(1);

            } else {
                checkbox.setImageResource(R.drawable.abc_btn_switch_to_on_mtrl_00001);
                r1.setBackgroundColor(Color.parseColor("#d6d7d7"));
                sub_name.setTextColor(Color.parseColor("#37474f"));
                group_list.get(position).setFlag(0);

            }

            r1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (group_list.get(position).getFlag() == 0) {
                        checkbox.setImageResource(R.drawable.ic_action_accept);
                        sub_name.setTextColor(Color.parseColor("#ffffff"));
                        r1.setBackgroundColor(Color.parseColor("#009688"));
                        group_list.get(position).setFlag(1);
                        //r2.setBackgroundColor(c.getResources().getColor());
                        SendHOD.chosenGroups.add(group_list.get(position));
                        Log.e("Array inserted", group_list.get(position).getSubject_group_code());
                    } else {
                        checkbox.setImageResource(R.drawable.abc_btn_switch_to_on_mtrl_00001);
                        r1.setBackgroundColor(Color.parseColor("#d6d7d7"));
                        sub_name.setTextColor(Color.parseColor("#37474f"));
                        group_list.get(position).setFlag(0);
                        //r2.setBackgroundColor(c.getResources().getColor(R.color.black));
                        SendHOD.chosenGroups.remove(group_list.get(position));
                        Log.e("Array removed", group_list.get(position).getSubject_group_code());

                    }
                }
            });
        }
        return v;
    }
}
