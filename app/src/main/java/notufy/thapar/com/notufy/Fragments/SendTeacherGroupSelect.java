package notufy.thapar.com.notufy.Fragments;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import notufy.thapar.com.notufy.Activity.SendTeacher;
import notufy.thapar.com.notufy.Adapters.GroupSelectAdapter;
import notufy.thapar.com.notufy.Beans.subject_group_bean;
import notufy.thapar.com.notufy.R;
import notufy.thapar.com.notufy.dataBase.dataBase;

/**
 * Created by hp1 on 21-01-2015.
 */
public class SendTeacherGroupSelect extends Fragment {

    public static GroupSelectAdapter groupadapter;
    public static ListView grouplistview;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         View v = inflater.inflate(R.layout.tab2,container,false);
        grouplistview=(ListView)v.findViewById(R.id.group_select);
        dataBase db=new dataBase(getActivity());
        groupadapter=new GroupSelectAdapter(SendTeacher.group_list,getActivity());
        //Log.e("adapter",Integer.toString(SendTeacher.group_list.size()));
        grouplistview.setAdapter(groupadapter);
        return v;
    }
}