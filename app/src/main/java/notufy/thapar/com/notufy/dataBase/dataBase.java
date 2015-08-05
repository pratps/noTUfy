package notufy.thapar.com.notufy.dataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;

import notufy.thapar.com.notufy.Beans.hostel_message;
import notufy.thapar.com.notufy.Beans.society_message;
import notufy.thapar.com.notufy.Beans.teacher_message;
import notufy.thapar.com.notufy.Beans.society;
import notufy.thapar.com.notufy.Beans.subject_group_bean;

public class dataBase extends SQLiteOpenHelper{

    int teacher_message_limit=10,hostel_message_limit=10,society_message_limit=8;

	public dataBase(Context context) {
		super(context, "noTUfy_db", null, 1);
		// TODO Auto-generated constructor stub
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String s="create table society_messages(message_id int(15) primary key,society_code varchar(50),heading varchar(300)," +
                "info varchar(1000),event_venue varchar(100),datetime varchar(100)," +
                "event_datetime varchar(100),event_timestamp int(15),image_url varchar(200),image_path varchar(200),image_size varchar(100));";
		db.execSQL(s);
		s="create table teacher_messages(message_id int(15) primary key,sender_user_code varchar(100),sender_user_name varchar(200),sender_user_type varchar(20)," +
                "sender_icon varchar(200), info varchar(1000), datetime varchar(100), file_name varchar(200),file_size varchar(50)," +
                "file_link varchar(200),file_path varchar(200));";
        db.execSQL(s);
        s="create table societies(society_code varchar(50) primary key,society_name varchar(200),society_icon_link varchar(200)," +
                "society_icon_path varchar(200),society_info varchar(200),society_color varchar(100));";
        db.execSQL(s);
        s="create table teacher_icon(teacher_code varchar(50),teacher_name varchar(200),teacher_icon BLOB,teacher_icon_link varchar(200));";
        db.execSQL(s);
        s="create table hostel_messages(message_id int(15) primary key,heading varchar(300),info varchar(1000),datetime varchar(200));";
        db.execSQL(s);
        //teacher sub grp map
        s="create table teacher_sub_grp_map(year varchar(20),course varchar(20),yeargroup varchar(50)," +
                "subject_code varchar(50),LTP varchar(10),subject_group_code varchar(100) primary key);";
        db.execSQL(s);
        //student sub grp map
        s="create table student_sub_grp_map(year varchar(20),course varchar(20),yeargroup varchar(50)," +
                "subject_code varchar(50),LTP varchar(10),subject_group_code varchar(100) primary key);";
        db.execSQL(s);
        s="create table extra_grp(year varchar(20),course varchar(20),yeargroup varchar(50)," +
                "group_code varchar(100) primary key);";
        db.execSQL(s);
    }


    @Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

 //functions for society_message table
	public void insert_society_messages(ArrayList<society_message> messagelist)
	{
		SQLiteDatabase db= this.getWritableDatabase();
		ContentValues values=new ContentValues();
		for(society_message m:messagelist)
		{

            values.put("message_id",m.getMessage_id());
            values.put("heading",m.getHeading());
            values.put("datetime",m.getDatetime());
            values.put("society_code",m.getSociety_code());
            values.put("info",m.getInfo());
            values.put("event_datetime",m.getEvent_datetime());
            values.put("event_timestamp",m.getEvent_timestamp());
            values.put("event_venue",m.getEvent_venue());
            values.put("image_url", m.getImage_url());
            values.put("image_size",m.getImage_size());
            values.put("image_path", m.getImage_path());
            db.insert("society_messages", null, values);
		}
		db.close();
	}

	public void insert_society_message(society_message m)
	{
		SQLiteDatabase db= this.getWritableDatabase();
		ContentValues values=new ContentValues();
        values.put("message_id",m.getMessage_id());
        values.put("heading",m.getHeading());
        values.put("datetime",m.getDatetime());
        values.put("society_code",m.getSociety_code());
        values.put("info",m.getInfo());
        values.put("event_datetime",m.getEvent_datetime());
        values.put("event_timestamp",m.getEvent_timestamp());
        values.put("event_venue",m.getEvent_venue());
        values.put("image_url", m.getImage_url());
        values.put("image_size",m.getImage_size());
        values.put("image_path", m.getImage_path());
        db.insert("society_messages", null, values);
        db.close();
	}

    public ArrayList<Long> get_society_message_id(Long last_sync){
        ArrayList<Long> message_ids=new ArrayList<Long>();
        String s = "select message_id from society_messages where message_id>" + Long.toString(last_sync)  + ";";
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery(s,null);
        while(c.moveToNext()){
            message_ids.add(c.getLong(0));
        }
        return message_ids;
    }

	public ArrayList<society_message> get_society_messages(Long last_message_id)
	{
		ArrayList<society_message> messagelist=new ArrayList<society_message>();
		String s;
        if(last_message_id<0)
            s="select * from society_messages order by message_id desc limit "+society_message_limit+";";
        else
        {
            s = "select * from society_messages where message_id<" + Long.toString(last_message_id) + " order by message_id desc limit " + society_message_limit + ";";
        }
		SQLiteDatabase db=this.getReadableDatabase();
		Cursor c=db.rawQuery(s,null);
		society_message m;

        while(c.moveToNext())
		{
			m=new society_message();
            m.setMessage_id(c.getLong(0));
            m.setSociety_code(c.getString(1));
            m.setHeading(c.getString(2));
            m.setInfo(c.getString(3));
            m.setEvent_venue(c.getString(4));
            m.setDatetime(c.getString(5));
            m.setEvent_datetime(c.getString(6));
            m.setEvent_timestamp(c.getLong(7));
            m.setImage_url(c.getString(8));
            m.setImage_path(c.getString(9));
            m.setImage_size(c.getString(10));

            s="select society_name,society_icon_link,society_icon_path,society_color from societies where society_code='"+c.getString(1)+"'";
            Cursor soc=db.rawQuery(s,null);
            soc.moveToFirst();
            if(soc.getCount()!=0) {

                m.setSociety_color(soc.getString(3));
                m.setSociety_icon_path(soc.getString(2));
                m.setSociety_icon_link(soc.getString(1));
                m.setSociety_name(soc.getString(0));

            }
            messagelist.add(m);
		}
		return messagelist;
	}


	public society_message get_society_message(long m_id)
	{

		String s="select * from society_messages where message_id="+m_id;
		SQLiteDatabase db=this.getReadableDatabase();
		Cursor c=db.rawQuery(s,null);
		society_message m=new society_message();
        c.moveToFirst();
        m.setMessage_id(c.getLong(0));
        m.setSociety_code(c.getString(1));
        m.setHeading(c.getString(2));
        m.setInfo(c.getString(3));
        m.setEvent_venue(c.getString(4));
        m.setDatetime(c.getString(5));
        m.setEvent_datetime(c.getString(6));
        m.setEvent_timestamp(c.getLong(7));
        m.setImage_url(c.getString(8));
        m.setImage_path(c.getString(9));
        m.setImage_size(c.getString(10));

        s="select society_name,society_icon_link,society_icon_path,society_color from societies where society_code='"+c.getString(1)+"'";
        Cursor soc=db.rawQuery(s,null);
        soc.moveToFirst();
        if(soc.getCount()!=0) {

            m.setSociety_color(soc.getString(3));
            m.setSociety_icon_path(soc.getString(2));
            m.setSociety_icon_link(soc.getString(1));
            m.setSociety_name(soc.getString(0));

        }

		return m;
	}


	public int society_message_count()
	{
		String s="select * from society_messages ;";
		SQLiteDatabase db=this.getReadableDatabase();
		Cursor c=db.rawQuery(s,null);
		int x=0;
		while(c.moveToNext())
		{
			x++;
		}
		return x;
	}


	public void delete_old_society_message(long time)
	{
		SQLiteDatabase db=getWritableDatabase();
		String[] args = new String[] { String.valueOf(time),String.valueOf(-1) };
		db.delete("society_messages", "event_timestamp<? AND event_timestamp<>?", args);
		db.close();
	}

	public void delete_all_society_messages()
	{
		SQLiteDatabase db=getWritableDatabase();
		db.delete("society_messages", "1=1", null);
		db.close();
	}
	public void delete_society_message(long message_id)
	{
		SQLiteDatabase db=getWritableDatabase();
		String[] args = new String[] { String.valueOf(message_id) };
		db.delete("society_messages", "message_id="+String.valueOf(message_id), null);
		db.close();
	}

    public Long get_last_society_messageid()
	{
		String s="select max(message_id) from society_messages;";
		SQLiteDatabase db=this.getReadableDatabase();
		Cursor c=db.rawQuery(s,null);
		return c.getLong(0);
	}

    public void update_society_message_image_path(long message_id, String image_path) {
        SQLiteDatabase db=this.getWritableDatabase();
        String str = "UPDATE society_messages SET image_path ='"+image_path+"' WHERE message_id = "+ message_id;
        Log.e("db update query",str);
        db.execSQL(str);
        db.close();
    }

   // Functions for teacher_message table


    private String sender_user_name,sender_icon,info,datetime,file_path,sender_user_code,sender_user_type;
    private long message_id;
    private byte[] teacher_icon;


    public void insert_teacher_messages(ArrayList<teacher_message> messagelist)
    {
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues values=new ContentValues();
        for(teacher_message m:messagelist)
        {

            values.put("message_id",m.getMessage_id());
            values.put("sender_user_name",m.getSender_user_name());
            values.put("sender_user_code",m.getSender_user_code());
            values.put("sender_icon",m.getSender_icon());
            values.put("file_name",m.getFile_name());
            values.put("file_link",m.getFile_link());
            values.put("file_size",m.getFile_size());
            values.put("file_path",m.getFile_path());
            values.put("datetime",m.getDatetime());
            values.put("info",m.getInfo());

            db.insert("teacher_messages", null, values);
        }
        db.close();
    }

    public void insert_teacher_message(teacher_message m)
    {
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("message_id",m.getMessage_id());
        values.put("sender_user_name",m.getSender_user_name());
        values.put("sender_user_code",m.getSender_user_code());
        values.put("sender_icon",m.getSender_icon());
        values.put("file_name",m.getFile_name());
        values.put("file_link",m.getFile_link());
        values.put("file_size",m.getFile_size());
        values.put("file_path",m.getFile_path());
        values.put("datetime",m.getDatetime());
        values.put("info",m.getInfo());

        db.insert("teacher_messages", null, values);
    }

    public void update_file_path(Long message_id, String file_path) {
        SQLiteDatabase db=this.getWritableDatabase();
        String str = "UPDATE teacher_messages SET file_path ='"+file_path+"' WHERE message_id ="+message_id +";";
        Log.e("db update query",str);
        db.execSQL(str);
        db.close();

    }

    public ArrayList<Long> get_teacher_message_id(Long last_sync){
        ArrayList<Long> message_ids=new ArrayList<Long>();
        String s = "select message_id from teacher_messages where message_id>" + Long.toString(last_sync)  + ";";
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery(s,null);
        while(c.moveToNext()){
            message_ids.add(c.getLong(0));
        }
        return message_ids;
    }

    public ArrayList<teacher_message> get_teacher_messages(Long last_message_id)
    {
        ArrayList<teacher_message> messagelist=new ArrayList<teacher_message>();
        String s;
        if(last_message_id<0)
        s="select * from teacher_messages order by message_id desc limit "+teacher_message_limit+";";
        else
        {
                s = "select * from teacher_messages where message_id<" + Long.toString(last_message_id) + " order by message_id desc limit " + teacher_message_limit + ";";
        }
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery(s,null);
        teacher_message m;
        while(c.moveToNext())
        {
            m=new teacher_message();
            m.setMessage_id(c.getLong(0));
            m.setSender_user_code(c.getString(1));
            m.setSender_user_name(c.getString(2));
            m.setSender_user_type(c.getString(3));
            m.setSender_icon(c.getString(4));
            m.setInfo(c.getString(5));
            m.setDatetime(c.getString(6));
            m.setFile_name(c.getString(7));
            m.setFile_size(c.getString(8));
            m.setFile_link(c.getString(9));
            m.setFile_path(c.getString(10));
          /*  s="select teacher_icon from societies where society_code="+c.getString(1);
            Cursor soc=db.rawQuery(s,null);
            if(soc!=null) {
                m.setTeacher_icon(soc.getBlob(1));
            }*/
            messagelist.add(m);
        }
        return messagelist;
    }

    public Long get_last_teacher_messageid()
    {
        String s="select max(message_id) from teacher_messages;";
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery(s,null);
        return c.getLong(0);
    }

//Society table functions
    public void insert_societies(ArrayList<society> societies)
    {
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues values=new ContentValues();
        for(society m:societies)
        {
            values.put("society_code",m.getSociety_code());
            values.put("society_name",m.getSociety_name());
            //Log.d("byte[]",m.getSociety_icon().toString());
            values.put("society_icon_link",m.getSociety_icon_link());
            values.put("society_icon_path",m.getSociety_icon_path());
            values.put("society_color",m.getSociety_color());
            values.put("society_info",m.getSociety_info());
            db.insert("societies", null, values);
        }
        db.close();
    }


    public ArrayList<society> get_societies()
    {
        ArrayList<society> societies=new ArrayList<society>();
        String s="select * from societies;";
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery(s,null);
        society m;
        while(c.moveToNext())
        {
            m=new society();
            m.setSociety_code(c.getString(0));
            m.setSociety_name(c.getString(1));
            m.setSociety_icon_link(c.getString(2));
            m.setSociety_icon_path(c.getString(3));
            m.setSociety_info(c.getString(4));
            m.setSociety_color(c.getString(5));
            societies.add(m);
        }
        return societies;
    }





    public void deleteAllsocieties(){
        SQLiteDatabase db=getWritableDatabase();
        db.delete("societies","1=1",null);
        db.close();
    }



    public society get_society(String society_code)
    {
        SQLiteDatabase db=getWritableDatabase();
        String s="select society_name,society_icon_link,society_icon_path,society_color from societies where society_code='"+society_code+"'";
        Cursor soc=db.rawQuery(s,null);
        soc.moveToFirst();
        society m=new society();
        if(soc.getCount()!=0) {

            m.setSociety_color(soc.getString(3));
            m.setSociety_icon_path(soc.getString(2));
            m.setSociety_icon_link(soc.getString(1));
            m.setSociety_name(soc.getString(0));

        }
        return m;

    }

    public void update_society_icon_path(String society_code, String society_icon_path) {
        SQLiteDatabase db=this.getWritableDatabase();
        String str = "UPDATE societies SET society_icon_path ='"+society_icon_path+"' WHERE society_code ='"+ society_code+"';";
        Log.e("db update query",str);
        db.execSQL(str);
        db.close();

    }

    //Teacher subject group map functions
    public void insert_teacher_sub_grp_map(ArrayList<subject_group_bean> sub_grp_list)
    {
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues values=new ContentValues();
        for(subject_group_bean m:sub_grp_list)
        {
            values.put("year",m.getYear());
            values.put("course",m.getCourse());
            values.put("yeargroup",m.getGroup());
            values.put("subject_code",m.getSubject_code());
            values.put("LTP",m.getLTP());
            values.put("subject_group_code",m.getSubject_group_code());
            db.insert("teacher_sub_grp_map", null, values);
        }
        db.close();
    }


    public ArrayList<subject_group_bean>
    get_teacher_sub_grp_map()
    {
        ArrayList<subject_group_bean> sub_list=new ArrayList<subject_group_bean>();
        String s="select * from teacher_sub_grp_map;";
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery(s,null);
        subject_group_bean m;
        while(c.moveToNext())
        {
            m=new subject_group_bean();
            m.setYear(c.getString(0));
            m.setCourse(c.getString(1));
            m.setGroup(c.getString(2));
            m.setSubject_code(c.getString(3));
            m.setLTP(c.getString(4));
            m.setSubject_group_code(c.getString(5));
            sub_list.add(m);
        }
        return sub_list;
    }




    public ArrayList<subject_group_bean> get_teacher_subjects()
    {
        ArrayList<subject_group_bean> subjects=new ArrayList<subject_group_bean>();
        String s="select DISTINCT subject_code from teacher_sub_grp_map;";
        SQLiteDatabase db=getWritableDatabase();
        subject_group_bean sub;
        Cursor c=db.rawQuery(s,null);
        while(c.moveToNext())
        {
            sub=new subject_group_bean();
            sub.setSubject_code(c.getString(0));
            subjects.add(sub);
        }
        Log.e("Subjects",subjects.toString());
        return subjects;
    }




     public ArrayList<subject_group_bean> get_teacher_grps(ArrayList<String> subjects)
    {
        SQLiteDatabase db=getWritableDatabase();
        ArrayList<subject_group_bean> groups=new ArrayList<subject_group_bean>();
        String sql;
        Cursor c;
        subject_group_bean grp;
        for(String s:subjects)
        {
            sql="select * from teacher_sub_grp_map where subject_code='"+s+"';";
            c=db.rawQuery(sql,null);
            Log.e("testing","subject-"+s);
            grp=new subject_group_bean();
            grp.setSubject_code(s);
            grp.setFlag(2);
            groups.add(grp);
            Log.e("testing","subject-"+grp.getSubject_code()+" flag="+Integer.toString(grp.getFlag()));
            while(c.moveToNext())
            {
                grp=new subject_group_bean();
                grp.setYear(c.getString(0));
                grp.setCourse(c.getString(1));
                grp.setGroup(c.getString(2));
                grp.setSubject_code(c.getString(3));
                grp.setLTP(c.getString(4));
                grp.setSubject_group_code(c.getString(5));
                grp.setFlag(0);
                Log.e("testing","GROUP-"+grp.getGroup()+grp.getLTP()+" flag="+grp.getFlag());
                groups.add(grp);
            }
        }
        return groups;
    }
//student sub grp func

    public void insert_student_sub_grp_map(ArrayList<subject_group_bean> sub_grp_list)
    {
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues values=new ContentValues();
        for(subject_group_bean m:sub_grp_list)
        {
            values.put("year",m.getYear());
            values.put("course",m.getCourse());
            values.put("yeargroup",m.getGroup());
            values.put("subject_code",m.getSubject_code());
            values.put("LTP",m.getLTP());
            values.put("subject_group_code",m.getSubject_group_code());
            db.insert("student_sub_grp_map", null, values);
        }
        db.close();
    }

    public ArrayList<subject_group_bean>
    get_student_sub_grp_map()
    {
        ArrayList<subject_group_bean> sub_list=new ArrayList<subject_group_bean>();
        String s="select * from student_sub_grp_map;";
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery(s,null);
        subject_group_bean m;
        while(c.moveToNext())
        {
            m=new subject_group_bean();
            m.setYear(c.getString(0));
            m.setCourse(c.getString(1));
            m.setGroup(c.getString(2));
            m.setSubject_code(c.getString(3));
            m.setLTP(c.getString(4));
            m.setSubject_group_code(c.getString(5));
            sub_list.add(m);
        }
        return sub_list;
    }



    public void insert_extra_grp(ArrayList<subject_group_bean> sub_grp_list)
    {
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues values=new ContentValues();
        for(subject_group_bean m:sub_grp_list)
        {
            values.put("year",m.getYear());
            values.put("course",m.getCourse());
            values.put("yeargroup",m.getGroup());
            values.put("group_code",m.getSubject_group_code());
            db.insert("extra_grp", null, values);
        }
        db.close();
    }

    public ArrayList<subject_group_bean>
    get_extra_grp()
    {
        ArrayList<subject_group_bean> sub_list=new ArrayList<subject_group_bean>();
        String s="select * from extra_grp order by course,year,yeargroup;";
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery(s,null);
        subject_group_bean m;
        while(c.moveToNext())
        {
            m=new subject_group_bean();
            m.setYear(c.getString(0));
            m.setCourse(c.getString(1));
            m.setGroup(c.getString(2));
            m.setSubject_group_code(c.getString(3));
            sub_list.add(m);
        }
        return sub_list;
    }

    public ArrayList<subject_group_bean> get_extra_year_course(){
        String s="select distinct year,course from extra_grp order by course,year;";
        SQLiteDatabase db=getWritableDatabase();
        ArrayList<subject_group_bean> groups=new ArrayList<subject_group_bean>();
        Cursor c=db.rawQuery(s,null);
        subject_group_bean m;
        while(c.moveToNext())
        {
            m=new subject_group_bean();
            m.setYear(c.getString(0));
            m.setCourse(c.getString(1));
            groups.add(m);
        }
        return groups;
    }
    public ArrayList<subject_group_bean> get_extra_grp(ArrayList<String> year,ArrayList<String> course){


        SQLiteDatabase db=getWritableDatabase();
        ArrayList<subject_group_bean> groups=new ArrayList<subject_group_bean>();
        String sql;
        Cursor c;
        subject_group_bean grp;
        for(int i=0;i<year.size();i++){
            String yr=year.get(i),crse=course.get(i);
            sql="select * from extra_grp where year='"+yr+"' AND course='"+crse+"' order by yeargroup;";
            c=db.rawQuery(sql,null);
            grp=new subject_group_bean();
            grp.setYear(yr);
            grp.setFlag(2);
            grp.setCourse(crse);
            groups.add(grp);
            while(c.moveToNext())
            {
                grp=new subject_group_bean();
                grp.setYear(c.getString(0));
                grp.setCourse(c.getString(1));
                grp.setGroup(c.getString(2));
                grp.setSubject_group_code(c.getString(3));
                grp.setFlag(0);
                Log.e("testing","GROUP-"+grp.getGroup()+" flag="+grp.getFlag());
                groups.add(grp);
            }
        }

        Log.e("groups",Integer.toString(groups.size()));
        return groups;
    }


    //hostel message functions

    public void insert_hostel_message(hostel_message m)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("message_id",m.getMessage_id());
        values.put("heading",m.getHeading());
        values.put("info",m.getInfo());
        values.put("datetime",m.getDatetime());
        db.insert("hostel_messages",null,values);
        db.close();
    }
    public void insert_hostel_messages(ArrayList<hostel_message> msgs)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
    for (hostel_message m:msgs)
    {
        values.put("message_id",m.getMessage_id());
        values.put("heading",m.getHeading());
        values.put("info",m.getInfo());
        values.put("datetime",m.getDatetime());
        db.insert("hostel_messages",null,values);
    }
    db.close();
    }

    public ArrayList<Long> get_hostel_message_id(Long last_sync){
        ArrayList<Long> message_ids=new ArrayList<Long>();
        String s = "select message_id from hostel_messages where message_id>" + Long.toString(last_sync)  + ";";
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery(s,null);
        while(c.moveToNext()){
            message_ids.add(c.getLong(0));
        }
        return message_ids;
    }
    public ArrayList<hostel_message> get_hostel_messages(Long last_message_id)
    {
        ArrayList<hostel_message> msgs=new ArrayList<hostel_message>();
        String s;
        if(last_message_id<0)
            s="select * from hostel_messages order by message_id desc limit "+hostel_message_limit+";";
        else
        {
            s = "select * from hostel_messages where message_id<" + Long.toString(last_message_id) + " order by message_id desc limit " + hostel_message_limit + ";";
        }

        SQLiteDatabase db=this.getWritableDatabase();
        Cursor c=db.rawQuery(s,null);
        hostel_message m;
        while(c.moveToNext())
        {
            m=new hostel_message();
            m.setMessage_id(c.getLong(0));
            m.setHeading(c.getString(1));
            m.setInfo(c.getString(2));
            m.setDatetime(c.getString(3));
            msgs.add(m);
        }
        return msgs;
    }



}
