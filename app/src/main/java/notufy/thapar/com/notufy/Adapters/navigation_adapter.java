package notufy.thapar.com.notufy.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import notufy.thapar.com.notufy.R;

/**
 * Created by prat on 2/27/2015.
 */
public class navigation_adapter extends BaseAdapter {
    Context context;
    String[] textview={"Societies","Send a message","Send message to teachers","Send to dept students","Settings ","Send to hostellers."};
    int[] image_icon={R.drawable.ic_action_settings,R.drawable.ic_action_settings,R.drawable.ic_action_settings,R.drawable.ic_action_settings,R.drawable.ic_action_settings,R.drawable.ic_action_settings};
    public navigation_adapter(Context context) {
        this.context=context;
    }
    public int getCount() {
        return textview.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return textview[position];
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v=null;
        if(convertView==null){
            LayoutInflater inflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            v=inflater.inflate(R.layout.layout_navigation,parent,false);
        }
        else{
            v=convertView;}
        ImageView image=(ImageView)v.findViewById(R.id.nav_image);
        TextView text=(TextView)v.findViewById(R.id.nav_text);
        image.setImageResource(image_icon[position]);
        text.setText(textview[position]);

        return v;
    }

}