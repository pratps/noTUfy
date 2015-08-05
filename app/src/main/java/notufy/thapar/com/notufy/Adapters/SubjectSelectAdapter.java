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

import notufy.thapar.com.notufy.Activity.SendTeacher;
import notufy.thapar.com.notufy.Beans.subject_group_bean;

import notufy.thapar.com.notufy.R;


public class SubjectSelectAdapter extends BaseAdapter {

    Context c;

    ArrayList<subject_group_bean> subject_list;

    public SubjectSelectAdapter(Context context, ArrayList<subject_group_bean> subject_list) {
        this.c = context;
        this.subject_list = subject_list;
    }

    @Override
    public int getCount() {
        return subject_list.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return subject_list.get(position);
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


        final String item = subject_list.get(position).getSubject_code();
        final ImageView checkbox = (ImageView) v.findViewById(R.id.checkbox);
        final TextView sub_name = (TextView) v.findViewById(R.id.subj_name);


        sub_name.setText(item);
        final RelativeLayout r1 = (RelativeLayout) v.findViewById(R.id.lay1);
        final RelativeLayout r2 = (RelativeLayout) v.findViewById(R.id.lay2);

        if(SendTeacher.chosenSubjects.contains(item))
        {
            checkbox.setImageResource(R.drawable.ic_action_accept);
            sub_name.setTextColor(Color.parseColor("#ffffff"));
            r1.setBackgroundColor(Color.parseColor("#009688"));
            subject_list.get(position).setFlag(1);

        }
        else
        {
            checkbox.setImageResource(R.drawable.abc_btn_switch_to_on_mtrl_00001);
            r1.setBackgroundColor(Color.parseColor("#d6d7d7"));
            sub_name.setTextColor(Color.parseColor("#37474f"));
            subject_list.get(position).setFlag(0);

        }

        r1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (subject_list.get(position).getFlag()==0) {
                    checkbox.setImageResource(R.drawable.ic_action_accept);
                    sub_name.setTextColor(Color.parseColor("#ffffff"));
                    r1.setBackgroundColor(Color.parseColor("#009688"));
                    subject_list.get(position).setFlag(1);
                    //r2.setBackgroundColor(c.getResources().getColor());
                    SendTeacher.chosenSubjects.add(item);
                    Log.e("Array",SendTeacher.chosenSubjects.toString());
                } else {
                    checkbox.setImageResource(R.drawable.abc_btn_switch_to_on_mtrl_00001);
                    r1.setBackgroundColor(Color.parseColor("#d6d7d7"));
                    sub_name.setTextColor(Color.parseColor("#37474f"));
                    subject_list.get(position).setFlag(0);
                    //r2.setBackgroundColor(c.getResources().getColor(R.color.black));
                    SendTeacher.chosenSubjects.remove(item);
                    for(int i=0;i< SendTeacher.chosenGroupcodes.size();i++)
                    {
                        if(SendTeacher.chosenGroupcodes.get(i).contains(item))
                            SendTeacher.chosenGroupcodes.remove(i);
                    }
                    Log.e("Array",SendTeacher.chosenSubjects.toString());
                    Log.e("Array2",SendTeacher.chosenGroupcodes.toString());
                }
            }
        });

        return v;
    }
}
