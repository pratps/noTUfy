package notufy.thapar.com.notufy.GCM;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;

import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import notufy.thapar.com.notufy.Activity.MainActivity;
import notufy.thapar.com.notufy.Activity.logined;
import notufy.thapar.com.notufy.Beans.hostel_message;
import notufy.thapar.com.notufy.Beans.society_message;
import notufy.thapar.com.notufy.Beans.teacher_message;
import notufy.thapar.com.notufy.R;
import notufy.thapar.com.notufy.dataBase.dataBase;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GcmIntentService extends IntentService {
	
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;
	static final String TAG = "GCM";
    Boolean flag=true;
	dataBase db;
	

	public GcmIntentService() {
		super("GcmIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {

        Log.e("inside onHandleIntent","onHandleIntent");
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		// The getMessageType() intent parameter must be the intent you received
		// in your BroadcastReceiver.
		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty()) {

            Log.e("inside", "extra if");
            Log.e("recieved bundle",extras.toString());
            Log.e("messagetype",messageType);

			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
					.equals(messageType)) {
                Log.e("inside", "error");

			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
					.equals(messageType)) {
                Log.e("inside", "delete");

			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
					.equals(messageType))
            {
                Log.e("recieved bundle",extras.toString());
				switch(extras.getString("msg_type"))
                {
                    case "0":parseTeachermessage(extras);
                        break;
                    case "1":parseSocietymessage(extras);
                        break;
                    case "2":parseHostelmessage(extras);
                        break;
                }
				}	
			}
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        Log.e("outside", "if");
        GcmBroadcastReceiver.completeWakefulIntent(intent);
			}
	



	// Put the message into a notification and post it.
	// This is just one simple example of what you might choose to do with
	// a GCM message.
	public void sendNotification(String msg, String title) {
		mNotificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, logined.class), 0);
		
		
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.cp).setContentTitle(title)
				.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
				.setAutoCancel(true)
				.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
				.setContentText(msg)
				.setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_SOUND);
		mBuilder.setContentIntent(contentIntent);
		mNotificationManager.notify(1, mBuilder.build());
		
	}
    private String nullcheck(String s) {
        if (s != null)
            return s;
        else
            return "";
    }
    void parseTeachermessage(Bundle extras)
    {
        teacher_message m = new teacher_message();
        try {
            m.setMessage_id(Long.parseLong(extras.getString("message_id")));
        }
        catch (Exception e) {
            m.setMessage_id(0);
            flag=false;
        }
        m.setSender_user_name(nullcheck(extras.getString("sender_user_name")));
        m.setSender_user_code(nullcheck(extras.getString("sender_user_code")));
        m.setSender_user_type(nullcheck(extras.getString("sender_user_type")));
        m.setInfo(nullcheck(extras.getString("info")));
        m.setDatetime(nullcheck(extras.getString("datetime")));
        m.setFile_name(nullcheck(extras.getString("file_name")));
        m.setFile_size(nullcheck(extras.getString("file_size")));
        m.setFile_link(nullcheck(extras.getString("file_link")));
        Log.e("test", "item recieved");
        db = new dataBase(this);
        if(m.getSender_user_name().length()>0&&flag)
        {
            db.insert_teacher_message(m);
            Log.e("test", "item inserted in db");
            sendNotification(m.getInfo(), m.getSender_user_name());
        }

    }
    void parseSocietymessage(Bundle extras)
    {
        society_message m=new society_message();
        db=new dataBase(getApplication());
        try {
            m.setMessage_id(Long.parseLong(extras.getString("message_id")));
        }
        catch (Exception e) {
            m.setMessage_id(0);
            flag=false;
        }

        m.setHeading(nullcheck(extras.getString("heading")));
        m.setInfo(nullcheck(extras.getString("info")));
        m.setSociety_code(nullcheck(extras.getString("society_code")));
        m.setDatetime(nullcheck(extras.getString("datetime")));
        m.setEvent_datetime(nullcheck(extras.getString("event_datetime")));
        m.setEvent_venue(nullcheck(extras.getString("event_venue")));
        m.setImage_url(nullcheck(extras.getString("image_link")));
        m.setImage_size(nullcheck(extras.getString("image_size")));
        if(m.getHeading().length()>0&&m.getSociety_code().length()>0&&flag)
        {
            db.insert_society_message(m);
            sendNotification(m.getInfo(),m.getHeading());
        }

    }

    void parseHostelmessage(Bundle extras)
    {
        hostel_message m=new hostel_message();
        db=new dataBase(getApplication());
        try {
            m.setMessage_id(Long.parseLong(extras.getString("message_id")));
        }
        catch (Exception e) {
            m.setMessage_id(0);
            flag=false;
        }
        m.setHeading(nullcheck(extras.getString("heading")));
        m.setInfo(nullcheck(extras.getString("info")));
        m.setDatetime(nullcheck(extras.getString("datetime")));
        if(m.getHeading().length()>0&&flag)
        {
            db.insert_hostel_message(m);
            sendNotification(m.getInfo(),m.getHeading());
        }

    }

}