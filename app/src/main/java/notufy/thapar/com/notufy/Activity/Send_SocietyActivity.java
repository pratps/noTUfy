package notufy.thapar.com.notufy.Activity;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.doomonafireball.betterpickers.datepicker.DatePickerBuilder;
import com.doomonafireball.betterpickers.datepicker.DatePickerDialogFragment;
import com.doomonafireball.betterpickers.timepicker.TimePickerBuilder;
import com.doomonafireball.betterpickers.timepicker.TimePickerDialogFragment;

import notufy.thapar.com.notufy.R;

/**
 * Created by pranav vij on 8/12/2015.
 */
public class Send_SocietyActivity extends ActionBarActivity {
    ImageView calender;
    ImageView time;
    ImageView attach_file;
    TextView dateTextView,timeTextView,venueTextView;
    DatePickerBuilder datePicker;
    TimePickerBuilder timePicker;
    Toolbar toolbar;
    int PICK_IMAGE=1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_society_message);
        toolbar=(Toolbar)findViewById(R.id.society_toolbar);
        calender=(ImageView)findViewById(R.id.set_date);
        time=(ImageView)findViewById(R.id.set_time);
        attach_file=(ImageView)findViewById(R.id.attach_image);
        dateTextView=(TextView)findViewById(R.id.settext_date);
        timeTextView=(TextView)findViewById(R.id.settext_time);
        venueTextView=(TextView)findViewById(R.id.settext_venue);
        setSupportActionBar(toolbar);
        attach_file.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, ""), PICK_IMAGE);

            }
        });
        calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker = new DatePickerBuilder()
                        .setFragmentManager(getSupportFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment);
                datePicker.addDatePickerDialogHandler(new DatePickerDialogFragment.DatePickerDialogHandler() {
                    @Override
                    public void onDialogDateSet(int i, int i1, int i2, int i3) {
                        dateTextView.setText(i3+":"+i1+":"+i2);
                    }
                });
                datePicker.show();
            }
        });
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker = new TimePickerBuilder()
                        .setFragmentManager(getSupportFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment);
                timePicker.addTimePickerDialogHandler(new TimePickerDialogFragment.TimePickerDialogHandler() {
                    @Override
                    public void onDialogTimeSet(int i, int i1, int i2) {
                        timeTextView.setText(i1+":"+i2);
                    }
                });
                timePicker.show();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_CANCELED) {
            if (requestCode == PICK_IMAGE) {
                Uri selectedImageUri = data.getData();
            }
        }

    }
}
