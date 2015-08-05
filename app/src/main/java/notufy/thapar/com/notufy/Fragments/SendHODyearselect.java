package notufy.thapar.com.notufy.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.common.GooglePlayServicesUtil;

import java.util.ArrayList;

import notufy.thapar.com.notufy.Activity.SendHOD;
import notufy.thapar.com.notufy.Adapters.HODYearSelectAdapter;
import notufy.thapar.com.notufy.Adapters.HODgrpSelectAdapter;
import notufy.thapar.com.notufy.Beans.subject_group_bean;
import notufy.thapar.com.notufy.R;
import notufy.thapar.com.notufy.dataBase.dataBase;

/**
 * Created by prat on 5/3/2015.
 */
public class SendHODyearselect extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab1,container,false);

        dataBase db=new dataBase(getActivity());

        HODYearSelectAdapter hoDyrSelectAdapter=new HODYearSelectAdapter(getActivity());
        ListView list=(ListView)v.findViewById(R.id.listView1);
        list.setAdapter(hoDyrSelectAdapter);

        return v;
    }
}
