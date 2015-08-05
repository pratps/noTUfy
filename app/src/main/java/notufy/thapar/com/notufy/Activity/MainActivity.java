package notufy.thapar.com.notufy.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import notufy.thapar.com.notufy.Asynctasks.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import notufy.thapar.com.notufy.Beans.society;
import notufy.thapar.com.notufy.Beans.teacher_message;
import notufy.thapar.com.notufy.GCM.gcm_config;
import notufy.thapar.com.notufy.R;
import notufy.thapar.com.notufy.config;
import notufy.thapar.com.notufy.dataBase.dataBase;


public class MainActivity extends ActionBarActivity {
    EditText username,password;
    Button submit;
    Context mContext;
    SharedPreferences share;
    String user_email,user_password;
    String LOGIN_URL="http://learntheeasyway.tk/notufy/app/login.php";
    String gcm_id=null;
    config conf;
    SharedPreferences.Editor edit;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext=this;
        mContext.deleteDatabase("noTUfy_db");

        conf=new config(mContext);
        share= conf.getPersonalInfoPreference();
        edit=share.edit();
        username=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);

        submit=(Button)findViewById(R.id.submit);
        gcm_id=new gcm_config(mContext).getGCMRegId();
//        getApplicationContext().deleteDatabase("noTUfy_db");
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                user_email=username.getText().toString();
                user_password=password.getText().toString();
                HashMap<String,String> map=new HashMap<String, String>();
                map.put("email_id",user_email);
                map.put("password",user_password);
                map.put("gcm_id",gcm_id);
                Log.e("map",map.toString());
                new AttemptLogin(true,"Please Wait..",LOGIN_URL,getApplicationContext(),map,MainActivity.this){
                    @Override
                    public void setonCallback(JSONObject json) {
                        if(json!=null)
                        enumerate(json);
                        else
                        Toast.makeText(mContext,"Network Connection Error",Toast.LENGTH_LONG).show();

                    }
                }.execute();
            }
        });
    }
    public void enumerate(JSONObject json) {
        if(json!=null) {
            try {
                dataBase db = new dataBase(getApplication());
                JSONObject code = json.getJSONObject("code");


                if (code.getInt("text") != config.SUCCESS) {
                    Toast.makeText(getApplicationContext(), "Please enter valid text.", Toast.LENGTH_SHORT).show();
                } else {
                    if (code.getInt("auth") != config.SUCCESS) {
                        Toast.makeText(getApplicationContext(), "Authentication Failure", Toast.LENGTH_SHORT).show();
                    } else {
                        int user_type = json.getInt("user_type");
                        Boolean stu_personal_error=false,teach_personal_error=false;
                        conf.enumerate_all_societies(json, db);
                        conf.getPersonalInfoPreference().edit().putInt("user_type",user_type).apply();
                        db.insert_society_messages(conf.enumerate_society_messages(json, db));
                        conf.setPassword(user_password);
                        SharedPreferences.Editor error_editor=conf.getError().edit();
                        if ((user_type & config.STUDENT) != 0) {
                            if (code.getInt("stu_personal") == config.SUCCESS) {
                                conf.setStudentPersonalInfo(json);
                                if (code.getInt("stu_academic") == config.SUCCESS) {
                                    db.insert_student_sub_grp_map(conf.parseSubgrpMap(json, "student_subject_group_map"));
                                    conf.enumerate_teacher_messages(json, db, "student_messages");
                                } else {
                                    error_editor.putBoolean("student_academic",true);
                                }
                            } else {
                                //set error flag
                                stu_personal_error=true;
                            }

                        }
                        Log.e("message","student info parsed");
                        if ((user_type & config.CONVENER) != 0) {
                            if (code.getInt("convener") == config.SUCCESS) {
                                conf.setConvenerPersonalInfo(json);
                            } else {
                                error_editor.putBoolean("convener",true);
                            }
                        }
                        Log.e("message","convener info parsed");

                        if ((user_type & config.TEACHER) != 0) {
                            if (code.getInt("teach_personal") == config.SUCCESS) {
                                conf.setTeacherPersonalInfo(json);
                                Log.e("message","teacher personal info parsed");
                                if (code.getInt("teach_academic") == config.SUCCESS) {
                                    db.insert_teacher_sub_grp_map(conf.parseSubgrpMap(json, "teacher_subject_group_map"));
                                    Log.e("message","teacher subgrp info parsed");
                                    db.insert_teacher_messages(conf.enumerate_teacher_messages(json, db, "teacher_messages"));
                                } else {
                                    error_editor.putBoolean("teacher_academic",true);
                                }

                            } else {
                                //set error flag
                                teach_personal_error=true;
                            }
                        }
                        Log.e("message","teacher info parsed");

                        if ((user_type & config.SUBJECT_COORD) != 0) {
                            if (code.getInt("sub_coordinator") == config.SUCCESS) {
                                conf.setSubCoordPersonalInfo(json);
                            } else {
                                //set error flag
                                error_editor.putBoolean("subject_coordinator",true);
                            }
                        }
                        Log.e("message","subject coord info parsed");

                        if ((user_type & config.YEAR_COORD) != 0) {
                            if (code.getInt("year_coordinator") == config.SUCCESS) {
                                db.insert_extra_grp(conf.parseExtragrp(json));
                            } else {
                                error_editor.putBoolean("year_coordinator",true);
                            }
                        }

                        Log.e("message","year coord ztinfo parsed");

                        if ((user_type & config.HOD) != 0) {
                            if (code.getInt("hod") == config.SUCCESS) {
                                db.insert_extra_grp(conf.parseExtragrp(json));
                            } else {
                                //set error flag
                                error_editor.putBoolean("hod",true);
                            }
                        }

                        Log.e("message","HOD info parsed");

                        if ((user_type & config.HOSTEL_ADMIN) != 0) {
                            if (code.getInt("hostel_admin") == config.SUCCESS) {
                                conf.setHostelAdminPersonalInfo(json);
                            } else {
                                //set error flag
                                error_editor.putBoolean("hostel_admin",true);
                            }
                        }

                        Log.e("message","hostel admin info parsed");

                        if ((user_type & config.HOSTEL_RESIDENT) != 0) {
                            if (code.getInt("hostel") == config.SUCCESS) {
                                conf.setHostelResidentPersonalInfo(json);
                                Log.e("message","hostel res personal info parsed");
                                db.insert_hostel_messages(conf.enumerate_hostel_messages(json, db));
                            } else {
                                //set error flag
                                error_editor.putBoolean("hostel_resident",true);
                            }
                        }
                       Log.e("message","hostel res info parsed");


                        if(!stu_personal_error&&!teach_personal_error) {
                            Intent i = new Intent(MainActivity.this, logined.class);
                            startActivity(i);
                            finish();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Your info is unavailable. Please contact admin.",Toast.LENGTH_LONG).show();
                        }

                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Network Connection Error",Toast.LENGTH_SHORT).show();
            }
        }else
        {
            Toast.makeText(getApplicationContext(),"Network Connection Error",Toast.LENGTH_SHORT).show();
        }
    }








    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
