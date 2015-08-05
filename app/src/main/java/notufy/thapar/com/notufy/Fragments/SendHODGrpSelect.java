package notufy.thapar.com.notufy.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import notufy.thapar.com.notufy.Activity.SendHOD;
import notufy.thapar.com.notufy.Adapters.HODgrpSelectAdapter;
import notufy.thapar.com.notufy.Beans.subject_group_bean;
import notufy.thapar.com.notufy.R;
import notufy.thapar.com.notufy.dataBase.dataBase;

/**
 * Created by prat on 4/5/2015.
 */
public class SendHODGrpSelect extends Fragment {
    Context mContext;
    public static ListView list;
    public static HODgrpSelectAdapter hoDgrpSelectAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.tab1, container, false);

        mContext=getActivity();
        dataBase db=new dataBase(mContext);
        //ArrayList<subject_group_bean> grps=db.get_extra_grp();
        hoDgrpSelectAdapter=new HODgrpSelectAdapter(mContext, SendHOD.group_list);
        list=(ListView)v.findViewById(R.id.listView1);
        list.setAdapter(hoDgrpSelectAdapter);
        return v;
    }
}
