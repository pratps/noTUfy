package notufy.thapar.com.notufy.Fragments;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;

import notufy.thapar.com.notufy.Activity.SendTeacher;
import notufy.thapar.com.notufy.Asynctasks.AttemptLogin;
import notufy.thapar.com.notufy.R;
import notufy.thapar.com.notufy.config;

/**
 * Created by hp1 on 21-01-2015.
 */
public class SendTeacherSendMessage extends Fragment {
    String server_file="/teacher_message.php";
    String reg_info="^[a-z|A-Z| |.]+$";
    int info_error=1;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_send_message_teacher,container,false);
        final EditText info=(EditText)v.findViewById(R.id.info);

        int padding_in_dp = 10;  // 6 dps
        final float scale = getResources().getDisplayMetrics().density;
        int px = (int) (padding_in_dp * scale + 0.5f);
        info.setPadding(px, px, px, px);
        RelativeLayout b=(RelativeLayout)v.findViewById(R.id.sendmessage);
        info.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String i=s.toString();
                if(s==null||s=="")
                {

                    info_error=1;
                }
                else
                {
                    if(i.matches(reg_info))
                    {
                        info.setTextColor(Color.parseColor("#000000"));
                        info_error=0;
                    }
                    else
                    {
                        info.setTextColor(Color.parseColor("#ff0000"));
                        info_error=1;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                config conf=new config(getActivity());
                if(info_error!=1) {

                    if(conf.isNetworkConnectionAvailable()) {


                        String in = info.getText().toString();
                        String groups = "";
                        for (String s : SendTeacher.chosenGroupcodes) {
                            groups += s + " ";
                        }
                        groups = groups.trim();
                        SharedPreferences r = conf.getPersonalInfoPreference();
                        String user_code = r.getString("user_code", "");
                        String password = r.getString("password", "");
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("sender_user_code", user_code);
                        map.put("password", password);
                        map.put("to_group", groups);
                        map.put("info", in);
                        Log.e("map", map.toString());
                        Log.e("Array1", SendTeacher.chosenSubjects.toString());
                        Log.e("Array2", SendTeacher.chosenGroupcodes.toString());

                        new AttemptLogin(true, "Sending message", config.URL + server_file, getActivity(), map, getActivity()) {

                            public void setonCallback(JSONObject json) {
                                super.setonCallback(json);
                                if(json!=null)
                                Toast.makeText(getActivity(), json.toString(), Toast.LENGTH_LONG).show();
                                else
                                    Toast.makeText(getActivity(),"Network Connection Error", Toast.LENGTH_LONG).show();
                            }
                        }.execute();
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "No Network Connection", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getActivity(), "Please enter valid text.", Toast.LENGTH_SHORT).show();

                }

            }
        });
        return v;
    }
}