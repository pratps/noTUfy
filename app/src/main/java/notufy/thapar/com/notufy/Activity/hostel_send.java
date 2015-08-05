package notufy.thapar.com.notufy.Activity;

import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import notufy.thapar.com.notufy.R;

public class hostel_send extends ActionBarActivity {

    Toolbar mToolBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_hostel2);

        mToolBar = (Toolbar) findViewById(R.id.screen_default_toolbar);
        mToolBar.setTitle("Send to Hostel J");
        mToolBar.setNavigationIcon(R.drawable.material_ic_drawer_menu_navigation);

        setSupportActionBar(mToolBar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);



        int padding_in_dp = 10;  // 6 dps
        final float scale = getResources().getDisplayMetrics().density;
        int px = (int) (padding_in_dp * scale + 0.5f);
        TextView heading=(TextView)findViewById(R.id.heading);
        TextView info=(TextView)findViewById(R.id.info);
        info.setPadding(px, px, px, px);
        heading.setPadding(px, px, px, px);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hostel_send, menu);
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
        if(id==android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }
}
