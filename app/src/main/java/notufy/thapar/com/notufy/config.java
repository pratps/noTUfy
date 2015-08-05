package notufy.thapar.com.notufy;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import notufy.thapar.com.notufy.Beans.hostel_message;
import notufy.thapar.com.notufy.Beans.society;
import notufy.thapar.com.notufy.Beans.society_message;
import notufy.thapar.com.notufy.Beans.subject_group_bean;
import notufy.thapar.com.notufy.Beans.teacher_message;
import notufy.thapar.com.notufy.dataBase.dataBase;

/**
 * Created by prat on 3/31/2015.
 */
public class config {
    Context context;
    final public static  int AUTHENTICATION_FAILURE=0;
    final public static int SUCCESS=1;
    final public static int PDO_ERROR=2;
    final public static int INVALID_TEXT=3;


    final public static int STUDENT=1;
    final public static int CONVENER=2;
    final public static int TEACHER=4;
    final public static int SUBJECT_COORD=8;
    final public static int YEAR_COORD=16;
    final public static int HOD=32;
    final public static int HOSTEL_RESIDENT=128;
    final public static int HOSTEL_ADMIN=64;

    SharedPreferences personal_info;
    SharedPreferences.Editor personal_info_editor;

    public static String URL="http://learntheeasyway.tk/notufy/app";
    public static String DOWNLOAD_URL="http://learntheeasyway.tk/notufy";
    public config(Context context)
    {
        this.context=context;
        personal_info=getPersonalInfoPreference();
        personal_info_editor=personal_info.edit();
    }

    public boolean isNetworkConnectionAvailable() {
        ConnectivityManager cm;
        cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) return false;
        NetworkInfo.State network = info.getState();
        return (network == NetworkInfo.State.CONNECTED || network == NetworkInfo.State.CONNECTING);
    }

    public String getUsercode()
    {
        SharedPreferences registry = context.getSharedPreferences("user_personal_info", Context.MODE_PRIVATE);
        return registry.getString("user_code", "");

    }


    public SharedPreferences getPersonalInfoPreference()
    {
        return context.getSharedPreferences("user_personal_info",Context.MODE_PRIVATE);
    }
    public SharedPreferences getSettings()
    {
        return context.getSharedPreferences("settings",Context.MODE_PRIVATE);
    }
    public Long getTeachermessagelastsync()
    {
        SharedPreferences registry = context.getSharedPreferences("sync_state", Context.MODE_PRIVATE);
        return registry.getLong("teacher_message_sync", 0);
    }

    public Long getHostelmessagelastsync()
    {
        SharedPreferences registry = context.getSharedPreferences("sync_state", Context.MODE_PRIVATE);
        return registry.getLong("hostel_message_sync", 0);
    }

    public void setHostelmessagelastsync()
    {
        SharedPreferences registry = context.getSharedPreferences("sync_state", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = registry.edit();
        long unixTime = System.currentTimeMillis() / 1000L;
        Log.e("last_sync saved",Long.toString(unixTime));
        editor.putLong("hostel_message_sync",unixTime);
        editor.commit();

    }

    public void setTeachermessagelastsync()
    {
        SharedPreferences registry = context.getSharedPreferences("sync_state", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = registry.edit();
        long unixTime = System.currentTimeMillis() / 1000L;
        Log.e("last_sync",Long.toString(unixTime));
        editor.putLong("teacher_message_sync",unixTime);
        editor.commit();

    }

    public Long getSocietymessagelastsync()
    {
        SharedPreferences registry = context.getSharedPreferences("sync_state", Context.MODE_PRIVATE);
        return registry.getLong("society_message_sync", 0);
    }

    public void setSocietymessagelastsync()
    {
        SharedPreferences registry = context.getSharedPreferences("sync_state", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = registry.edit();
        long unixTime = System.currentTimeMillis() / 1000L;
        editor.putLong("society_message_sync",unixTime);

        editor.commit();

    }

    public  void setPassword(String s){
        personal_info=getPersonalInfoPreference();
        personal_info_editor=personal_info.edit();
        personal_info_editor.putString("password",s);
        personal_info_editor.apply();
    }

    public SharedPreferences getError(){
        return context.getSharedPreferences("error",Context.MODE_PRIVATE);
    }


    public ArrayList<teacher_message> enumerate_teacher_messages(JSONObject json, dataBase db,String s) throws JSONException {
        JSONArray messages=json.getJSONArray(s);
        ArrayList<teacher_message> messages_List=new ArrayList<teacher_message>();
        Log.e("msg length:-",Integer.toString(messages.length()));
        for(int i=0;i<messages.length();i++){
            JSONObject obj= (JSONObject) messages.get(i);
            Log.e("msg:-",obj.toString());
            teacher_message m=new teacher_message();
            m.setDatetime(nullcheck(obj.getString("datetime")));
            m.setFile_link(nullcheck(obj.getString("file_link")));
            m.setFile_size(nullcheck(obj.getString("file_size")));
            //m.setSender_icon(obj.get);
            m.setInfo(nullcheck(obj.getString("info")));
            m.setSender_user_code(nullcheck(obj.getString("sender_user_code")));
            m.setSender_user_type(nullcheck(obj.getString("sender_user_type")));
            //m.setTeacher_icon();
            m.setSender_user_name(nullcheck(obj.getString("sender_name")));
            m.setMessage_id(Long.parseLong(nullcheck(obj.getString("message_id"))));
            m.setFile_name(nullcheck(obj.getString("file_name")));
            //m.setFile_path();
            messages_List.add(m);
        }
        db.insert_teacher_messages(messages_List);
        return messages_List;
    }


    public ArrayList<hostel_message> enumerate_hostel_messages(JSONObject json,dataBase db) throws JSONException{
        JSONArray messages=json.getJSONArray("hostel_messages");
        ArrayList<hostel_message> message_List=new ArrayList<hostel_message>();
        for(int i=0;i<messages.length();i++){
            JSONObject obj=(JSONObject)messages.get(i);
            hostel_message m=new hostel_message();
            m.setDatetime(nullcheck(obj.getString("datetime")));
            m.setMessage_id(Long.parseLong(nullcheck(obj.getString("message_id"))));
            m.setHeading(nullcheck(obj.getString("heading")));
            m.setInfo(nullcheck(obj.getString("info")));
            message_List.add(m);
        }
        db.insert_hostel_messages(message_List);
        return message_List;
    }

    public ArrayList<society_message> enumerate_society_messages(JSONObject json, dataBase db) throws JSONException {
        JSONArray society_messages=json.getJSONArray("society_messages");
        ArrayList<society_message> messages_List=new ArrayList<society_message>();
        society soc;
        for(int i=0;i<society_messages.length();i++){
            JSONObject obj= (JSONObject) society_messages.get(i);
            Log.e("msg:-",obj.toString());
            society_message m=new society_message();
            m.setDatetime(nullcheck(obj.getString("datetime")));
            m.setSociety_code(nullcheck(obj.getString("society_code")));
            m.setHeading(nullcheck(obj.getString("heading")));
            //m.setSender_icon(obj.get);
            m.setInfo(nullcheck(obj.getString("info")));
            m.setEvent_datetime(nullcheck(obj.getString("event_datetime")));
            m.setEvent_venue(nullcheck(obj.getString("event_venue")));
            //m.setTeacher_icon();
            m.setImage_url(nullcheck(obj.getString("image_link")));
            m.setMessage_id(Long.parseLong(nullcheck(obj.getString("message_id"))));
            m.setImage_size(nullcheck(obj.getString("image_size")));
            soc=db.get_society(nullcheck(m.getSociety_code()));
            m.setSociety_color(nullcheck(soc.getSociety_color()));
            m.setSociety_icon_link(nullcheck(soc.getSociety_icon_link()));
            m.setSociety_icon_path(nullcheck(soc.getSociety_icon_path()));
            m.setSociety_name(nullcheck(soc.getSociety_name()));
            //m.setFile_path();
            messages_List.add(m);
        }
        db.insert_society_messages(messages_List);
        return messages_List;
    }

    public void enumerate_all_societies(JSONObject json, dataBase db) throws JSONException {
        JSONArray society_array=json.getJSONArray("societies");
        ArrayList<society> societyList=new ArrayList<society>();
        for(int i=0;i<society_array.length();i++){
            JSONObject obj= (JSONObject) society_array.get(i);
            society s=new society();
            s.setSociety_code(nullcheck(obj.getString("society_code")));
            s.setSociety_color(nullcheck(obj.getString("society_colour")));
            s.setSociety_name(nullcheck(obj.getString("society_name")));
            s.setSociety_icon_link(nullcheck(obj.getString("society_icon")));
            s.setSociety_info(nullcheck(obj.getString("society_info")));
            societyList.add(s);
        }
        db.insert_societies(societyList);
    }


    public void setStudentPersonalInfo(JSONObject json) throws JSONException{

        JSONObject student_personal_info=new JSONObject(json.getString("student_personal_info"));
        personal_info_editor.putString("user_code",nullcheck(student_personal_info.getString("user_code")));
        personal_info_editor.putString("name",nullcheck(student_personal_info.getString("name")));
        personal_info_editor.putString("email_id",nullcheck(student_personal_info.getString("email_id")));
        personal_info_editor.putString("society_subscription", nullcheck(student_personal_info.getString("society_subscription")));
        personal_info_editor.commit();
    }

    public void setTeacherPersonalInfo(JSONObject json) throws JSONException{
        JSONObject teacher_personal_info=new JSONObject(json.getString("teacher_personal_info"));
        personal_info_editor.putString("user_code",nullcheck(teacher_personal_info.getString("user_code")));
        personal_info_editor.putString("name",nullcheck(teacher_personal_info.getString("name")));
        personal_info_editor.putString("department", nullcheck(teacher_personal_info.getString("department")));
        personal_info_editor.putString("email_id",nullcheck(teacher_personal_info.getString("email_id")));
        personal_info_editor.putString("icon",nullcheck(teacher_personal_info.getString("icon")));
        personal_info_editor.putString("society_subscription",nullcheck(teacher_personal_info.getString("society_subscription")));
        personal_info_editor.commit();
    }

    public void setConvenerPersonalInfo(JSONObject json) throws JSONException{
        JSONObject convener_info=new JSONObject(json.getString("convener_society"));
        personal_info_editor.putString("convener_society_name",nullcheck(convener_info.getString("society_name")));
        personal_info_editor.putString("convener_society_code",nullcheck(convener_info.getString("society_code")));
        personal_info_editor.commit();
    }

    public void setSubCoordPersonalInfo(JSONObject json) throws JSONException{
        JSONObject sub_coord=new JSONObject(json.getString("extra"));
        personal_info_editor.putString("subject_code",nullcheck(sub_coord.getString("subject_code")));
        personal_info_editor.putString("subject_name",nullcheck(sub_coord.getString("subject_name")));
        personal_info_editor.commit();
    }

    public void setHostelResidentPersonalInfo(JSONObject json) throws  JSONException{
        JSONObject hostel_info=new JSONObject(json.getString("hostel_info"));
        personal_info_editor.putString("hostel_id",nullcheck(hostel_info.getString("hostel_id")));
        personal_info_editor.putString("hostel_name",nullcheck(hostel_info.getString("hostel_name")));
        personal_info_editor.putString("room_number",nullcheck(hostel_info.getString("room_number")));
        personal_info_editor.putString("mess_bill",nullcheck(hostel_info.getString("mess_bill")));
        personal_info_editor.commit();
    }

    public void setHostelAdminPersonalInfo(JSONObject json) throws JSONException{
        JSONObject hostel_admin=new JSONObject(json.getString("hostel_admin"));
        personal_info_editor.putString("hostel_id",nullcheck(hostel_admin.getString("hostel_id")));
        personal_info_editor.putString("hostel_name",nullcheck(hostel_admin.getString("hostel_name")));
        personal_info_editor.commit();
    }







    public ArrayList<subject_group_bean>parseSubgrpMap(JSONObject json,String s) throws JSONException
    {
        JSONArray sub_array=json.getJSONArray(s);
        ArrayList<subject_group_bean> subgrp=new ArrayList<subject_group_bean>();
        HashMap<String,String> sub_map=new HashMap<String,String>();
        String grp,sub,ltp,sub_grp_code,mapltp;
        for(int i=0;i<sub_array.length();i++)
        {
            JSONObject obj=(JSONObject)sub_array.get(i);
            Log.e("obj",obj.toString());
            grp=nullcheck(obj.getString("group_code"));
            sub=nullcheck(obj.getString("subject_code"));
            ltp= String.valueOf(sub.charAt(sub.length()-1));
            sub=sub.substring(0,sub.length()-1);
            Log.e("subject",sub);
            sub_grp_code=grp+'_'+sub;
            Log.e("grp",grp);
            Log.e("subgrpcode",sub_grp_code);
            mapltp=sub_map.get(sub_grp_code);
            if(mapltp==null)
            {
                sub_map.put(sub_grp_code,ltp);
            }
            else
            {
                sub_map.put(sub_grp_code,mapltp+ltp);
            }
            Log.e("map",sub_map.toString());
        }
        subject_group_bean bean;
        for(Map.Entry<String,String> entry:sub_map.entrySet()){
            bean=new subject_group_bean();
            sub_grp_code=entry.getKey();
            ltp=entry.getValue();
            bean.setLTP(ltp);
            bean.setSubject_group_code(sub_grp_code);
            String[] ar=sub_grp_code.split("_");
            bean.setYear(ar[0]);
            bean.setCourse(ar[1]);
            bean.setGroup(ar[2]);
            bean.setSubject_code(ar[3]);
            subgrp.add(bean);
        }
        return subgrp;

    }

    public ArrayList<subject_group_bean> parseExtragrp(JSONObject json)throws JSONException{
        JSONObject obj=(JSONObject)json.getJSONObject("extra");
        JSONArray jarray=(JSONArray)obj.getJSONArray("group");
        ArrayList<subject_group_bean> sub_array=new ArrayList<subject_group_bean>();
        subject_group_bean bean;
        for(int i=0;i<jarray.length();i++){
            String s=jarray.getString(i);
            String[] ar=s.split("_");
            bean=new subject_group_bean();
            bean.setYear(ar[0]);
            bean.setCourse(ar[1]);
            bean.setGroup(ar[2]);
            bean.setSubject_group_code(ar[3]);
            sub_array.add(bean);
        }

        return sub_array;
    }





    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    private String nullcheck(String s) {
        if (s != null)
            return s;
        else
            return "";
    }
}
