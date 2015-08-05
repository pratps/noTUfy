package notufy.thapar.com.notufy.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

import notufy.thapar.com.notufy.R;
import notufy.thapar.com.notufy.config;

public class settings extends ActionBarActivity {

    SwitchCompat notification_toggle,sound_toggle,image_toggle;
    Context mContext;
    SharedPreferences settings;
    Toolbar mToolBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mContext=getApplicationContext();
        config conf=new config(mContext);
        settings=conf.getSettings();

        mToolBar = (Toolbar) findViewById(R.id.screen_default_toolbar);
        mToolBar.setTitle("bla bla");
        mToolBar.setNavigationIcon(R.drawable.material_ic_drawer_menu_navigation);

        setSupportActionBar(mToolBar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);


        notification_toggle=(SwitchCompat)findViewById(R.id.notification_toggle);
        sound_toggle=(SwitchCompat)findViewById(R.id.sound_toggle);
        image_toggle=(SwitchCompat)findViewById(R.id.image_toggle);

        notification_toggle.setChecked(settings.getBoolean("notification",true));
        sound_toggle.setChecked(settings.getBoolean("notification_sound",true));
        image_toggle.setChecked(settings.getBoolean("image_download",false));

        final SharedPreferences.Editor editor=settings.edit();
        notification_toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("notification",isChecked).apply();
            }
        });
        sound_toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("notification_sound",isChecked).apply();
            }
        });
        image_toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("image_download",isChecked).apply();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
