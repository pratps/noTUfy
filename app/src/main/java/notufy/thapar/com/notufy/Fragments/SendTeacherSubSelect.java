package notufy.thapar.com.notufy.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collection;

import notufy.thapar.com.notufy.Adapters.SubjectSelectAdapter;
import notufy.thapar.com.notufy.Beans.subject_group_bean;
import notufy.thapar.com.notufy.R;
import notufy.thapar.com.notufy.dataBase.dataBase;


public class SendTeacherSubSelect extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.tab1,container,false);
        ListView lv= (ListView) v.findViewById(R.id.listView1);
        int i;

        dataBase db=new dataBase(getActivity());


        SubjectSelectAdapter adapter=new SubjectSelectAdapter(getActivity().getApplicationContext(),db.get_teacher_subjects());
        lv.setAdapter(adapter);
        return v;
    }

}